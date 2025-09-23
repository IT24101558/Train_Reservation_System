package __Y2_S1_MTR_02.repository;

import __Y2_S1_MTR_02.model.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByMemberEmailOrderByCreatedAtDesc(String memberEmail);
}


