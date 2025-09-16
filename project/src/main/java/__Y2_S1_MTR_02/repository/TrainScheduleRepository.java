// src/main/java/com/_Y2_S1_MTR_02/repository/TrainScheduleRepository.java
package _Y2_S1_MTR_02.repository;

import com.slraile.model.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, Long> {
    List<TrainSchedule> findByStatus(TrainSchedule.TrainStatus status);
    List<TrainSchedule> findByRouteFromContainingIgnoreCaseOrRouteToContainingIgnoreCase(String from, String to);
}