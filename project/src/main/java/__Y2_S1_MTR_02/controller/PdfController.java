package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.model.Booking;
import __Y2_S1_MTR_02.service.BookingService;
import __Y2_S1_MTR_02.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin
public class PdfController {

    private final PdfService pdfService;
    private final BookingService bookingService;

    public PdfController(PdfService pdfService, BookingService bookingService) {
        this.pdfService = pdfService;
        this.bookingService = bookingService;
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<byte[]> downloadBookingTicket(@PathVariable Long bookingId) {
        try {
            System.out.println("Generating PDF for booking ID: " + bookingId);
            
            Booking booking = bookingService.getBooking(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

            byte[] pdfBytes = pdfService.generateBookingTicket(booking);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "booking-ticket-" + bookingId + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("Error generating PDF for booking " + bookingId + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<byte[]> downloadBookingHistory(@RequestParam("email") String userEmail) {
        try {
            System.out.println("Generating booking history PDF for email: " + userEmail);
            
            List<Booking> bookings = bookingService.getBookingsForMember(userEmail);
            
            byte[] pdfBytes = pdfService.generateBookingHistoryPdf(bookings, userEmail);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "booking-history-" + userEmail.replace("@", "-") + ".pdf");
            headers.setContentLength(pdfBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("Error generating booking history PDF for " + userEmail + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}

