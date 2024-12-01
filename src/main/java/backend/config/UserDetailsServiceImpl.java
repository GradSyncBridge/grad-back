package backend.config;

import backend.mapper.StudentMapper;
import backend.mapper.TeacherMapper;
import backend.model.entity.Student;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import backend.mapper.UserMapper;
import backend.util.FieldsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 用户详情服务实现
 *
 * @field userMapper: 用户mapper
 * @function loadUserByUsername: 获取当前用户
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<String> fields = List.of("id", "username", "password");

        Map<String, Boolean> scope = FieldsGenerator.generateFields(User.class, fields);

        User user;
        try {
            user = userMapper.selectUser(User.builder().username(username).build(), scope).getFirst();
        } catch (Exception e) {
            throw new UsernameNotFoundException("user not found");
        }

        return user;
    }

    public User loadUserById(Integer id) throws UsernameNotFoundException {
        User user;

        try {
            user = userMapper.selectUser(
                    User.builder().id(id).build(),
                    FieldsGenerator.generateFields(User.class)
            ).getFirst();

            Integer role = user.getRole();

            if (role == 1) {
                List<Student> students = studentMapper.selectStudent(
                        Student.builder().userId(id).build(),
                        FieldsGenerator.generateFields(Student.class)
                );

                user.setStudent(students.isEmpty() ? null : students.getFirst());

            } else if (role == 2) {
                List<Teacher> teachers = teacherMapper.selectTeacher(
                        Teacher.builder().userId(id).build(),
                        FieldsGenerator.generateFields(Teacher.class)
                );

                user.setTeacher(teachers.isEmpty() ? null : teachers.getFirst());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new UsernameNotFoundException("user not found");
        }

        return user;
    }

}
