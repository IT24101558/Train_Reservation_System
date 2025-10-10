package __Y2_S1_MTR_02.config;

import __Y2_S1_MTR_02.model.SeatConfiguration;
import __Y2_S1_MTR_02.model.TrainSchedule;
import __Y2_S1_MTR_02.model.TrainStatus;
import __Y2_S1_MTR_02.model.TrainType;
import __Y2_S1_MTR_02.model.UserAccount;
import __Y2_S1_MTR_02.model.UserRole;
import __Y2_S1_MTR_02.repository.SeatConfigurationRepository;
import __Y2_S1_MTR_02.repository.TrainScheduleRepository;
import __Y2_S1_MTR_02.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Component
public class DataInitializer implements ApplicationRunner {

	private final UserAccountRepository userAccountRepository;
	private final TrainScheduleRepository trainScheduleRepository;
	private final SeatConfigurationRepository seatConfigurationRepository;
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

	public DataInitializer(UserAccountRepository userAccountRepository, 
	                      TrainScheduleRepository trainScheduleRepository,
	                      SeatConfigurationRepository seatConfigurationRepository,
	                      PasswordEncoder passwordEncoder) {
		this.userAccountRepository = userAccountRepository;
		this.trainScheduleRepository = trainScheduleRepository;
		this.seatConfigurationRepository = seatConfigurationRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) {
		// Initialize admin user
		initializeAdminUser();
		
		// Initialize seat configuration
		initializeSeatConfiguration();
		
		// Initialize train schedules
		initializeTrainSchedules();
	}

	private void initializeAdminUser() {
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

	private void initializeSeatConfiguration() {
		// Check if seat configuration already exists
		if (seatConfigurationRepository.count() > 0) {
			return; // Don't duplicate data
		}

		// Create default seat configuration
		SeatConfiguration config = new SeatConfiguration();
		config.setFirstClassSeats(20);  // 20 first class seats
		config.setSecondClassSeats(80);  // 80 second class seats
		config.setView360Link("https://example.com/360view"); // Placeholder link
		
		seatConfigurationRepository.save(config);
	}

	private void initializeTrainSchedules() {
		// Check if schedules already exist
		if (trainScheduleRepository.count() > 0) {
			return; // Don't duplicate data
		}

		// Create sample train schedules
		List<TrainSchedule> schedules = List.of(
			// Colombo to Kandy routes
			new TrainSchedule(null, "Express Train 01", TrainType.EXPRESS, "Colombo Fort", "Kandy", 
				LocalTime.of(6, 30), LocalTime.of(9, 15), 1, TrainStatus.ACTIVE, 
				450.0, 750.0, 1200.0, "Direct express service"),
			
			new TrainSchedule(null, "Intercity Train 02", TrainType.INTERCITY, "Colombo Fort", "Kandy", 
				LocalTime.of(8, 0), LocalTime.of(11, 30), 2, TrainStatus.ACTIVE, 
				400.0, 650.0, 1000.0, "Comfortable intercity service"),
			
			new TrainSchedule(null, "Express Train 03", TrainType.EXPRESS, "Colombo Fort", "Kandy", 
				LocalTime.of(14, 0), LocalTime.of(16, 45), 3, TrainStatus.ACTIVE, 
				450.0, 750.0, 1200.0, "Afternoon express service"),

			// Colombo to Galle routes
			new TrainSchedule(null, "Coastal Express 04", TrainType.EXPRESS, "Colombo Fort", "Galle", 
				LocalTime.of(7, 15), LocalTime.of(9, 45), 4, TrainStatus.ACTIVE, 
				350.0, 550.0, 900.0, "Scenic coastal route"),
			
			new TrainSchedule(null, "Intercity Train 05", TrainType.INTERCITY, "Colombo Fort", "Galle", 
				LocalTime.of(10, 30), LocalTime.of(13, 0), 5, TrainStatus.ACTIVE, 
				300.0, 500.0, 800.0, "Mid-morning coastal service"),

			// Colombo to Anuradhapura routes
			new TrainSchedule(null, "Northern Express 06", TrainType.EXPRESS, "Colombo Fort", "Anuradhapura", 
				LocalTime.of(5, 45), LocalTime.of(9, 30), 6, TrainStatus.ACTIVE, 
				500.0, 800.0, 1300.0, "Northern express service"),
			
			new TrainSchedule(null, "Intercity Train 07", TrainType.INTERCITY, "Colombo Fort", "Anuradhapura", 
				LocalTime.of(12, 0), LocalTime.of(16, 15), 7, TrainStatus.ACTIVE, 
				450.0, 700.0, 1100.0, "Afternoon northern service"),

			// Kandy to Colombo routes (return journeys)
			new TrainSchedule(null, "Express Train 08", TrainType.EXPRESS, "Kandy", "Colombo Fort", 
				LocalTime.of(10, 0), LocalTime.of(12, 45), 8, TrainStatus.ACTIVE, 
				450.0, 750.0, 1200.0, "Return express service"),
			
			new TrainSchedule(null, "Intercity Train 09", TrainType.INTERCITY, "Kandy", "Colombo Fort", 
				LocalTime.of(16, 30), LocalTime.of(20, 0), 9, TrainStatus.ACTIVE, 
				400.0, 650.0, 1000.0, "Evening return service"),

			// Galle to Colombo routes (return journeys)
			new TrainSchedule(null, "Coastal Express 10", TrainType.EXPRESS, "Galle", "Colombo Fort", 
				LocalTime.of(11, 0), LocalTime.of(13, 30), 10, TrainStatus.ACTIVE, 
				350.0, 550.0, 900.0, "Return coastal express"),
			
			new TrainSchedule(null, "Intercity Train 11", TrainType.INTERCITY, "Galle", "Colombo Fort", 
				LocalTime.of(15, 15), LocalTime.of(17, 45), 11, TrainStatus.ACTIVE, 
				300.0, 500.0, 800.0, "Evening coastal return"),

			// Anuradhapura to Colombo routes (return journeys)
			new TrainSchedule(null, "Northern Express 12", TrainType.EXPRESS, "Anuradhapura", "Colombo Fort", 
				LocalTime.of(11, 30), LocalTime.of(15, 15), 12, TrainStatus.ACTIVE, 
				500.0, 800.0, 1300.0, "Return northern express"),
			
			new TrainSchedule(null, "Intercity Train 13", TrainType.INTERCITY, "Anuradhapura", "Colombo Fort", 
				LocalTime.of(18, 0), LocalTime.of(22, 15), 13, TrainStatus.ACTIVE, 
				450.0, 700.0, 1100.0, "Evening northern return"),

			// Additional routes for better coverage
			new TrainSchedule(null, "Express Train 14", TrainType.EXPRESS, "Colombo Fort", "Trincomalee", 
				LocalTime.of(6, 0), LocalTime.of(11, 30), 14, TrainStatus.ACTIVE, 
				600.0, 950.0, 1500.0, "Eastern express service"),
			
			new TrainSchedule(null, "Intercity Train 15", TrainType.INTERCITY, "Colombo Fort", "Jaffna", 
				LocalTime.of(7, 30), LocalTime.of(14, 45), 15, TrainStatus.ACTIVE, 
				700.0, 1100.0, 1800.0, "Northern intercity service")
		);

		// Save all schedules
		trainScheduleRepository.saveAll(schedules);
	}
}


