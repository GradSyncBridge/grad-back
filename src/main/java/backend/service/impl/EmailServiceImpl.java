package backend.service.impl;

import backend.config.JwtService;
import backend.mapper.UserMapper;
import backend.model.entity.User;
import backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    ApplicationContext context;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.password}")
    private String password;

    @Value("classpath:templates/smtp_reset_password.html")
    private Resource resetPasswordTemplate;

    @Override
    public void sendValidationMessage(String to, String subject, String text) {

    }

    @Override
    public void sendVerificationEmail(String token) {

    }

    @Override
    public void sendResetPasswordEmail(String email) {
        String token = generateToken(email);
        if(token == null) throw new IllegalArgumentException("Email cannot be null");


    }

    public boolean verifyToken(String token) {
        Integer uid = jwtService.extractUid(token);
        List<User> userList = userMapper.selectUser(User.builder().id(uid).build(), Map.of("id", true));
        if(userList.isEmpty()) return false;
        return jwtService.validateTokenById(token, userList.getFirst());
    }

    public String generateToken(String email) {
        List<User> userList = userMapper.selectUser(User.builder().email(email).build(), Map.of("id", true));
        if(userList.isEmpty()) return null;
        return jwtService.generateToken(userList.getFirst().getId(), 1);
    }
}
