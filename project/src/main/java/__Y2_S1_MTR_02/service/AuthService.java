package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.dto.LoginRequest;
import __Y2_S1_MTR_02.dto.RegisterRequest;
import __Y2_S1_MTR_02.model.UserAccount;
import __Y2_S1_MTR_02.model.UserRole;
import __Y2_S1_MTR_02.repository.UserAccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public UserAccount registerPassenger(RegisterRequest request) {
        if (userAccountRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }
        UserAccount account = new UserAccount();
        account.setFullName(request.getFullname());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        account.setRole(UserRole.PASSENGER);
        return userAccountRepository.save(account);
    }

    public Optional<UserAccount> authenticate(LoginRequest request) {
        // Admin backdoor as requested
        if ("admin@gmail.com".equalsIgnoreCase(request.getEmail()) &&
                "Admin@123".equals(request.getPassword())) {
            UserAccount admin = new UserAccount();
            admin.setId(-1L);
            admin.setFullName("Administrator");
            admin.setEmail("admin@gmail.com");
            admin.setPhone("N/A");
            admin.setRole(UserRole.ADMIN);
            admin.setPasswordHash("N/A");
            return Optional.of(admin);
        }

        return userAccountRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPasswordHash()));
    }
}


