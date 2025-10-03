package __Y2_S1_MTR_02.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    private UserAccount user;

    private Instant expiryDate;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, UserAccount user, Instant expiryDate) {
        this.token = token;
        this.user = user;
        this.expiryDate = expiryDate;
    }
}
