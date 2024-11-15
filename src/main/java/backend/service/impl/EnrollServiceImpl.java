package backend.service.impl;

import backend.enums.DeadlineEnum;

import backend.exception.model.deadline.DeadlineExceedException;
import backend.exception.model.deadline.DeadlineNotFoundException;
import backend.exception.model.enroll.EnrollExceedException;
import backend.exception.model.enroll.EnrollInvalidException;
import backend.exception.model.enroll.EnrollNotFoundException;
import backend.exception.model.major.MajorNotFoundException;
import backend.exception.model.user.UserNotFoundException;
import backend.exception.model.user.UserRoleDeniedException;

import backend.mapper.*;

import backend.model.DTO.EnrollConfirmDTO;

import backend.model.VO.department.DepartmentVO;
import backend.model.VO.enroll.EnrollSelectVO;
import backend.model.VO.enroll.EnrollVO;
import backend.model.VO.major.MajorFirstVO;
import backend.model.VO.user.UserProfileVO;

import backend.model.converter.DepartmentConverter;
import backend.model.converter.MajorConverter;
import backend.model.converter.UserConverter;

import backend.model.entity.*;

import backend.service.EnrollService;
import backend.util.FieldsGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EnrollServiceImpl implements EnrollService {
    @Autowired
    private EnrollMapper enrollMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DeadlineMapper deadlineMapper;

    @Autowired
    private MajorToTeacherMapper majorToTeacherMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private DepartmentConverter departmentConverter;

    @Autowired
    private MajorConverter majorConverter;


    private void verifyDeadline(DeadlineEnum type) {
        List<Deadline> deadlines = deadlineMapper.selectDeadline(
                Deadline.builder().type(type.getValue()).build(),
                FieldsGenerator.generateFields(Deadline.class)
        );
        Deadline targetDeadline = deadlines.isEmpty() ? null : deadlines.getFirst();

        if (targetDeadline == null)
            throw new DeadlineNotFoundException();

        if (LocalDateTime.now().isAfter(targetDeadline.getTime()))
            throw new DeadlineExceedException();
    }


    /**
     * GET /unauthorized/enroll
     * */
    @Override
    public List<EnrollVO> getEnrollTable(Integer department, Integer year) {

        DeadlineEnum ddl = DeadlineEnum.ENROLL;

        try {
            verifyDeadline(ddl);
            List<Enroll> enrolls = enrollMapper.selectEnrollWithDept(department, year);

            return enrolls
                    .stream()
                    .map(e -> {
                        List<User> students = userMapper.selectUser(
                                User.builder().id(e.getSid()).build(),
                                FieldsGenerator.generateFields(User.class)
                        );
                        UserProfileVO student = students.isEmpty() ?
                                null :
                                userConverter.UserToUserProfileVO(students.getFirst());

                        List<User> teachers = userMapper.selectUser(
                                User.builder().id(e.getTid()).build(),
                                FieldsGenerator.generateFields(User.class)
                        );
                        UserProfileVO teacher = teachers.isEmpty() ?
                                null :
                                userConverter.UserToUserProfileVO(teachers.getFirst());


                        List<Department> departments = departmentMapper.selectDepartment(
                                Department.builder().id(department).build(),
                                FieldsGenerator.generateFields(Department.class)
                        );
                        DepartmentVO dept = departments.isEmpty() ?
                                null :
                                departmentConverter.DepartmentToDepartmentVO(departments.getFirst());

                        List<Major> majors = majorMapper.selectMajor(
                                Major.builder().id(e.getMid()).build(),
                                FieldsGenerator.generateFields(Major.class)
                        );
                        MajorFirstVO major = majors.isEmpty() ?
                                null :
                                majorConverter.MajorToMajorFirstVO(majors.getFirst());

                        return EnrollVO
                                .builder()
                                .enrollmentID(e.getId())
                                .student(student)
                                .teacher(teacher)
                                .department(dept)
                                .major(major)
                                .build();

                    })
                    .filter(Objects::nonNull)
                    .toList();

        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(ddl, 403);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    /**
     * POST /enroll
     * --------------
     * 1. Verify deadline
     * 2. Verify identity / role
     * 3. Verify field integrity
     * 4. Verify whether this teacher is valid / has remaining metric in this major
     * 5. Insert enroll record
     * 6. Update MajorToTeacher record
     * 7. Update Teacher remaining count (Do NOT perform in-place subtraction)
     * */
    @Override
    public void enrollConfirm(EnrollConfirmDTO confirm) {
        DeadlineEnum ddl = DeadlineEnum.ENROLL;
        User user = User.getAuth();
        Teacher teacher = user.getTeacher();

        try {
            if (teacher == null || teacher.getIdentity() < 1 || teacher.getIdentity() > 2)
                throw new UserRoleDeniedException();

            verifyDeadline(ddl);

            if (studentMapper.selectStudent(
                    Student.builder().userId(confirm.getStudentID()).valid(1).build(),
                    Map.of("id", true)
            ).isEmpty())
                throw new UserNotFoundException();

            if (majorMapper.selectMajor(
                    Major.builder().id(confirm.getMajorID()).build(),
                    Map.of("id", true)
            ).isEmpty())
                throw new MajorNotFoundException();


            List<MajorToTeacher> teachers = majorToTeacherMapper.selectMajorToTeacher(
                    MajorToTeacher.builder().mid(confirm.getMajorID()).tid(teacher.getUserId()).build(),
                    FieldsGenerator.generateFields(MajorToTeacher.class)
            );

            MajorToTeacher target = teachers.isEmpty() ? null : teachers.getFirst();

            if (target == null || target.getValid() == 0)
                throw new EnrollInvalidException();

            if (target.getRemnant() == 0)
                throw new EnrollExceedException();

            enrollMapper.insertEnroll(
                    Enroll.builder().mid(confirm.getMajorID()).tid(teacher.getUserId()).sid(confirm.getStudentID())
                            .disabled(1).year(LocalDateTime.now().getYear() + 1).build()
            );

            target.setRemnant(target.getRemnant() - 1);
            majorToTeacherMapper.updateMajorToTeacher(
                    target,
                    MajorToTeacher.builder().tid(teacher.getUserId()).mid(confirm.getMajorID()).build()
            );

            teacher.setRemnant(
                    majorToTeacherMapper
                            .selectMajorToTeacher(
                                    MajorToTeacher.builder().tid(teacher.getUserId()).valid(1).build(),
                                    Map.of("remnant", true)
                            )
                            .stream()
                            .mapToInt(MajorToTeacher::getRemnant)
                            .sum()

            );
            teacherMapper.updateTeacher(teacher, Teacher.builder().userId(teacher.getUserId()).build());


        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(ddl, 403);
        } catch (UserRoleDeniedException userRoleDeniedException) {
            throw new UserRoleDeniedException(user.getRole(), 4031, teacher == null ? null : teacher.getIdentity());
        } catch (EnrollInvalidException enrollInvalidException) {
            throw new EnrollInvalidException(teacher == null ? null : teacher.getUserId(), confirm.getMajorID(), 400);
        } catch (EnrollExceedException enrollExceedException) {
            throw new EnrollExceedException(teacher == null ? null : teacher.getUserId(), confirm.getMajorID(), 4001);
        } catch (UserNotFoundException userNotFoundException) {
            throw new UserNotFoundException(confirm.getStudentID(), 1, 4041);
        } catch (MajorNotFoundException majorNotFoundException) {
            throw new MajorNotFoundException(confirm.getMajorID(), 4042);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * DELETE /enroll
     * --------------
     * 1. Verify deadline
     * 2. Verify identity / role
     * 3. Verify field integrity
     * 4. Update enroll record
     * 5. Update MajorToTeacher record
     * 6. Update Teacher's remaining count (Do NOT perform in-place addition)
     * */
    @Override
    public void enrollCancel(Integer enroll) {
        DeadlineEnum ddl = DeadlineEnum.ENROLL;
        User user = User.getAuth();
        Teacher teacher = user.getTeacher();

        try {
            if (teacher == null || teacher.getIdentity() < 1 || teacher.getIdentity() > 2)
                throw new UserRoleDeniedException();

            verifyDeadline(ddl);

            List<Enroll> enrolls = enrollMapper.selectEnroll(
                    Enroll.builder().id(enroll).build(),
                    FieldsGenerator.generateFields(Enroll.class)
            );

            if (enrolls.isEmpty())
                throw new EnrollNotFoundException();

            Enroll targetEnroll = enrolls.getFirst();
            targetEnroll.setDisabled(0);
            enrollMapper.updateEnroll(targetEnroll, Enroll.builder().id(enroll).build());

            MajorToTeacher majorToTeacher = majorToTeacherMapper.selectMajorToTeacher(
                    MajorToTeacher.builder().mid(targetEnroll.getMid()).tid(targetEnroll.getTid()).build(),
                    FieldsGenerator.generateFields(MajorToTeacher.class)
            ).getFirst();
            majorToTeacher.setRemnant(majorToTeacher.getRemnant() + 1);
            majorToTeacherMapper.updateMajorToTeacher(
                    majorToTeacher,
                    MajorToTeacher.builder().id(majorToTeacher.getId()).build()
            );

            teacher.setRemnant(
                    majorToTeacherMapper
                            .selectMajorToTeacher(
                                    MajorToTeacher.builder().tid(teacher.getUserId()).valid(1).build(),
                                    Map.of("remnant", true)
                            )
                            .stream()
                            .mapToInt(MajorToTeacher::getRemnant)
                            .sum()

            );
            teacherMapper.updateTeacher(teacher, Teacher.builder().userId(teacher.getUserId()).build());


        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(ddl, 403);
        } catch (UserRoleDeniedException userRoleDeniedException) {
            throw new UserRoleDeniedException(user.getRole(), 4031, teacher == null ? null : teacher.getIdentity());
        } catch (EnrollNotFoundException enrollNotFoundException) {
            throw new EnrollNotFoundException(enroll);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * GET /enroll/list
     * --------------
     * 1. Verify identity / role
     * 2. Select target enroll records
     * */
    @Override
    public List<EnrollSelectVO> getEnrollList() {
        User user = User.getAuth();
        Teacher teacher = user.getTeacher();

        try {
            if (teacher == null || teacher.getIdentity() < 1 || teacher.getIdentity() > 2)
                throw new UserRoleDeniedException();

            List<Enroll> enrolls = enrollMapper.selectEnrollWithDept(
                    teacher.getDepartment(),
                    LocalDateTime.now().getYear() + 1
            );

            return enrolls
                    .stream()
                    .map(e -> {
                        List<User> students = userMapper.selectUser(
                                User.builder().id(e.getSid()).build(),
                                FieldsGenerator.generateFields(User.class)
                        );
                        UserProfileVO student = students.isEmpty() ?
                                null :
                                userConverter.UserToUserProfileVO(students.getFirst());

                        List<Major> majors = majorMapper.selectMajor(
                                Major.builder().id(e.getMid()).build(),
                                FieldsGenerator.generateFields(Major.class)
                        );
                        MajorFirstVO major = majors.isEmpty() ?
                                null :
                                majorConverter.MajorToMajorFirstVO(majors.getFirst());

                        return EnrollSelectVO
                                .builder()
                                .enrollmentID(e.getId())
                                .student(student)
                                .major(major)
                                .build();

                    })
                    .filter(Objects::nonNull)
                    .toList();

        } catch (UserRoleDeniedException userRoleDeniedException) {
            throw new UserRoleDeniedException(user.getRole(), 4031, teacher == null ? null : teacher.getIdentity());
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
