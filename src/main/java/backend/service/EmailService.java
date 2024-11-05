package backend.service;

public interface EmailService {
    void sendValidationMessage(String to, String subject, String text);

    void sendVerificationEmail(String token);

    void sendResetPasswordEmail(String email);
}
