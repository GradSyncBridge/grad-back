package backend.model.entity;

import backend.annotation.entity.UserValidation;
import backend.annotation.entity.group.userGroup.EmailGroup;
import backend.annotation.entity.group.userGroup.UsernameGroup;
import backend.mapper.StudentMapper;
import backend.mapper.TeacherMapper;
import backend.mapper.UserMapper;
import backend.util.FieldsGenerator;
import lombok.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 用户表
 *
 * @field id user id int
 * @field username 用户昵称 unique int
 * @field name 用户名 string
 * @field password 用户密码 string
 * @field email 用户邮箱 string
 * @field avatar 用户头像 string
 * @field role 用户角色 int (1 -- student, 2-- teacher)
 * @field phone 用户电话 string
 * @field gender 用户性别 string
 * @field disabled 用户是否被禁用 int (1 -- active, 0 -- disabled)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@UserValidation(groups = EmailGroup.class)
@UserValidation(groups = UsernameGroup.class)
public class User implements UserDetails, ApplicationContextAware {

    private Integer id;

    private String username;

    private String name;

    private String password;

    private String email;

    private String avatar;

    // 1 -- student, 2-- teacher
    private Integer role;

    private String phone;

    private Integer gender;

    // 1 -- active, 0 -- disabled
    private Integer disabled;

    private Student student;

    private Teacher teacher;

    private static ApplicationContext context;

    public static User getAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserMapper userMapper = context.getBean(UserMapper.class);
        User user = (User) authentication.getPrincipal();

        Integer id = user.getId();
        Integer role = user.getRole();

        User newUser = userMapper.selectUser(
                User.builder().id(id).build(),
                FieldsGenerator.generateFields(User.class)
        ).getFirst();

        if (role == 1) {
            StudentMapper studentMapper = context.getBean(StudentMapper.class);
            List<Student> students = studentMapper.selectStudent(
                    Student.builder().userId(id).build(),
                    FieldsGenerator.generateFields(Student.class)
            );
            newUser.setStudent(students.isEmpty() ? null : students.getFirst());
        } else if (role == 2) {
            TeacherMapper teacherMapper = context.getBean(TeacherMapper.class);
            List<Teacher> teachers = teacherMapper.selectTeacher(
                    Teacher.builder().userId(id).build(),
                    FieldsGenerator.generateFields(Teacher.class)
            );
            newUser.setTeacher(teachers.isEmpty() ? null : teachers.getFirst());
        }

        return newUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

}
