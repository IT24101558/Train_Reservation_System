// src/main/java/com/slraile/controller/GuestScheduleController.java
package _Y2_S1_MTR_02.controller;

import com.slraile.dto.TrainScheduleDTO;
import com.slraile.service.TrainScheduleService;
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
    public ResponseEntity<List<TrainScheduleDTO>> getAllActiveSchedules() {
        return ResponseEntity.ok(
                scheduleService.getAllSchedules().stream()
                        .filter(dto -> "ACTIVE".equals(dto.getStatus().name()))
                        .toList()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<TrainScheduleDTO>> searchSchedules(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String routeFrom,
            @RequestParam(required = false) String routeTo) {

        // Simple search: match routeFrom or routeTo
        List<TrainScheduleDTO> all = scheduleService.getAllSchedules();
        return ResponseEntity.ok(all.stream()
                .filter(dto -> {
                    boolean matchesQuery = query != null && (
                            dto.getTrainName().toLowerCase().contains(query.toLowerCase()) ||
                                    dto.getRouteFrom().toLowerCase().contains(query.toLowerCase()) ||
                                    dto.getRouteTo().toLowerCase().contains(query.toLowerCase())
                    );
                    boolean matchesFrom = routeFrom != null && dto.getRouteFrom().equalsIgnoreCase(routeFrom);
                    boolean matchesTo = routeTo != null && dto.getRouteTo().equalsIgnoreCase(routeTo);

                    return matchesQuery || matchesFrom || matchesTo;
                })
                .filter(dto -> "ACTIVE".equals(dto.getStatus().name()))
                .toList()
        );
    }
}