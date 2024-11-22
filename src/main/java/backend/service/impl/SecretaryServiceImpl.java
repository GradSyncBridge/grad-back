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
import backend.util.GlobalLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SecretaryServiceImpl implements SecretaryService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 研究生管理秘书审核学生报名表单
     * POST /secretary/examine
     * @param examineDTO 学生报名表单审核信息
     */
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
            throw new RuntimeException(e.getMessage());
        }finally {
            GlobalLogger.builder().created(LocalDateTime.now()).userId(user.getId())
                    .endpoint("POST /secretary/examine").operation(examineDTO.toString()).build().getThis();
        }

    }

    /**
     * 研究生管理秘书修改初试成绩
     * PUT /secretary/grade
     * @param gradeDTO 学生成绩信息
     */
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
            throw new RuntimeException(e.getMessage());
        } finally {
            GlobalLogger.builder().userId(user.getId()).created(LocalDateTime.now())
                    .operation(gradeDTO.toString()).endpoint("PUT /secretary/grade").build().getThis();
        }
    }
}
