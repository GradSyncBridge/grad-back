package backend.service.impl;

import backend.config.GlobalConfig;
import backend.mapper.*;

import backend.model.DTO.StudentApplicationSubmitDTO;
import backend.model.DTO.StudentGradeModifyDTO;
import backend.model.DTO.StudentGradeSubmitDTO;
import backend.model.DTO.StudentSubmitDTO;

import backend.model.VO.student.*;
import backend.model.VO.teacher.TeacherVO;
import backend.model.VO.user.UserProfileVO;

import backend.model.converter.StudentConverter;
import backend.model.converter.UserConverter;

import backend.model.entity.*;

import backend.service.StudentService;

import backend.enums.DeadlineEnum;
import backend.exception.model.deadline.DeadlineExceedException;
import backend.exception.model.deadline.DeadlineNotFoundException;

import backend.util.FieldsGenerator;
import backend.util.StringToList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentConverter studentConverter;

    @Autowired
    private StudentGradeMapper studentGradeMapper;

    @Autowired
    private QualityFileMapper qualityFileMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private StudentApplyMapper studentApplyMapper;

    @Autowired
    private DeadlineMapper deadlineMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConverter userConverter;

    private void verifyDeadline(DeadlineEnum type) {
        List<Deadline> deadlines = deadlineMapper.selectDeadline(Deadline.builder().type(type.getValue()).build(),
                FieldsGenerator.generateFields(Deadline.class));
        Deadline targetDeadline = deadlines.isEmpty() ? null : deadlines.getFirst();

        if (targetDeadline == null)
            throw new DeadlineNotFoundException();

        if (LocalDateTime.now().isAfter(targetDeadline.getTime()))
            throw new DeadlineExceedException();

    }

    @Override
    public StudentSubmitTableVO getStudentSubmitTable(Integer targetUid) {
        User user = User.getAuth();
        Student student;

        if (user.getRole() == 1)
            student = User.getAuth().getStudent();
        else {
            List<Student> students = studentMapper.selectStudent(Student.builder().userId(targetUid).build(),
                    FieldsGenerator.generateFields(Student.class));
            student = students.isEmpty() ? null : students.getFirst();
        }

        if (student == null)
            return null;

        try {
            List<Quality> fileList = StringToList.convert(student.getQuality())
                    .stream()
                    .map(f -> {
                        List<QualityFile> files = qualityFileMapper.selectQualityFile(
                                QualityFile.builder().id(f).build(), FieldsGenerator.generateFields(QualityFile.class));
                        return files.isEmpty() ? null : files.getFirst();
                    })
                    .filter(Objects::nonNull)
                    .map(f -> studentConverter.INSTANCE.qualityFileToQuality(f))
                    .toList();

            // Major apply
            List<Major> majorList = majorMapper.selectMajor(Major.builder().id(student.getMajorApply()).build(),
                    FieldsGenerator.generateFields(Major.class));
            MajorSubject majorApply = majorList.isEmpty() ? null
                    : studentConverter.INSTANCE.majorToMajorSubject(majorList.getFirst());

            // Major study
            List<Integer> majorStudy = StringToList.convert(student.getMajorStudy());
            List<MajorSubject> majorStudyList = majorStudy
                    .stream()
                    .map(i -> {
                        List<Major> majors = majorMapper.selectMajor(Major.builder().id(i).build(),
                                FieldsGenerator.generateFields(Major.class));
                        return (majors.isEmpty() ? null
                                : studentConverter.INSTANCE.majorToMajorSubject(majors.getFirst()));
                    })
                    .filter(Objects::nonNull)
                    .toList();

            // application
            List<TeacherVO> applications = studentApplyMapper
                    .selectApplicationWithTeacher(StudentApply.builder().userId(student.getUserId()).build());

            // Grades
            List<GradeList> gradeListFirst = studentGradeMapper
                    .selectGradeWithSubject(StudentGrade.builder().userId(student.getUserId()).disabled(1).build(), 0);
            List<GradeList> gradeListSecond = studentGradeMapper
                    .selectGradeWithSubject(StudentGrade.builder().userId(student.getUserId()).disabled(1).build(), 1);
            Score gradeFirst = Score.builder().gradeTotal(student.getGradeFirst()).gradeList(gradeListFirst).build();
            Score gradeSecond = Score.builder().gradeTotal(student.getGradeSecond()).gradeList(gradeListSecond).build();

            return studentConverter.INSTANCE.StudentToSubmitTable(student, gradeFirst, gradeSecond, applications, fileList, majorApply, majorStudyList);

        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<UserProfileVO> searchStudent(String key, Integer valid) {
        try {
            User query = User.builder().build();

            if (key != null && !key.isEmpty()) {
                if (key.matches(GlobalConfig.uidPattern))
                    query.setId(Integer.parseInt(key));

                else if (key.matches(GlobalConfig.emailPattern))
                    query.setEmail(key);
                else
                    query.setName(key);
            }

            return userMapper
                    .searchStudent(query, valid)
                    .stream()
                    .map(u -> userConverter.UserToUserProfileVO(u))
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
//             e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void studentSubmit(StudentSubmitDTO submitDTO) {
        DeadlineEnum type = DeadlineEnum.INITIAL_SUBMISSION;

        try {
            verifyDeadline(type);

            // Accommodate for LocalDateTime
            submitDTO.setBirth(String.format("%sT00:00:00", submitDTO.getBirth()));
            studentMapper.updateStudent(
                    studentConverter.INSTANCE.StudentSubmitDTOToStudent(
                            submitDTO,
                            submitDTO.getQuality().toString()
                    ),
                    Student.builder().userId(User.getAuth().getId()).build()
            );

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, 403);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void studentGradeSubmit(StudentGradeSubmitDTO studentGrade) {
        Integer role = User.getAuth().getRole();

        DeadlineEnum type = role == 1 ? DeadlineEnum.INITIAL_SUBMISSION : DeadlineEnum.SECOND_SUBMISSION;
        Integer studentID = role == 1 ? User.getAuth().getId() : studentGrade.getStudentID();

        Student student = role == 1 ?
                User.getAuth().getStudent() :
                studentMapper.selectStudent(
                        Student.builder().userId(studentID).build(),
                        FieldsGenerator.generateFields(Student.class)
                ).getFirst();

        try {
            verifyDeadline(type);

            studentGrade
                    .getGrades()
                    .forEach(g ->
                            studentGradeMapper.insertStudentGrade(
                                    StudentGrade
                                            .builder().grade(g.getGrade()).sid(g.getSubjectID())
                                            .disabled(1).userId(studentID).build()
                            )
                    );

            Double totalGrade = studentGrade
                    .getGrades()
                    .stream()
                    .mapToDouble(StudentGradeSubmitDTO.GradeSingleDTO::getGrade)
                    .sum();
            if (role == 1)
                student.setGradeFirst(totalGrade);
            else
                student.setGradeSecond(totalGrade);
            studentMapper.updateStudent(student, Student.builder().userId(studentID).build());

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, role == 1 ? 4031 : 4032);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void studentGradeModify(StudentGradeModifyDTO studentGrade) {
        Integer role = User.getAuth().getRole();

        DeadlineEnum type = role == 1 ? DeadlineEnum.INITIAL_SUBMISSION : DeadlineEnum.SECOND_SUBMISSION;
        Integer studentID = role == 1 ? User.getAuth().getId() : studentGrade.getStudentID();

        Student student = role == 1 ?
                User.getAuth().getStudent() :
                studentMapper.selectStudent(
                        Student.builder().userId(studentID).build(),
                        FieldsGenerator.generateFields(Student.class)
                ).getFirst();

        try {
            verifyDeadline(type);

            studentGrade
                    .getGrades()
                    .forEach(g -> studentGradeMapper.updateStudentGrade(
                                    StudentGrade.builder().grade(g.getGrade()).build(),
                                    StudentGrade.builder().id(g.getGradeID()).build()
                            )
                    );

            Double totalGrade = studentGrade
                    .getGrades()
                    .stream()
                    .mapToDouble(StudentGradeModifyDTO.GradeSingleDTO::getGrade)
                    .sum();
            if (role == 1)
                student.setGradeFirst(totalGrade);
            else
                student.setGradeSecond(totalGrade);
            studentMapper.updateStudent(student, Student.builder().userId(studentID).build());

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, role == 1 ? 4031 : 4032);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void studentApplicationSubmit(StudentApplicationSubmitDTO studentApplication) {
        DeadlineEnum type = DeadlineEnum.SECOND_SUBMISSION;

        Student student = User.getAuth().getStudent();
        Integer uid = student.getUserId();

        try {
            verifyDeadline(type);

            if (!studentApplyMapper.selectStudentApply(
                    StudentApply.builder().userId(uid).disabled(1).build(),
                    Map.of("id", true)).isEmpty()
            )
                return;

            List<Integer> applications = studentApplication.getApplication();
            for (int i = 0; i < applications.size(); i++)
                studentApplyMapper.insertStudentApply(
                        StudentApply.builder().tid(applications.get(i)).level(i+1).userId(uid).disabled(1).build()
                );

            student.setMajorStudy(studentApplication.getApplication().toString());
            student.setReassign(studentApplication.getReassign());

            studentMapper.updateStudent(student, Student.builder().userId(uid).build());

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, 403);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


}
