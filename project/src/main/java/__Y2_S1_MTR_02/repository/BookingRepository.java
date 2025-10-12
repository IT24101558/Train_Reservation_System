package __Y2_S1_MTR_02.repository;

import __Y2_S1_MTR_02.model.Booking;
import __Y2_S1_MTR_02.model.BookingStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByMemberEmailOrderByCreatedAtDesc(String memberEmail);

    List<Booking> findByTrainSchedule_IdAndTravelDate(Long trainScheduleId, LocalDate travelDate);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = :status")
    long countByStatus(@Param("status") BookingStatus status);
    
    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status = :status")
    BigDecimal sumTotalAmountByStatus(@Param("status") BookingStatus status);
    
    @Query("SELECT COUNT(b) FROM Booking b WHERE DATE(b.createdAt) = :date")
    long countByCreatedDate(@Param("date") LocalDate date);
    
    @Query("SELECT b FROM Booking b ORDER BY b.createdAt DESC")
    List<Booking> findAllOrderByCreatedAtDesc();
}


