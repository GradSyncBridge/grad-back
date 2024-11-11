package backend.service.impl;

import backend.mapper.*;

import backend.model.DTO.ApplicationSubmitDTO;
import backend.model.DTO.StudentTableDTO;
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

    @Override
    public void submitTable(StudentTableDTO studentTableDTO) {
        Integer targetUid;
        DeadlineEnum type;
        Integer userRole = User.getAuth().getRole();

        type = userRole == 1 ? DeadlineEnum.INITIAL_SUBMISSION : DeadlineEnum.SECOND_SUBMISSION;

        studentTableDTO.setBirthday(LocalDateTime.parse(studentTableDTO.getBirth() + "T00:00:00"));
        Student student = studentConverter.INSTANCE.StudentTableDTOToStudent(
                studentTableDTO,
                studentTableDTO.getMajorStudy().toString(),
                studentTableDTO.getQuality().toString());

        try {
            List<Deadline> deadlines = deadlineMapper.selectDeadline(Deadline.builder().type(type.getValue()).build(),
                    FieldsGenerator.generateFields(Deadline.class));
            Deadline targetDeadline = deadlines.isEmpty() ? null : deadlines.getFirst();

            if (targetDeadline == null)
                throw new DeadlineNotFoundException();

            if (LocalDateTime.now().isAfter(targetDeadline.getTime()))
                throw new DeadlineExceedException();

            float totalGrade = 0;
            for (StudentGrade grade : studentTableDTO.getGrades()) {
                totalGrade += grade.getGrade();
                StudentGrade studentGrade = StudentGrade
                        .builder()
                        .userId(User.getAuth().getId()).sid(grade.getSid()).grade(grade.getGrade()).build();
                studentGradeMapper.insertStudentGrade(studentGrade);
            }

            if (userRole == 1) {
                student.setGradeFirst(totalGrade);
                targetUid = User.getAuth().getId();
            } else {
                student.setGradeSecond(totalGrade);
                targetUid = studentTableDTO.getTargetUid();
            }

            studentMapper.updateStudent(student, Student.builder().userId(targetUid).build());
        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, userRole == 1 ? 4031 : 4032);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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

    @Override
    public List<UserProfileVO> searchStudent(String key) {
        try {
            List<User> users;

            if (key.matches("\\d+"))
                users = userMapper.selectUser(User.builder().id(Integer.parseInt(key)).build(),
                        FieldsGenerator.generateFields(User.class));
            else if (key.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
                users = userMapper.selectUser(User.builder().email(key).build(),
                        FieldsGenerator.generateFields(User.class));
            else
                users = userMapper.selectUser(User.builder().name(key).build(),
                        FieldsGenerator.generateFields(User.class));

            return users
                    .stream()
                    .map(u -> userConverter.UserToUserProfileVO(u))
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            // e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
