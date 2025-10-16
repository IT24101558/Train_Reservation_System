package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.dto.*;
import __Y2_S1_MTR_02.model.*;
import __Y2_S1_MTR_02.repository.UserAccountRepository;
import __Y2_S1_MTR_02.repository.PasswordResetTokenRepository;
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
    private final PasswordResetTokenRepository resetRepo;
    private final EmailService emailService; // still needed for password reset

    public UserService(UserAccountRepository userRepo,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authManager,
                       JwtUtil jwtUtil,
                       EmailService emailService,
                       PasswordResetTokenRepository resetRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.resetRepo = resetRepo;
    }


    public void register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        UserAccount user = new UserAccount();
        user.setFullName(req.getFullname());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(UserRole.PASSENGER);

        user.setEnabled(true);

        userRepo.save(user);
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
        // Optional: if using a token blacklist, implement here
        // Otherwise, frontend discards the token
    }


    public void forgotPassword(String email) {
        Optional<UserAccount> opt = userRepo.findByEmail(email);
        if (opt.isEmpty()) return;

        UserAccount user = opt.get();
        String token = UUID.randomUUID().toString();

        PasswordResetToken pr = new PasswordResetToken(
                token,
                user,
                Instant.now().plusSeconds(3600) // 1 hour expiry
        );
        resetRepo.save(pr);


        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }


    public void resetPassword(String token, String newPassword) {
        PasswordResetToken pr = resetRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (pr.getExpiryDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expired");
        }

        UserAccount user = pr.getUserAccount();
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        resetRepo.delete(pr);
    }
}
