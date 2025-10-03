package __Y2_S1_MTR_02.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class PasswordResetToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne
    private User user;

    private Instant expiryDate;

    public PasswordResetToken() {}
    public PasswordResetToken(String token, User user, Instant expiry) {
        this.token = token; this.user = user; this.expiryDate = expiry;
    }
}
