package __Y2_S1_MTR_02.repository;

import __Y2_S1_MTR_02.model.Payment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.booking.id = :bookingId order by p.paidAt desc")
    List<Payment> findByBookingIdOrderByPaidAtDesc(@Param("bookingId") Long bookingId);

    @Query("select p from Payment p where p.payerEmail = :email order by p.paidAt desc")
    List<Payment> findByPayerEmailOrderByPaidAtDesc(@Param("email") String email);
}


