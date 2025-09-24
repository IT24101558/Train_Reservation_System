package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.dto.LoginRequest;
import __Y2_S1_MTR_02.dto.RegisterRequest;
import __Y2_S1_MTR_02.model.UserAccount;
import __Y2_S1_MTR_02.model.UserRole;
import __Y2_S1_MTR_02.repository.UserAccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
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
        return userAccountRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPasswordHash()));
    }
}


