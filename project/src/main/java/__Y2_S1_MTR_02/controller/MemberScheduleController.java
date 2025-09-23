// src/main/java/com/slraile/controller/MemberScheduleController.java
package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.dto.TrainScheduleDTO;
import __Y2_S1_MTR_02.service.TrainScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/member/schedules")
@CrossOrigin(origins = "*")
public class MemberScheduleController {

    @Autowired
    private TrainScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<TrainScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainScheduleDTO>> searchSchedules(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String routeFrom,
            @RequestParam(required = false) String routeTo) {

        return ResponseEntity.ok(scheduleService.searchSchedules(query, routeFrom, routeTo));
    }
}