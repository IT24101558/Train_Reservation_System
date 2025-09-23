// File: src/main/java/com/yourpackage/repository/SeatConfigurationRepository.java

package __Y2_S1_MTR_02.repository;

import __Y2_S1_MTR_02.model.SeatConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatConfigurationRepository extends JpaRepository<SeatConfiguration, Long> {

    // Optional: If you want to get the latest or only config
    // SeatConfiguration findTopByOrderByIdDesc();
}