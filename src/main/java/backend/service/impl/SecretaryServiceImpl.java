package backend.service.impl;

import backend.exception.model.user.UserNotFoundException;
import backend.exception.model.user.UserRoleDeniedException;

import backend.mapper.StudentMapper;
import backend.model.DTO.SecretaryExamineDTO;
import backend.model.DTO.SecretaryGradeDTO;
import backend.model.entity.Student;
import backend.model.entity.User;

import backend.service.SecretaryService;

import backend.util.FieldsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecretaryServiceImpl implements SecretaryService {

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void examineStudentSubmission(SecretaryExamineDTO examineDTO) {
        User user = User.getAuth();
        Integer role = user.getRole();

        try {
            if (role == 1)
                throw new UserRoleDeniedException();

            List<Student> students = studentMapper.selectStudent(
                    Student.builder().userId(examineDTO.getStudentID()).build(),
                    FieldsGenerator.generateFields(Student.class)
            );

            if (students.isEmpty())
                throw new UserNotFoundException();

            Student student = students.getFirst();
            student.setValid(examineDTO.getValid());

            studentMapper.updateStudent(
                    student,
                    Student.builder().userId(examineDTO.getStudentID()).build()
            );

        } catch (UserRoleDeniedException userRoleDeniedException) {
            throw new UserRoleDeniedException(role, 403);
        } catch (UserNotFoundException userNotFoundException) {
            throw new UserNotFoundException(examineDTO.getStudentID(), 1);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public void modifyStudentGrade(SecretaryGradeDTO gradeDTO) {
        User user = User.getAuth();
        Integer role = user.getRole();

        try {
            if (role == 1)
                throw new UserRoleDeniedException();

            List<Student> students = studentMapper.selectStudent(
                    Student.builder().userId(gradeDTO.getStudentID()).build(),
                    FieldsGenerator.generateFields(Student.class)
            );

            if (students.isEmpty())
                throw new UserNotFoundException();

            Student student = students.getFirst();
            student.setGradeFirst(student.getGradeFirst() + gradeDTO.getAddition());
            studentMapper.updateStudent(
                    student,
                    Student.builder().userId(gradeDTO.getStudentID()).build()
            );

        } catch (UserRoleDeniedException userRoleDeniedException) {
            throw new UserRoleDeniedException(role, 403);
        } catch (UserNotFoundException userNotFoundException) {
            throw new UserNotFoundException(gradeDTO.getStudentID(), 1);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}