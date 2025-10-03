package __Y2_S1_MTR_02.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender) { this.mailSender = mailSender; }

    public void sendVerificationEmail(String to, String link) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Verify your email");
        msg.setText("Click the link to verify: " + link);
        mailSender.send(msg);
    }

    public void sendPasswordResetEmail(String to, String link) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Reset your password");
        msg.setText("Click the link to reset your password: " + link);
        mailSender.send(msg);
    }
}
