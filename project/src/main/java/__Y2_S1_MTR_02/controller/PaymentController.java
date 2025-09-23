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
    public ResponseEntity<Payment> pay(@RequestBody PaymentRequest req) {
        PaymentMethod method = PaymentMethod.valueOf(req.method.toUpperCase());
        Payment payment = paymentService.processPayment(
                req.bookingId,
                req.amount,
                method,
                req.payerName,
                req.payerEmail,
                req.cardLast4,
                req.transactionId
        );
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/history")
    public List<Payment> listByEmail(@RequestParam("email") String email) {
        return paymentService.getPaymentsForEmail(email);
    }
}


