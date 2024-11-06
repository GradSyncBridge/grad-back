package backend.service;

import org.springframework.web.servlet.view.RedirectView;

public interface EmailService {
    void sendValidationMessage(String to, String subject, String text);

    void sendVerificationEmail(String token);

    void sendResetPasswordEmail(String email);

    RedirectView redirectToLoginPage(String token);
}
