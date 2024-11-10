package backend.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import backend.config.JwtService;
import backend.exception.model.user.UserNotFoundException;
import backend.mapper.UserMapper;
import backend.model.entity.User;
import backend.service.EmailService;
import jakarta.mail.internet.MimeMessage;

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

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.password}")
    private String password;

    @Value("classpath:templates/smtp_reset_password.html")
    private Resource resetPasswordTemplate;

    @Value("classpath:templates/smtp_reset_password_redirect.html")
    private Resource resetPasswordRedirectTemplate;

    @Value("${spring.mail.baseURL}")
    private Resource baseURL;

    @Value("${spring.mail.loginURL}")
    private String loginURL;

    private User sendUser;

    @Override
    public void sendValidationMessage(String to, String subject, String text) {

    }

    @Override
    public void sendVerificationEmail(String token) {

//        System.out.println(sendUser);
    }

    @Override
    public RedirectView redirectToLoginPage(String token) {
        if(!verifyToken(token)) throw new RuntimeException("Token is invalid");
        userMapper.updateUser(User.builder().password(passwordEncoder.encode(sendUser.getEmail())).build(),
                User.builder().id(sendUser.getId()).build());

        RedirectView redirectView = new RedirectView(resetPasswordRedirectTemplate.getFilename());
        redirectView.setStatusCode(HttpStatus.FOUND);  // 302 状态码，表示重定向
        return redirectView;
    }



    @Override
    public void sendResetPasswordEmail(String email) {
        String token = generateToken(email);
        if(token == null) throw new IllegalArgumentException("Email cannot be null");

        try {
            // Load HTML content
            String htmlContent = new String(Files.readAllBytes(resetPasswordTemplate.getFile().toPath()),
                    StandardCharsets.UTF_8);
            token = URLEncoder.encode(token, StandardCharsets.UTF_8.toString());

            // Replace placeholder
            String password = UUID.randomUUID().toString();
            htmlContent = htmlContent.replace("resetLink", baseURL + token);

            // Send email
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("重置密码");
            helper.setText(htmlContent, true);
            helper.setFrom(from);

            javaMailSender.send(message);
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public boolean verifyToken(String token) {
        Integer uid = jwtService.extractUid(token);
        List<User> userList = userMapper.selectUser(User.builder().id(uid).build(), Map.of("id", true, "email", true));

        if(userList.isEmpty()) throw new UserNotFoundException();
        sendUser = userList.getFirst();

        return jwtService.validateTokenById(token, userList.getFirst());
    }

    public String generateToken(String email) {
        List<User> userList = userMapper.selectUser(User.builder().email(email).build(), Map.of("id", true));

        if(userList.isEmpty()) throw new UserNotFoundException();

        return jwtService.generateToken(userList.getFirst().getId(), userList.getFirst().getRole(), 1);
    }
}
