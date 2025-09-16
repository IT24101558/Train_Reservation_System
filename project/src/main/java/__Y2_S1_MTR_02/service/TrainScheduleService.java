// src/main/java/com/slraile/service/TrainScheduleService.java
package _Y2_S1_MTR_02.service;

import com.slraile.dto.TrainScheduleDTO;
import com.slraile.model.TrainSchedule;
import com.slraile.model.TrainStatus;
import com.slraile.repository.TrainScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
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