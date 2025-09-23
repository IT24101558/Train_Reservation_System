package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.model.Booking;
import __Y2_S1_MTR_02.model.BookingStatus;
import __Y2_S1_MTR_02.model.TrainSchedule;
import __Y2_S1_MTR_02.repository.BookingRepository;
import __Y2_S1_MTR_02.repository.TrainScheduleRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainScheduleRepository trainScheduleRepository;

    public BookingService(BookingRepository bookingRepository, TrainScheduleRepository trainScheduleRepository) {
        this.bookingRepository = bookingRepository;
        this.trainScheduleRepository = trainScheduleRepository;
    }

    public List<Booking> getBookingsForMember(String memberEmail) {
        return bookingRepository.findByMemberEmailOrderByCreatedAtDesc(memberEmail);
    }

    @Transactional
    public Booking createBooking(String memberEmail,
                                 Long trainScheduleId,
                                 LocalDate travelDate,
                                 String seatNumbers,
                                 BigDecimal totalAmount) {
        TrainSchedule schedule = trainScheduleRepository.findById(trainScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Train schedule not found: " + trainScheduleId));

        Booking booking = new Booking();
        booking.setMemberEmail(memberEmail);
        booking.setTrainSchedule(schedule);
        booking.setTravelDate(travelDate);
        booking.setSeatNumbers(seatNumbers);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBooking(Long id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public void markBookingPaid(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);
    }
}


