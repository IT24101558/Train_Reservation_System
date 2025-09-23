// src/main/java/com/slraile/controller/AdminScheduleController.java
package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.dto.TrainScheduleDTO;
import __Y2_S1_MTR_02.model.TrainSchedule;
import __Y2_S1_MTR_02.model.TrainStatus;
import __Y2_S1_MTR_02.service.TrainScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/schedules")
@CrossOrigin(origins = "*") // Allow frontend requests (adjust for production)
public class AdminScheduleController {

    @Autowired
    private TrainScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<TrainScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainScheduleDTO> getSchedule(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.getScheduleById(id));
    }

    @PostMapping
    public ResponseEntity<TrainScheduleDTO> createSchedule(@RequestBody TrainSchedule schedule) {
        TrainScheduleDTO created = scheduleService.createSchedule(schedule);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainScheduleDTO> updateSchedule(@PathVariable Long id, @RequestBody TrainSchedule schedule) {
        TrainScheduleDTO updated = scheduleService.updateSchedule(id, schedule);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TrainScheduleDTO>> getSchedulesByStatus(@PathVariable TrainStatus status) {
        return ResponseEntity.ok(scheduleService.getSchedulesByStatus(status));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainScheduleDTO>> searchSchedules(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String routeFrom,
            @RequestParam(required = false) String routeTo,
            @RequestParam(required = false) TrainStatus status
    ) {
        List<TrainScheduleDTO> results = (status == null)
                ? scheduleService.searchSchedules(query, routeFrom, routeTo)
                : scheduleService.searchSchedules(query, routeFrom, routeTo).stream()
                    .filter(dto -> dto.getStatus() == status)
                    .toList();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("ACTIVE", scheduleService.countByStatus(TrainStatus.ACTIVE));
        stats.put("DELAYED", scheduleService.countByStatus(TrainStatus.DELAYED));
        stats.put("CANCELED", scheduleService.countByStatus(TrainStatus.CANCELED));
        stats.put("MAINTENANCE", scheduleService.countByStatus(TrainStatus.MAINTENANCE));
        return ResponseEntity.ok(stats);
    }
}