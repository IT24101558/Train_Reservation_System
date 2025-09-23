package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.model.Booking;
import __Y2_S1_MTR_02.model.Payment;
import __Y2_S1_MTR_02.model.PaymentMethod;
import __Y2_S1_MTR_02.model.PaymentStatus;
import __Y2_S1_MTR_02.repository.BookingRepository;
import __Y2_S1_MTR_02.repository.PaymentRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository,
                          BookingService bookingService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.bookingService = bookingService;
    }

    public List<Payment> getPaymentsForBooking(Long bookingId) {
        return paymentRepository.findByBookingIdOrderByPaidAtDesc(bookingId);
    }

    @Transactional
    public Payment processPayment(Long bookingId,
                                  BigDecimal amount,
                                  PaymentMethod method,
                                  String payerName,
                                  String payerEmail,
                                  String cardLast4,
                                  String transactionId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setMethod(method);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPayerName(payerName);
        payment.setPayerEmail(payerEmail);
        payment.setCardLast4(cardLast4);
        payment.setTransactionId(transactionId);
        Payment saved = paymentRepository.save(payment);

        bookingService.markBookingPaid(bookingId);
        return saved;
    }

    public List<Payment> getPaymentsForEmail(String email) {
        return paymentRepository.findByPayerEmailOrderByPaidAtDesc(email);
    }
}


