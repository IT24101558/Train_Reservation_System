// src/main/java/com/slraile/controller/GuestScheduleController.java
package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.dto.TrainScheduleDTO;
import __Y2_S1_MTR_02.service.TrainScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guest/schedules")
@CrossOrigin(origins = "*")
public class GuestScheduleController {

    @Autowired
    private TrainScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<TrainScheduleDTO>> getAllSchedulesForGuest() {
        // Show all available schedules to guests on load
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainScheduleDTO>> searchSchedules(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String routeFrom,
            @RequestParam(required = false) String routeTo) {

        // Show all matching schedules (not only ACTIVE) for guest search
        return ResponseEntity.ok(scheduleService.searchSchedules(query, routeFrom, routeTo));
    }
}