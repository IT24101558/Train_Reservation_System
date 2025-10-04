package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.dto.AuthResponse;
import __Y2_S1_MTR_02.dto.LoginRequest;
import __Y2_S1_MTR_02.dto.UserProfileResponse;
import __Y2_S1_MTR_02.dto.RegisterRequest;
import __Y2_S1_MTR_02.model.UserAccount;
import __Y2_S1_MTR_02.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import __Y2_S1_MTR_02.repository.UserAccountRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserAccountRepository userAccountRepository;

    public AuthController(AuthService authService, UserAccountRepository userAccountRepository) {
        this.authService = authService;
        this.userAccountRepository = userAccountRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            UserAccount created = authService.registerPassenger(request);
            return ResponseEntity.ok(new AuthResponse(true, created.getRole().name(), "Registered"));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new AuthResponse(false, null, ex.getMessage()));
        }
    }


    @PostMapping("/register-return-profile")
    public ResponseEntity<?> registerAndReturnProfile(@RequestBody RegisterRequest request) {
        try {
            UserAccount created = authService.registerPassenger(request);
            return ResponseEntity.ok(new UserProfileResponse(
                    created.getId(), created.getFullName(), created.getEmail(), created.getPhone()
            ));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(new AuthResponse(false, null, ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return authService.authenticate(request)
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(new AuthResponse(true, user.getRole().name(), "OK")))
                .orElseGet(() -> ResponseEntity.status(401).body(new AuthResponse(false, null, "Invalid credentials")));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(@RequestParam("email") String email) {
        return userAccountRepository.findByEmail(email)
                .<ResponseEntity<?>>map(u -> ResponseEntity.ok(new UserProfileResponse(
                        u.getId(), u.getFullName(), u.getEmail(), u.getPhone()
                )))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}


