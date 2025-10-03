package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.dto.*;
import __Y2_S1_MTR_02.model.*;
import __Y2_S1_MTR_02.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserAccountRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final VerificationTokenRepository verificationRepo;
    private final PasswordResetTokenRepository resetRepo;

    public UserService(UserAccountRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authManager,
                       JwtUtil jwtUtil,
                       EmailService emailService,
                       VerificationTokenRepository verificationRepo,
                       PasswordResetTokenRepository resetRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.verificationRepo = verificationRepo;
        this.resetRepo = resetRepo;
    }


    public void register(RegisterRequest req, String appUrl) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        UserAccount user = new UserAccount();
        user.setFullName(req.getFullname());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.PASSENGER);
        userRepo.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken v = new VerificationToken(token, user, Instant.now().plusSeconds(86400)); // 24h expiry
        verificationRepo.save(v);

        String link = appUrl + "/api/users/verify?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), link);
    }


    public void verifyEmail(String token) {
        VerificationToken v = verificationRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (v.getExpiryDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expired");
        }
        UserAccount user = v.getUser();
        user.setRole(UserRole.PASSENGER);
        userRepo.save(user);
        verificationRepo.delete(v);
    }


    public AuthResponse login(LoginRequest req) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new AuthResponse(false, null, "Invalid email or password");
        }

        UserAccount user = userRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getId());
        return new AuthResponse(true, user.getRole().name(), token);
    }


    public void logout(String token) {
        // If using blacklist, save token in DB.
        // Otherwise, just rely on frontend to discard token.
    }


    public void forgotPassword(String email, String appUrl) {
        Optional<UserAccount> opt = userRepo.findByEmail(email);
        if (opt.isEmpty()) return;

        UserAccount user = opt.get();
        String token = UUID.randomUUID().toString();
        PasswordResetToken pr = new PasswordResetToken(token, user, Instant.now().plusSeconds(3600)); // 1h expiry
        resetRepo.save(pr);

        String link = appUrl + "/api/users/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(user.getEmail(), link);
    }


    public void resetPassword(String token, String newPassword) {
        PasswordResetToken pr = resetRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        if (pr.getExpiryDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        UserAccount user = pr.getUser();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        resetRepo.delete(pr);
    }
}
