package backend.service.impl;

import backend.enums.DeadlineEnum;

import backend.exception.model.deadline.DeadlineNotFoundException;
import backend.exception.model.deadline.DeadlineUnreachedException;
import backend.exception.model.user.UserNotFoundException;

import backend.mapper.DeadlineMapper;
import backend.mapper.MajorMapper;
import backend.mapper.StudentMapper;
import backend.mapper.TeacherMapper;

import backend.model.DTO.AdminDeadlineDTO;
import backend.model.DTO.AdminTeacherDTO;
import backend.model.VO.teacher.TeacherProfileVO;

import backend.model.entity.*;

import backend.service.AdminService;
import backend.util.FieldsGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private DeadlineMapper deadlineMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 管理员筛选拟录取学生信息
     * PUT /admin/deadline
     * @param deadlineDTO 截至日期修改信息
     * */
    @Override
    @Transactional
    public void adminModifyDeadline(AdminDeadlineDTO deadlineDTO) {
        Integer userId = User.getAuth().getId();
        try {
            Deadline query = Deadline.builder().id(deadlineDTO.getDeadlineID()).build();

            List<Deadline> deadlines = deadlineMapper.selectDeadline(
                    query, FieldsGenerator.generateFields(Deadline.class)
            );

            if (deadlines.isEmpty())
                throw new DeadlineNotFoundException();

            Deadline ddl = deadlines.getFirst();
            ddl.setName(deadlineDTO.getName());
            ddl.setTime(LocalDateTime.parse(String.format("%sT00:00", deadlineDTO.getDeadline())));

            deadlineMapper.updateDeadline(ddl, query);

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(deadlineDTO.getDeadlineID(), 1);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 管理员获取仍有剩余名额的导师
     * GET /admin/teachers/remnant
     * */
    @Override
    public List<TeacherProfileVO> getTeachersWithMetric() {
        try {
            return teacherMapper.selectTeacherWithMetric(
                    User.getAuth().getTeacher().getDepartment(),
                    1
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 管理员获取所有教师信息
     * GET /admin/teachers
     * */
    @Override
    public List<TeacherProfileVO> getAllTeachers() {
        try {
            return teacherMapper.selectTeacherWithMetric(
                    User.getAuth().getTeacher().getDepartment(),
                    0
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 管理员修改教师信息
     * GET /admin/teacher
     * */
    @Override
    public void adminModifyTeacher(AdminTeacherDTO teacherDTO) {
        try {
            List<Teacher> teachers = teacherMapper.selectTeacher(
                    Teacher.builder().userId(teacherDTO.getTeacherID()).build(),
                    FieldsGenerator.generateFields(Teacher.class)
            );

            if (teachers.isEmpty())
                throw new UserNotFoundException();

            Teacher targetTeacher = teachers.getFirst();

            targetTeacher.setTitle(teacherDTO.getTitle());
            targetTeacher.setIdentity(teacherDTO.getIdentity());
            targetTeacher.setTotal(teacherDTO.getTotal());

            teacherMapper.updateTeacher(
                    targetTeacher,
                    Teacher.builder().userId(teacherDTO.getTeacherID()).build()
            );

        } catch (UserNotFoundException userNotFoundException) {
            throw new UserNotFoundException(teacherDTO.getTeacherID(), 2);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 管理员获取所有无学生选择的教师
     * GET /admin/teachers/empty
     * */
    @Override
    public List<TeacherProfileVO> getTeachersWithoutEnrolls() {
        try {
            return teacherMapper.selectTeacherWithoutEnroll(
                    User.getAuth().getTeacher().getDepartment()
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 管理员按照比例筛选进入复试人员
     * POST /admin/filter-enroll
     * @param ratio 筛选比例
     * */
    @Override
    @Transactional
    public void adminFilterEnrolls(Double ratio) {
        DeadlineEnum type = DeadlineEnum.INITIAL_SUBMISSION;
        Integer dept = User.getAuth().getTeacher().getDepartment();

        try {
            Deadline targetDeadline = deadlineMapper.selectDeadline(
                    Deadline.builder().type(type.getValue()).build(),
                    FieldsGenerator.generateFields(Deadline.class)
            ).getFirst();

            if (LocalDateTime.now().isBefore(targetDeadline.getTime()))
                throw new DeadlineUnreachedException();

            Integer total = majorMapper.selectMajor(
                            Major.builder().pid(0).department(dept).build(),
                            FieldsGenerator.generateFields(Major.class)
                    )
                    .stream()
                    .mapToInt(m -> m.getTotal() + m.getAddition() - m.getRecommend())
                    .sum();

            total = (int) Math.ceil(ratio * total);

            List<Student> students = studentMapper
                    .selectStudent(
                            Student.builder().department(dept).valid(0).build(),
                            FieldsGenerator.generateFields(Student.class)
                    )
                    .stream()
                    .sorted(Comparator.comparingDouble(Student::getGradeFirst).reversed())
                    .toList();

            int startIndex = Math.min(students.size(), total);
            int endIndex = students.size();

            if (startIndex == endIndex)
                return;

            studentMapper.invalidateStudent(students.subList(startIndex, endIndex));

        } catch (DeadlineUnreachedException deadlineUnreachedException) {
            throw new DeadlineUnreachedException(type, 4031);
        }
    }

    /**
     * 管理员筛选拟录取学生信息
     * POST /admin/possible-enroll
     *
     * */
    @Override
    @Transactional
    public void adminFilterPossibleEnrolls() {
        DeadlineEnum type = DeadlineEnum.SECOND_SUBMISSION;

        try {
            Deadline ddl = deadlineMapper.selectDeadline(
                    Deadline.builder().type(type.getValue()).build(),
                    FieldsGenerator.generateFields(Deadline.class)
            ).getFirst();

            if (LocalDateTime.now().isBefore(ddl.getTime()))
                throw new DeadlineUnreachedException();

            List<Major> majors = majorMapper.selectMajor(
                    Major.builder().pid(0).department(User.getAuth().getTeacher().getDepartment()).disabled(1).build(),
                    FieldsGenerator.generateFields(Major.class)
            );

            for (Major major : majors) {
                List<Student> students = studentMapper
                        .selectStudent(
                                Student.builder().majorApply(major.getId()).valid(0).build(),
                                FieldsGenerator.generateFields(Student.class)
                        )
                        .stream()
                        .sorted(Comparator.comparingDouble(s -> -(s.getGradeFirst() + s.getGradeSecond())))
                        .toList();

                int startIndex = Math.min(
                        major.getTotal() + major.getAddition() - major.getRecommend(),
                        students.size()
                );
                int endIndex = students.size();

                if (startIndex == endIndex)
                    continue;

                studentMapper.invalidateStudent(students.subList(startIndex, endIndex));
            }

        } catch (DeadlineUnreachedException deadlineUnreachedException) {
            throw new DeadlineUnreachedException(type, 4031);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 管理员筛选不接受调剂/未被录入的学生
     * POST /admin/final-enroll
     *
     * */
    @Override
    @Transactional
    public void adminFilterFinalEnrolls() {
        try {
            List<Student> students = studentMapper.selectStudentWithoutEnroll(
                    User.getAuth().getTeacher().getDepartment()
            );

            if (!students.isEmpty())
                studentMapper.invalidateStudent(students);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
