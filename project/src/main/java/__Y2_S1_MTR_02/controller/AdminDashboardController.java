package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.dto.AdminDashboardStatsDTO;
import __Y2_S1_MTR_02.dto.RecentBookingDTO;
import __Y2_S1_MTR_02.model.Booking;
import __Y2_S1_MTR_02.model.BookingStatus;
import __Y2_S1_MTR_02.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    @Autowired
    private BookingRepository bookingRepository;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println("AdminDashboardController test endpoint called");
        return ResponseEntity.ok("Admin Dashboard Controller is working!");
    }

    @GetMapping("/simple-stats")
    public ResponseEntity<Map<String, Object>> getSimpleStats() {
        try {
            System.out.println("=== Simple stats endpoint called ===");
            
            long totalBookings = bookingRepository.count();
            System.out.println("Total bookings count: " + totalBookings);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalBookings", totalBookings);
            stats.put("message", "Simple stats working");
            stats.put("timestamp", java.time.LocalDateTime.now());
            
            System.out.println("Returning simple stats: " + stats);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("Error in simple stats: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/dashboard/stats")
    public ResponseEntity<AdminDashboardStatsDTO> getDashboardStats() {
        try {
            System.out.println("=== AdminDashboardController.getDashboardStats called ===");
            
            // Get total bookings
            long totalBookings = bookingRepository.count();
            System.out.println("Total bookings: " + totalBookings);
            
            // Get total revenue (sum of all paid bookings) - using stream approach for now
            List<Booking> allBookings = bookingRepository.findAll();
            System.out.println("Found " + allBookings.size() + " total bookings in database");
            
            BigDecimal totalRevenue = allBookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.PAID)
                .map(Booking::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("Total revenue: " + totalRevenue);
            
            // Get today's tickets
            LocalDate today = LocalDate.now();
            long todaysTickets = allBookings.stream()
                .filter(booking -> booking.getCreatedAt().toLocalDate().equals(today))
                .count();
            System.out.println("Today's tickets: " + todaysTickets);
            
            // Get booking counts by status
            long confirmedBookings = allBookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.PAID)
                .count();
            long pendingBookings = allBookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.PENDING_PAYMENT)
                .count();
            long cancelledBookings = allBookings.stream()
                .filter(booking -> booking.getStatus() == BookingStatus.CANCELLED)
                .count();

            AdminDashboardStatsDTO stats = new AdminDashboardStatsDTO(
                totalBookings, totalRevenue, todaysTickets, 
                confirmedBookings, pendingBookings, cancelledBookings
            );

            System.out.println("Returning stats: " + stats);
            System.out.println("=== End of getDashboardStats ===");
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            System.err.println("Error getting dashboard stats: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/dashboard/recent-bookings")
    public ResponseEntity<List<RecentBookingDTO>> getRecentBookings(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            System.out.println("AdminDashboardController.getRecentBookings called with limit: " + limit);
            List<Booking> recentBookings = bookingRepository.findAll()
                .stream()
                .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                .limit(limit)
                .collect(Collectors.toList());
            System.out.println("Found " + recentBookings.size() + " recent bookings");

            List<RecentBookingDTO> recentBookingDTOs = recentBookings.stream()
                .map(booking -> {
                    String passengerName = extractNameFromEmail(booking.getMemberEmail());
                    String trainName = booking.getTrainSchedule() != null ? 
                        booking.getTrainSchedule().getTrainName() : "Unknown Train";
                    String trainNumber = booking.getTrainSchedule() != null ? 
                        booking.getTrainSchedule().getTrainNumber() : "N/A";
                    
                    return new RecentBookingDTO(
                        booking.getId(),
                        "#BK" + String.format("%04d", booking.getId()),
                        passengerName,
                        booking.getMemberEmail(),
                        trainName,
                        trainNumber,
                        booking.getTravelDate(),
                        booking.getStatus().toString(),
                        booking.getTotalAmount(),
                        booking.getCreatedAt()
                    );
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(recentBookingDTOs);
        } catch (Exception e) {
            System.err.println("Error getting recent bookings: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    private String extractNameFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Unknown";
        }
        
        // Extract the part before @ and replace dots/underscores with spaces
        String namePart = email.split("@")[0];
        namePart = namePart.replace(".", " ").replace("_", " ");
        
        // Capitalize first letter of each word
        String[] words = namePart.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                      .append(word.substring(1).toLowerCase())
                      .append(" ");
            }
        }
        
        return result.toString().trim();
    }
}
