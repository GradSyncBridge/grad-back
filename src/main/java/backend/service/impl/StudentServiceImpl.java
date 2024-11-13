package backend.service.impl;

import backend.config.GlobalConfig;
import backend.mapper.*;

import backend.model.DTO.ApplicationSubmitDTO;
import backend.model.DTO.StudentSubmitDTO;
import backend.model.VO.student.*;
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

import java.util.Comparator;
import java.util.List;
import java.time.LocalDateTime;
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

            // Major application
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
            List<Application> applications = studentApplyMapper
                    .selectApplicationWithTeacher(StudentApply.builder().userId(user.getId()).build());

            // Grades
            List<GradeList> gradeListFirst = studentGradeMapper
                    .selectGradeWithSubject(StudentGrade.builder().userId(user.getId()).build(), 0);
            List<GradeList> gradeListSecond = studentGradeMapper
                    .selectGradeWithSubject(StudentGrade.builder().userId(user.getId()).build(), 1);
            Score gradeFirst = Score.builder().gradeTotal(student.getGradeFirst()).gradeList(gradeListFirst).build();
            Score gradeSecond = Score.builder().gradeTotal(student.getGradeSecond()).gradeList(gradeListSecond).build();

            return studentConverter.INSTANCE.StudentToSubmitTable(student, gradeFirst, gradeSecond, applications,
                    fileList, majorApply, majorStudyList);

        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void submitApplication(ApplicationSubmitDTO applicationSubmit) {
        DeadlineEnum type = DeadlineEnum.SECOND_SUBMISSION;

        try {
            List<Deadline> deadlines = deadlineMapper.selectDeadline(Deadline.builder().type(type.getValue()).build(),
                    FieldsGenerator.generateFields(Deadline.class));
            Deadline targetDeadline = deadlines.isEmpty() ? null : deadlines.getFirst();

            if (targetDeadline == null)
                throw new DeadlineNotFoundException();

            if (LocalDateTime.now().isAfter(targetDeadline.getTime()))
                throw new DeadlineExceedException();

            List<Integer> applications = applicationSubmit.getApplication();

            for (int i = 0; i < applications.size(); i++)
                studentApplyMapper.insertStudentApply(StudentApply.builder()
                        .userId(User.getAuth().getId())
                        .tid(applications.get(i))
                        .level(i + 1)
                        .disabled(1).build());
        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, 403);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void modifyApplication(ApplicationSubmitDTO applicationSubmit) {
        DeadlineEnum type = DeadlineEnum.SECOND_SUBMISSION;

        try {
            List<Deadline> deadlines = deadlineMapper.selectDeadline(Deadline.builder().type(type.getValue()).build(),
                    FieldsGenerator.generateFields(Deadline.class));
            Deadline targetDeadline = deadlines.isEmpty() ? null : deadlines.getFirst();

            if (targetDeadline == null)
                throw new DeadlineNotFoundException();

            if (LocalDateTime.now().isAfter(targetDeadline.getTime()))
                throw new DeadlineExceedException();

            List<StudentApply> applications = studentApplyMapper
                    .selectStudentApply(StudentApply.builder().userId(User.getAuth().getId()).disabled(1).build(),
                            FieldsGenerator.generateFields(StudentApply.class))
                    .stream()
                    .sorted(Comparator.comparingInt(StudentApply::getLevel))
                    .toList();

            for (int i = 0; i != applicationSubmit.getApplication().size(); i++) {
                studentApplyMapper.updateStudentApply(
                        StudentApply.builder().userId(User.getAuth().getId())
                                .tid(applicationSubmit.getApplication().get(i)).level(i + 1).build(),
                        applications.get(i));
            }

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, 403);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    // Newer interfaces
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
             e.printStackTrace();
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
}
