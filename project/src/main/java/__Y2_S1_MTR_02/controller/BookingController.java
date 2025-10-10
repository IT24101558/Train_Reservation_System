package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.model.Booking;
import __Y2_S1_MTR_02.model.TrainSchedule;
import __Y2_S1_MTR_02.service.BookingService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
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
    public ResponseEntity<Object> listByMember(@RequestParam("email") String memberEmail) {
        System.out.println("BookingController.listByMember called with email: " + memberEmail);
        try {
            List<Booking> bookings = bookingService.getBookingsForMember(memberEmail);
            System.out.println("Found " + bookings.size() + " bookings for email: " + memberEmail);
            
            // Create a simplified response to avoid serialization issues
            List<Object> simplifiedBookings = new ArrayList<>();
            for (Booking booking : bookings) {
                Map<String, Object> bookingData = new HashMap<>();
                bookingData.put("id", booking.getId());
                bookingData.put("memberEmail", booking.getMemberEmail());
                bookingData.put("seatNumbers", booking.getSeatNumbers());
                bookingData.put("totalAmount", booking.getTotalAmount());
                bookingData.put("status", booking.getStatus().toString());
                bookingData.put("travelDate", booking.getTravelDate().toString());
                bookingData.put("createdAt", booking.getCreatedAt().toString());
                
                // Add train schedule info safely
                if (booking.getTrainSchedule() != null) {
                    Map<String, Object> trainData = new HashMap<>();
                    trainData.put("id", booking.getTrainSchedule().getId());
                    trainData.put("trainName", booking.getTrainSchedule().getTrainName());
                    trainData.put("routeFrom", booking.getTrainSchedule().getRouteFrom());
                    trainData.put("routeTo", booking.getTrainSchedule().getRouteTo());
                    trainData.put("departureTime", booking.getTrainSchedule().getDepartureTime().toString());
                    trainData.put("arrivalTime", booking.getTrainSchedule().getArrivalTime().toString());
                    trainData.put("platform", booking.getTrainSchedule().getPlatform());
                    trainData.put("status", booking.getTrainSchedule().getStatus().toString());
                    bookingData.put("trainSchedule", trainData);
                }
                
                simplifiedBookings.add(bookingData);
            }
            
            return ResponseEntity.ok(simplifiedBookings);
        } catch (Exception e) {
            System.err.println("Error retrieving bookings for email " + memberEmail + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error retrieving bookings: " + e.getMessage());
        }
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

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOne(@PathVariable Long id) {
        try {
            Optional<Booking> booking = bookingService.getBooking(id);
            if (booking.isPresent()) {
                return ResponseEntity.ok(booking.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // Log the full stack trace for debugging
            System.err.println("Booking retrieval error for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<Object> getBookingStatus(@PathVariable Long id) {
        try {
            Optional<Booking> booking = bookingService.getBooking(id);
            if (booking.isPresent()) {
                return ResponseEntity.ok().body("{\"status\":\"" + booking.get().getStatus() + "\"}");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Booking status check error for ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @GetMapping("/debug")
    public ResponseEntity<Object> debugBookings(@RequestParam("email") String memberEmail) {
        try {
            System.out.println("Debug endpoint called with email: " + memberEmail);
            List<Booking> bookings = bookingService.getBookingsForMember(memberEmail);
            
            // Create debug response
            StringBuilder debugInfo = new StringBuilder();
            debugInfo.append("Debug Information:\n");
            debugInfo.append("Email: ").append(memberEmail).append("\n");
            debugInfo.append("Total bookings found: ").append(bookings.size()).append("\n");
            
            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                debugInfo.append("Booking ").append(i + 1).append(":\n");
                debugInfo.append("  ID: ").append(b.getId()).append("\n");
                debugInfo.append("  Email: ").append(b.getMemberEmail()).append("\n");
                debugInfo.append("  Status: ").append(b.getStatus()).append("\n");
                debugInfo.append("  Seat Numbers: ").append(b.getSeatNumbers()).append("\n");
                debugInfo.append("  Total Amount: ").append(b.getTotalAmount()).append("\n");
                if (b.getTrainSchedule() != null) {
                    debugInfo.append("  Train: ").append(b.getTrainSchedule().getTrainName()).append("\n");
                }
            }
            
            return ResponseEntity.ok().body(debugInfo.toString());
        } catch (Exception e) {
            System.err.println("Debug endpoint error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Debug error: " + e.getMessage());
        }
    }

    @PostMapping("/create-test")
    public ResponseEntity<Object> createTestBooking(@RequestParam("email") String memberEmail) {
        try {
            System.out.println("Creating test booking for email: " + memberEmail);
            
            // Get the first available train schedule
            List<TrainSchedule> schedules = bookingService.getTrainScheduleRepository().findAll();
            if (schedules.isEmpty()) {
                return ResponseEntity.badRequest().body("No train schedules available");
            }
            
            TrainSchedule schedule = schedules.get(0);
            
            // Create test booking
            Booking testBooking = bookingService.createBooking(
                memberEmail,
                schedule.getId(),
                java.time.LocalDate.now().plusDays(1),
                "A1,B1",
                new java.math.BigDecimal("1500.00")
            );
            
            return ResponseEntity.ok().body("Test booking created with ID: " + testBooking.getId());
            
        } catch (Exception e) {
            System.err.println("Error creating test booking: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error creating test booking: " + e.getMessage());
        }
    }

    @GetMapping("/simple")
    public ResponseEntity<Object> getSimpleBookings(@RequestParam("email") String memberEmail) {
        try {
            System.out.println("Simple bookings endpoint called with email: " + memberEmail);
            List<Booking> bookings = bookingService.getBookingsForMember(memberEmail);
            
            // Return just basic info without complex serialization
            Map<String, Object> response = new HashMap<>();
            response.put("email", memberEmail);
            response.put("count", bookings.size());
            response.put("bookings", bookings.stream().map(b -> {
                Map<String, Object> booking = new HashMap<>();
                booking.put("id", b.getId());
                booking.put("email", b.getMemberEmail());
                booking.put("status", b.getStatus().toString());
                booking.put("seats", b.getSeatNumbers());
                booking.put("amount", b.getTotalAmount());
                return booking;
            }).toList());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Error in simple bookings endpoint: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}


