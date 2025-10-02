// src/main/java/com/slraile/service/TrainScheduleService.java
package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.dto.TrainScheduleDTO;
import __Y2_S1_MTR_02.model.TrainSchedule;
import __Y2_S1_MTR_02.model.TrainStatus;
import __Y2_S1_MTR_02.repository.TrainScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainScheduleService {

    @Autowired
    private TrainScheduleRepository scheduleRepository;

    public List<TrainScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TrainScheduleDTO getScheduleById(Long id) {
        TrainSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        return convertToDTO(schedule);
    }

    public TrainScheduleDTO createSchedule(TrainSchedule schedule) {
        validateSchedule(schedule);
        TrainSchedule saved = scheduleRepository.save(schedule);
        return convertToDTO(saved);
    }

    public TrainScheduleDTO updateSchedule(Long id, TrainSchedule updatedSchedule) {
        TrainSchedule existing = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // Copy fields (avoid overwriting ID)
        existing.setTrainName(updatedSchedule.getTrainName());
        existing.setTrainType(updatedSchedule.getTrainType());
        existing.setRouteFrom(updatedSchedule.getRouteFrom());
        existing.setRouteTo(updatedSchedule.getRouteTo());
        existing.setDepartureTime(updatedSchedule.getDepartureTime());
        existing.setArrivalTime(updatedSchedule.getArrivalTime());
        existing.setPlatform(updatedSchedule.getPlatform());
        existing.setStatus(updatedSchedule.getStatus());
        existing.setEconomyPrice(updatedSchedule.getEconomyPrice());
        existing.setBusinessPrice(updatedSchedule.getBusinessPrice());
        existing.setFirstPrice(updatedSchedule.getFirstPrice());
        existing.setNotes(updatedSchedule.getNotes());

        validateSchedule(existing);

        TrainSchedule saved = scheduleRepository.save(existing);
        return convertToDTO(saved);
    }

    public void deleteSchedule(Long id) {
        if (!scheduleRepository.existsById(id)) {
            throw new RuntimeException("Schedule not found");
        }
        scheduleRepository.deleteById(id);
    }

    public List<TrainScheduleDTO> getSchedulesByStatus(TrainStatus status) {
        return scheduleRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TrainScheduleDTO> getActiveSchedules() {
        return getSchedulesByStatus(TrainStatus.ACTIVE);
    }

    public List<TrainScheduleDTO> searchSchedules(String query, String routeFrom, String routeTo) {
        List<TrainScheduleDTO> all = getAllSchedules();
        String q = query != null ? query.trim().toLowerCase() : null;
        String from = routeFrom != null ? routeFrom.trim().toLowerCase() : null;
        String to = routeTo != null ? routeTo.trim().toLowerCase() : null;

        return all.stream()
                .filter(dto -> {
                    boolean anyFilter = q != null || from != null || to != null;

                    String dtoFrom = dto.getRouteFrom() == null ? "" : dto.getRouteFrom().trim().toLowerCase();
                    String dtoTo = dto.getRouteTo() == null ? "" : dto.getRouteTo().trim().toLowerCase();
                    String dtoName = dto.getTrainName() == null ? "" : dto.getTrainName().trim().toLowerCase();

                    boolean matchesQuery = (q == null) || dtoName.contains(q) || dtoFrom.contains(q) || dtoTo.contains(q);

                    // Flexible contains match for locations (typed value can be part of stored value or vice-versa)
                    boolean fromOk = (from == null) || dtoFrom.contains(from) || from.contains(dtoFrom);
                    boolean toOk = (to == null) || dtoTo.contains(to) || to.contains(dtoTo);

                    boolean locationMatch;
                    if (from != null && to != null) {
                        locationMatch = fromOk && toOk;
                    } else if (from != null) {
                        locationMatch = fromOk;
                    } else if (to != null) {
                        locationMatch = toOk;
                    } else {
                        locationMatch = true;
                    }

                    return !anyFilter || (matchesQuery && locationMatch);
                })
                .collect(Collectors.toList());
    }

    public List<TrainScheduleDTO> searchActiveSchedules(String query, String routeFrom, String routeTo) {
        return searchSchedules(query, routeFrom, routeTo).stream()
                .filter(dto -> dto.getStatus() == TrainStatus.ACTIVE)
                .collect(Collectors.toList());
    }

    public long countByStatus(TrainStatus status) {
        return scheduleRepository.countByStatus(status);
    }

    // Helper: Convert Entity to DTO
    private TrainScheduleDTO convertToDTO(TrainSchedule schedule) {
        String duration = schedule.getDuration(); // computed via @Transient
        String priceDisplay = schedule.getPriceDisplay();

        return new TrainScheduleDTO(
                schedule.getId(),
                schedule.getTrainName(),
                schedule.getTrainType(),
                schedule.getRouteFrom(),
                schedule.getRouteTo(),
                schedule.getDepartureTime().toString(), // HH:mm:ss → we'll trim seconds
                schedule.getArrivalTime().toString(),
                schedule.getPlatform(),
                schedule.getStatus(),
                schedule.getEconomyPrice(),
                schedule.getBusinessPrice(),
                schedule.getFirstPrice(),
                schedule.getNotes(),
                duration,
                priceDisplay
        );
    }

    private void validateSchedule(TrainSchedule schedule) {
        if (schedule.getPlatform() < 1 || schedule.getPlatform() > 15) {
            throw new IllegalArgumentException("Platform must be between 1 and 15");
        }
        if (schedule.getEconomyPrice() <= 0 || schedule.getBusinessPrice() <= 0 || schedule.getFirstPrice() <= 0) {
            throw new IllegalArgumentException("All prices must be greater than zero");
        }
        if (schedule.getDepartureTime().isAfter(schedule.getArrivalTime())) {
            throw new IllegalArgumentException("Departure time cannot be after arrival time");
        }
    }
}