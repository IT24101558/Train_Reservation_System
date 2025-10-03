package __Y2_S1_MTR_02.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendVerificationEmail(String to, String token) {
        String link = "http://localhost:8080/api/auth/verify?token=" + token;
        sendEmail(to, "Verify your account", "Click the link to verify: " + link);
    }

    public void sendPasswordResetEmail(String to, String token) {
        String link = "http://localhost:8080/api/auth/reset-password?token=" + token;
        sendEmail(to, "Password Reset", "Click the link to reset: " + link);
    }
}
