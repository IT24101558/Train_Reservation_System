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
                .toList()
        );
    }
}