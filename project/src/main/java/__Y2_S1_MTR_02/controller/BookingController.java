package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.model.Booking;
import __Y2_S1_MTR_02.service.BookingService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<Booking> listByMember(@RequestParam("email") String memberEmail) {
        return bookingService.getBookingsForMember(memberEmail);
    }

    // Seats reserved/occupied lookup for Select Your Seat page
    @GetMapping("/occupied")
    public ResponseEntity<List<String>> getOccupiedSeats(
            @RequestParam("trainScheduleId") Long trainScheduleId,
            @RequestParam("travelDate") String travelDateIso) {
        return ResponseEntity.ok(bookingService.getOccupiedSeatNumbers(trainScheduleId, travelDateIso));
    }

    public static class CreateBookingRequest {
        public String memberEmail;
        public Long trainScheduleId;
        public String travelDate; // ISO yyyy-MM-dd
        public String seatNumbers; // comma separated
        public BigDecimal totalAmount;
    }

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody CreateBookingRequest req) {
        Booking created = bookingService.createBooking(
                req.memberEmail,
                req.trainScheduleId,
                LocalDate.parse(req.travelDate),
                req.seatNumbers,
                req.totalAmount
        );
        return ResponseEntity.ok(created);
    }
}


