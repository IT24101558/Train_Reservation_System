package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.model.Payment;
import __Y2_S1_MTR_02.model.PaymentMethod;
import __Y2_S1_MTR_02.service.PaymentService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    public List<Payment> listByBooking(@RequestParam("bookingId") Long bookingId) {
        return paymentService.getPaymentsForBooking(bookingId);
    }

    public static class PaymentRequest {
        public Long bookingId;
        public BigDecimal amount;
        public String method; // visa | mastercard | bank | paypal
        public String payerName;
        public String payerEmail;
        public String cardLast4;
        public String transactionId;
    }

    @PostMapping
    public ResponseEntity<Object> pay(@RequestBody PaymentRequest req) {
        try {
            // Validate required fields
            if (req.bookingId == null) {
                return ResponseEntity.badRequest().body("Booking ID is required");
            }
            if (req.amount == null || req.amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest().body("Valid amount is required");
            }
            if (req.method == null || req.method.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Payment method is required");
            }
            if (req.payerEmail == null || req.payerEmail.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Payer email is required");
            }

            // Convert method string to enum
            PaymentMethod method;
            try {
                method = PaymentMethod.valueOf(req.method.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Invalid payment method: " + req.method);
            }

            Payment payment = paymentService.processPayment(
                    req.bookingId,
                    req.amount,
                    method,
                    req.payerName,
                    req.payerEmail,
                    req.cardLast4,
                    req.transactionId
            );
            
            // Check if this was an existing payment (booking already paid)
            boolean wasAlreadyPaid = paymentService.isBookingPaid(req.bookingId);
            if (wasAlreadyPaid) {
                return ResponseEntity.ok().body("{\"message\":\"Payment already processed\", \"payment\":" + 
                    "{\"id\":" + payment.getId() + 
                    ",\"amount\":" + payment.getAmount() + 
                    ",\"method\":\"" + payment.getMethod() + "\"}}");
            }
            
            return ResponseEntity.ok(payment);
        } catch (IllegalArgumentException e) {
            // Log the specific error for debugging
            System.err.println("Payment validation error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Error processing payment: " + e.getMessage());
        } catch (Exception e) {
            // Log the full stack trace for debugging
            System.err.println("Payment processing error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @GetMapping("/history")
    public List<Payment> listByEmail(@RequestParam("email") String email) {
        return paymentService.getPaymentsForEmail(email);
    }

    @GetMapping("/booking/{bookingId}/status")
    public ResponseEntity<Object> getPaymentStatus(@PathVariable Long bookingId) {
        try {
            boolean isPaid = paymentService.isBookingPaid(bookingId);
            if (isPaid) {
                Payment existingPayment = paymentService.getExistingPayment(bookingId);
                return ResponseEntity.ok().body("{\"status\":\"paid\", \"payment\":" + 
                    (existingPayment != null ? "{\"id\":" + existingPayment.getId() + 
                     ",\"amount\":" + existingPayment.getAmount() + 
                     ",\"method\":\"" + existingPayment.getMethod() + "\"}" : "null") + "}");
            } else {
                return ResponseEntity.ok().body("{\"status\":\"pending\"}");
            }
        } catch (Exception e) {
            System.err.println("Error checking payment status for booking ID " + bookingId + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}


