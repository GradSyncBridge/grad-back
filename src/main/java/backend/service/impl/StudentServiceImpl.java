package backend.service.impl;

import backend.mapper.StudentMapper;
import backend.model.DTO.StudentTableDTO;
import backend.model.converter.StudentConverter;
import backend.model.entity.Student;
import backend.model.entity.User;
import backend.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentConverter studentConverter;

    @Override
    public void submitTable(StudentTableDTO studentTableDTO) {
        Student student = studentConverter.INSTANCE.StudentTableDTOToStudent(studentTableDTO, studentTableDTO.getMajorStudy().toString());
        try {
            studentMapper.updateStudent(student, Student.builder().userId(User.getAuth().getId()).build());
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
