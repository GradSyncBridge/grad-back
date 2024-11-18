package backend.service.impl;

import backend.enums.DeadlineEnum;

import backend.exception.model.deadline.DeadlineExceedException;
import backend.exception.model.deadline.DeadlineNotFoundException;
import backend.exception.model.deadline.DeadlineUnreachedException;
import backend.exception.model.enroll.EnrollDuplicateException;
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

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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


    private void verifyDeadline(DeadlineEnum type, Boolean after) {
        List<Deadline> deadlines = deadlineMapper.selectDeadline(
                Deadline.builder().type(type.getValue()).build(),
                FieldsGenerator.generateFields(Deadline.class)
        );
        Deadline targetDeadline = deadlines.isEmpty() ? null : deadlines.getFirst();

        if (targetDeadline == null)
            throw new DeadlineNotFoundException();

        if (after) {
            if (LocalDateTime.now().isAfter(targetDeadline.getTime()))
                throw new DeadlineExceedException();
        } else {
            if (LocalDateTime.now().isBefore(targetDeadline.getTime()))
                throw new DeadlineUnreachedException();
        }

    }


    /**
     * 获取学院录取学生
     * GET /unauthorized/enroll
     *
     * @param department 学院
     * @param year       年份
     * @return 录取学生列表
     */
    @Override
    public List<EnrollVO> getEnrollTable(Integer department, Integer year) {

        DeadlineEnum ddl = DeadlineEnum.ENROLL;

        try {
            verifyDeadline(ddl, false);
            List<Enroll> enrolls = enrollMapper.selectEnrollWithDept(department, year);

            List<CompletableFuture<EnrollVO>> enrollVOListFuture = new ArrayList<>();
            for (Enroll e : enrolls) {
                CompletableFuture<UserProfileVO> studentFuture =
                        CompletableFuture.supplyAsync(() -> {
                            List<User> students = userMapper.selectUser(
                                    User.builder().id(e.getSid()).build(),
                                    FieldsGenerator.generateFields(User.class)
                            );
                            return students.isEmpty() ? null : userConverter.UserToUserProfileVO(students.getFirst());
                        });

                CompletableFuture<UserProfileVO> teacherFuture =
                        CompletableFuture.supplyAsync(() -> {
                            List<User> teachers = userMapper.selectUser(
                                    User.builder().id(e.getTid()).build(),
                                    FieldsGenerator.generateFields(User.class)
                            );
                            return teachers.isEmpty() ? null : userConverter.UserToUserProfileVO(teachers.getFirst());
                        });

                CompletableFuture<DepartmentVO> departmentFuture =
                        CompletableFuture.supplyAsync(() -> {
                            List<Department> departments = departmentMapper.selectDepartment(
                                    Department.builder().id(department).build(),
                                    FieldsGenerator.generateFields(Department.class)
                            );
                            return departments.isEmpty() ? null : departmentConverter.DepartmentToDepartmentVO(departments.getFirst());
                        });

                CompletableFuture<MajorFirstVO> majorFuture =
                        CompletableFuture.supplyAsync(() -> {
                            List<Major> majors = majorMapper.selectMajor(
                                    Major.builder().id(e.getMid()).build(),
                                    FieldsGenerator.generateFields(Major.class)
                            );
                            return majors.isEmpty() ? null : majorConverter.MajorToMajorFirstVO(majors.getFirst());
                        });

                CompletableFuture<EnrollVO> enrollVOFuture = studentFuture
                        .thenCombine(teacherFuture, (student, teacher) ->
                                EnrollVO.builder().student(student).teacher(teacher)
                        )
                        .thenCombine(departmentFuture, EnrollVO.EnrollVOBuilder::department)
                        .thenCombine(majorFuture, EnrollVO.EnrollVOBuilder::major)
                        .thenApply(enroll ->
                                EnrollVO.builder()
                                        .enrollmentID(e.getId())
                                        .student(enroll.build().getStudent())
                                        .teacher(enroll.build().getTeacher())
                                        .department(enroll.build().getDepartment())
                                        .major(enroll.build().getMajor())
                                        .build()
                        );

                enrollVOListFuture.add(enrollVOFuture);
            }

            CompletableFuture.allOf(enrollVOListFuture.toArray(new CompletableFuture[0])).join();
            return enrollVOListFuture.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (DeadlineUnreachedException deadlineUnreachedException) {
            throw new DeadlineUnreachedException(ddl, 403);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    /**
     * 导师确认录取学生
     * POST /enroll
     *
     * @param confirm 录取信息
     *                --------------
     *                1. 验证截止日期
     *                2. 验证身份证和用户类型
     *                3. 验证信息完整性
     *                4. 验证当前老师是否有资格并且当前专业还有录取名额
     *                5. 插入录取记录
     *                6. 更新MajorToTeacher记录
     *                7. 更新教师录取名额 (Do NOT perform in-place subtraction)
     */
    @Override
    public void enrollConfirm(EnrollConfirmDTO confirm) {
        DeadlineEnum ddl = DeadlineEnum.ENROLL;
        User user = User.getAuth();
        Teacher teacher = user.getTeacher();

        try {
            if (teacher == null || teacher.getIdentity() < 1 || teacher.getIdentity() > 2)
                throw new UserRoleDeniedException();

            verifyDeadline(ddl, true);

            if (studentMapper.selectStudent(
                    Student.builder().userId(confirm.getStudentID()).valid(0).build(),
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

            Enroll targetEnroll = Enroll.builder().mid(confirm.getMajorID())
                    .tid(teacher.getUserId()).sid(confirm.getStudentID())
                    .disabled(1).year(LocalDateTime.now().getYear() + 1).build();
            List<Enroll> enrollList = enrollMapper.selectEnroll(
                    targetEnroll,
                    FieldsGenerator.generateFields(Enroll.class)
            );

            if (!enrollList.isEmpty())
                throw new EnrollDuplicateException();

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


        } catch (EnrollDuplicateException enrollDuplicateException) {
            throw new EnrollDuplicateException(
                    String.format(
                            "tid=%d, sid=%d, mid=%d",
                            teacher == null ? null : teacher.getUserId(),
                            confirm.getStudentID(),
                            confirm.getMajorID()
                    ),
                    409
            );
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
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 导师取消录取关系
     * DELETE /enroll
     *
     * @param enroll 录取记录
     *               --------------
     *               1. 验证截至日期
     *               2. 验证身份证和用户类别
     *               3. 验证信息完整性
     *               4. 更新录取记录
     *               5. 更新MajorToTeacher记录
     *               6. 更新教师录取名额 (Do NOT perform in-place addition)
     */
    @Override
    public void enrollCancel(Integer enroll) {
        DeadlineEnum ddl = DeadlineEnum.ENROLL;
        User user = User.getAuth();
        Teacher teacher = user.getTeacher();

        try {
            if (teacher == null || teacher.getIdentity() < 1 || teacher.getIdentity() > 2)
                throw new UserRoleDeniedException();

            verifyDeadline(ddl, true);

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
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 导师查看已选学生信息
     * GET /enroll/list
     *
     * @return 录取列表
     * --------------
     * 1. 验证身份证和用户类别
     * 2. 获取目标老师录取列表
     */
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

            List<CompletableFuture<EnrollSelectVO>> futures = new ArrayList<>();

            for (Enroll e : enrolls) {
                CompletableFuture<UserProfileVO> studentFuture =
                        CompletableFuture.supplyAsync(() -> {
                            List<User> students = userMapper.selectUser(
                                    User.builder().id(e.getSid()).build(),
                                    FieldsGenerator.generateFields(User.class)
                            );
                            return students.isEmpty() ? null : userConverter.UserToUserProfileVO(students.getFirst());
                        });

                CompletableFuture<MajorFirstVO> majorFuture =
                        CompletableFuture.supplyAsync(() -> {
                            List<Major> majors = majorMapper.selectMajor(
                                    Major.builder().id(e.getMid()).build(),
                                    FieldsGenerator.generateFields(Major.class)
                            );
                            return majors.isEmpty() ? null : majorConverter.MajorToMajorFirstVO(majors.getFirst());
                        });

                CompletableFuture<EnrollSelectVO> enrollSelectVOFuture = studentFuture
                        .thenCombine(majorFuture, (student, major) ->
                                EnrollSelectVO.builder().student(student).major(major).build()
                        );

                futures.add(enrollSelectVOFuture);
            }

            return futures.stream()
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (UserRoleDeniedException userRoleDeniedException) {
            throw new UserRoleDeniedException(user.getRole(), 4031, teacher == null ? null : teacher.getIdentity());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
