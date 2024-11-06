package backend.service.impl;

import backend.mapper.StudentGradeMapper;
import backend.mapper.StudentMapper;
import backend.model.DTO.StudentTableDTO;
import backend.model.converter.StudentConverter;
import backend.model.entity.Student;
import backend.model.entity.StudentGrade;
import backend.model.entity.User;
import backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentConverter studentConverter;

    @Autowired
    private StudentGradeMapper studentGradeMapper;

    @Override
    public void submitTable(StudentTableDTO studentTableDTO) {
        studentTableDTO.setBirthday(LocalDateTime.parse(studentTableDTO.getBirth() + "T00:00:00"));
        Student student = studentConverter.INSTANCE.StudentTableDTOToStudent(studentTableDTO, studentTableDTO.getMajorStudy().toString());
        System.out.println(User.getAuth().getStudent());
        try {

            float totalGrade = 0;
            for(StudentGrade grade : studentTableDTO.getGrades()){
                totalGrade += grade.getGrade();
                StudentGrade studentGrade =
                        StudentGrade.builder().userId(User.getAuth().getId()).sid(grade.getSid()).grade(grade.getGrade()).build();
                studentGradeMapper.insertStudentGrade(studentGrade);
            }

            if(User.getAuth().getRole() == 1) student.setGradePrimary(totalGrade);
            else student.setGradeSecond(totalGrade);

            studentMapper.updateStudent(student, Student.builder().userId(User.getAuth().getId()).build());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
