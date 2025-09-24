package __Y2_S1_MTR_02.config;

import __Y2_S1_MTR_02.model.UserAccount;
import __Y2_S1_MTR_02.model.UserRole;
import __Y2_S1_MTR_02.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements ApplicationRunner {

	private final UserAccountRepository userAccountRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${app.admin.email}")
	private String adminEmail;

	@Value("${app.admin.password}")
	private String adminPassword;

	@Value("${app.admin.fullname:Administrator}")
	private String adminFullname;

	@Value("${app.admin.phone:0000000000}")
	private String adminPhone;

	@Value("${app.admin.reset-on-startup:false}")
	private boolean resetOnStartup;

	public DataInitializer(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
		this.userAccountRepository = userAccountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (adminEmail == null || adminEmail.isBlank() || adminPassword == null || adminPassword.isBlank()) {
			return; // nothing to seed
		}


		Optional<UserAccount> existing = userAccountRepository.findByEmail(adminEmail);
		if (existing.isPresent()) {
			UserAccount account = existing.get();
			boolean changed = false;
			if (account.getRole() != UserRole.ADMIN) {
				account.setRole(UserRole.ADMIN);
				changed = true;
			}
			if (resetOnStartup) {
				account.setPasswordHash(passwordEncoder.encode(adminPassword));
				account.setFullName(adminFullname);
				account.setPhone(adminPhone);
				changed = true;
			}
			if (changed) {
				userAccountRepository.save(account);
			}
			return;
		}

		UserAccount admin = new UserAccount();
		admin.setFullName(adminFullname);
		admin.setEmail(adminEmail);
		admin.setPhone(adminPhone);
		admin.setPasswordHash(passwordEncoder.encode(adminPassword));
		admin.setRole(UserRole.ADMIN);
		userAccountRepository.save(admin);
	}
}


