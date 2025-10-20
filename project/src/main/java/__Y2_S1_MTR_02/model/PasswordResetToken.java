package __Y2_S1_MTR_02.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.Instant;

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Reset token cannot be empty")
    @Column(nullable = false, unique = true)
    private String token;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    @Column(nullable = false)
    private Instant expiryDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    @NotNull(message = "Associated user cannot be null")
    private UserAccount userAccount;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, UserAccount userAccount, Instant expiryDate) {
        this.token = token;
        this.userAccount = userAccount;
        this.expiryDate = expiryDate;
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
