package backend.service.impl;

import backend.mapper.UserMapper;
import backend.model.entity.StudentGrades;
import backend.model.entity.User;
import backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public String updateGrade() {
        User user = User.builder().id(1).build();
        StudentGrades studentGrades = new StudentGrades();
        studentGrades.getFirst().setFirst(408, 100);
        studentGrades.getSecond().setSecond(401, null);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            user.setGrade(objectMapper.writeValueAsString(studentGrades));
            System.out.println("hello world: " + user.getGrade());
        }catch (Exception e) {
            e.printStackTrace();
        }

        StudentGrades studentGradesConvert = null;
        try {
            studentGradesConvert = objectMapper.readValue(user.getGrade(), StudentGrades.class);
            System.out.println(studentGradesConvert);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(studentGradesConvert);
        return null;
    }
}
