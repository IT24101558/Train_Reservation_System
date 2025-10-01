// File: src/main/java/com/yourpackage/controller/AdminSeatController.java

package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.dto.SeatConfigurationDTO;
import __Y2_S1_MTR_02.model.SeatConfiguration;
import __Y2_S1_MTR_02.repository.SeatConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000") // Adjust if your frontend runs elsewhere
public class AdminSeatController {

    @Autowired
    private SeatConfigurationRepository seatConfigRepo;

    // POST endpoint to save seat configuration
    @PostMapping("/seats")
    public ResponseEntity<String> saveSeatConfiguration(@RequestBody SeatConfigurationDTO dto) {
        try {
            SeatConfiguration config = seatConfigRepo.findAll().stream().findFirst().orElse(new SeatConfiguration());

            config.setFirstClassSeats(dto.getFirstClassSeats());
            config.setSecondClassSeats(dto.getSecondClassSeats());
            config.setView360Link(dto.getView360Link());

            seatConfigRepo.save(config);

            return ResponseEntity.ok("Seat configuration saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving seat configuration: " + e.getMessage());
        }
    }

    // 👇 ADD THE GET ENDPOINT HERE 👇
    @GetMapping("/seats")
    public ResponseEntity<SeatConfiguration> getSeatConfiguration() {
        SeatConfiguration config = seatConfigRepo.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No seat configuration found"));
        return ResponseEntity.ok(config);
    }

    // Compatibility endpoint used by existing frontend
    @GetMapping("/seats/current")
    public ResponseEntity<SeatConfiguration> getCurrentSeatConfiguration() {
        SeatConfiguration config = seatConfigRepo.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No seat configuration found"));
        return ResponseEntity.ok(config);
    }
}