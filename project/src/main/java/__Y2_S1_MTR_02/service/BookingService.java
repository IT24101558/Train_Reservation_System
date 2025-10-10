package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.model.Booking;
import __Y2_S1_MTR_02.model.BookingStatus;
import __Y2_S1_MTR_02.model.TrainSchedule;
import __Y2_S1_MTR_02.repository.BookingRepository;
import __Y2_S1_MTR_02.repository.TrainScheduleRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
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
        System.out.println("BookingService.getBookingsForMember called with email: " + memberEmail);
        try {
            List<Booking> bookings = bookingRepository.findByMemberEmailOrderByCreatedAtDesc(memberEmail);
            System.out.println("Repository returned " + bookings.size() + " bookings for email: " + memberEmail);
            if (!bookings.isEmpty()) {
                System.out.println("First booking details: ID=" + bookings.get(0).getId() + 
                                 ", Status=" + bookings.get(0).getStatus() + 
                                 ", Email=" + bookings.get(0).getMemberEmail());
            }
            return bookings;
        } catch (Exception e) {
            System.err.println("Error in BookingService.getBookingsForMember for email " + memberEmail + ": " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    // For seat map: gather occupied seat numbers for a given schedule and date
    public List<String> getOccupiedSeatNumbers(Long trainScheduleId, String travelDateIso) {
        LocalDate date = LocalDate.parse(travelDateIso);
        return bookingRepository.findByTrainSchedule_IdAndTravelDate(trainScheduleId, date)
                .stream()
                .flatMap(b -> Arrays.stream(b.getSeatNumbers().split(",")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();
    }

    @Transactional
    public Booking createBooking(String memberEmail,
                                 Long trainScheduleId,
                                 LocalDate travelDate,
                                 String seatNumbers,
                                 BigDecimal totalAmount) {
        System.out.println("BookingService.createBooking called with:");
        System.out.println("  memberEmail: " + memberEmail);
        System.out.println("  trainScheduleId: " + trainScheduleId);
        System.out.println("  travelDate: " + travelDate);
        System.out.println("  seatNumbers: " + seatNumbers);
        System.out.println("  totalAmount: " + totalAmount);
        
        TrainSchedule schedule = trainScheduleRepository.findById(trainScheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Train schedule not found: " + trainScheduleId));

        Booking booking = new Booking();
        booking.setMemberEmail(memberEmail);
        booking.setTrainSchedule(schedule);
        booking.setTravelDate(travelDate);
        booking.setSeatNumbers(seatNumbers);
        booking.setTotalAmount(totalAmount);
        booking.setStatus(BookingStatus.PENDING_PAYMENT);
        
        Booking saved = bookingRepository.save(booking);
        System.out.println("Booking created successfully with ID: " + saved.getId());
        System.out.println("Saved booking email: " + saved.getMemberEmail());
        System.out.println("Saved booking status: " + saved.getStatus());
        
        return saved;
    }

    public Optional<Booking> getBooking(Long id) {
        try {
            return bookingRepository.findById(id);
        } catch (Exception e) {
            System.err.println("Error retrieving booking with ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Transactional
    public void markBookingPaid(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);
    }

    public TrainScheduleRepository getTrainScheduleRepository() {
        return trainScheduleRepository;
    }
}


