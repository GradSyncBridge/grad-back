package backend.service.impl;

import backend.mapper.*;
import backend.model.DTO.ApplicationSubmitDTO;
import backend.model.DTO.StudentTableDTO;
import backend.model.VO.student.*;
import backend.model.converter.StudentConverter;
import backend.model.entity.*;
import backend.service.StudentService;
import backend.util.FieldsGenerator;
import backend.util.StringToList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


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

    @Override
    public void submitTable(StudentTableDTO studentTableDTO) {
        studentTableDTO.setBirthday(LocalDateTime.parse(studentTableDTO.getBirth() + "T00:00:00"));
        Student student = studentConverter.INSTANCE.StudentTableDTOToStudent(studentTableDTO, studentTableDTO.getMajorStudy().toString());

        try {

            float totalGrade = 0;
            for (StudentGrade grade : studentTableDTO.getGrades()) {
                totalGrade += grade.getGrade();
                StudentGrade studentGrade = StudentGrade
                        .builder()
                        .userId(User.getAuth().getId()).sid(grade.getSid()).grade(grade.getGrade()).build();
                studentGradeMapper.insertStudentGrade(studentGrade);
            }

            if (User.getAuth().getRole() == 1)
                student.setGradeFirst(totalGrade);
            else
                student.setGradeSecond(totalGrade);

            studentMapper.updateStudent(student, Student.builder().userId(User.getAuth().getId()).build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public StudentSubmitTableVO getStudentSubmitTable() {
        User user = User.getAuth();
        Student student = User.getAuth().getStudent();

        try {
            List<Quality> fileList = StringToList.convert(student.getQuality())
                    .stream()
                    .map(f -> {
                        List<QualityFile> files = qualityFileMapper.selectQualityFile(QualityFile.builder().id(f).build(), FieldsGenerator.generateFields(QualityFile.class));
                        return files.isEmpty() ? null : files.getFirst();
                    })
                    .filter(Objects::nonNull)
                    .map(f -> studentConverter.INSTANCE.qualityFileToQuality(f))
                    .toList();

            // Major application
            List<Major> majorList = majorMapper.selectMajor(Major.builder().id(student.getMajorApply()).build(), FieldsGenerator.generateFields(Major.class));
            MajorSubject majorApply = majorList.isEmpty() ? null : studentConverter.INSTANCE.majorToMajorSubject(majorList.getFirst());

            // Major study
            List<Integer> majorStudy = StringToList.convert(student.getMajorStudy());
            List<MajorSubject> majorStudyList = majorStudy
                    .stream()
                    .map(i -> {
                        List<Major> majors = majorMapper.selectMajor(Major.builder().id(i).build(), FieldsGenerator.generateFields(Major.class));
                        return (majors.isEmpty() ? null : studentConverter.INSTANCE.majorToMajorSubject(majors.getFirst()));
                    })
                    .filter(Objects::nonNull)
                    .toList();

            // application
            List<Application> applications = studentApplyMapper.selectApplicationWithTeacher(StudentApply.builder().userId(user.getId()).build());

            // Grades
            List<GradeList> gradeListFirst = studentGradeMapper.selectGradeWithSubject(StudentGrade.builder().userId(user.getId()).build(), 0);
            List<GradeList> gradeListSecond = studentGradeMapper.selectGradeWithSubject(StudentGrade.builder().userId(user.getId()).build(), 1);
            Score gradeFirst = Score.builder().gradeTotal(student.getGradeFirst()).gradeList(gradeListFirst).build();
            Score gradeSecond = Score.builder().gradeTotal(student.getGradeSecond()).gradeList(gradeListSecond).build();

            return studentConverter.INSTANCE.StudentToSubmitTable(student, gradeFirst, gradeSecond, applications, fileList, majorApply, majorStudyList);

        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void submitApplication(ApplicationSubmitDTO applicationSubmit) {
        try {
            List<Integer> applications = applicationSubmit.getApplication();

            for (int i = 0; i < applications.size(); i++)
                studentApplyMapper.
                        insertStudentApply(StudentApply.
                                builder()
                                .userId(User.getAuth().getId())
                                .tid(applications.get(i))
                                .level(i+1)
                                .disabled(1).build()
                        );
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }



    }
}
