package __Y2_S1_MTR_02.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired(required = false)
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(String to, String subject, String text) {
        if (mailSender == null) {
            System.out.println("=== EMAIL NOTIFICATION ===");
            System.out.println("To: " + to);
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + text);
            System.out.println("=========================");
            return;
        }
        
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
