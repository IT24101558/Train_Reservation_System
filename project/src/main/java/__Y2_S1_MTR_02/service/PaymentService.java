package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.model.*;
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

    public PaymentService(PaymentRepository paymentRepository,
                          BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
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
        Booking booking;
        try {
            // Validate booking exists
            booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));

            // Check if booking is already paid - return existing payment instead of error
            if (booking.getStatus() == BookingStatus.PAID) {
                // Return the most recent payment for this booking
                List<Payment> existingPayments = paymentRepository.findByBookingIdOrderByPaidAtDesc(bookingId);
                if (!existingPayments.isEmpty()) {
                    return existingPayments.get(0); // Return the most recent payment
                }
                // If no payment found but status is PAID, this is an inconsistent state
                throw new IllegalArgumentException("Booking is marked as paid but no payment record found");
            }
        } catch (IllegalArgumentException e) {
            // Re-throw validation errors
            throw e;
        } catch (Exception e) {
            System.err.println("Error retrieving booking with ID " + bookingId + ": " + e.getMessage());
            e.printStackTrace();
            throw new IllegalArgumentException("Error processing payment: Unable to retrieve booking");
        }

        // Create payment
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

        // Mark booking as paid within the same transaction
        booking.setStatus(BookingStatus.PAID);
        bookingRepository.save(booking);
        
        return saved;
    }

    public List<Payment> getPaymentsForEmail(String email) {
        return paymentRepository.findByPayerEmailOrderByPaidAtDesc(email);
    }

    public boolean isBookingPaid(Long bookingId) {
        try {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with ID: " + bookingId));
            return booking.getStatus() == BookingStatus.PAID;
        } catch (Exception e) {
            System.err.println("Error checking payment status for booking ID " + bookingId + ": " + e.getMessage());
            return false;
        }
    }

    public Payment getExistingPayment(Long bookingId) {
        try {
            List<Payment> payments = paymentRepository.findByBookingIdOrderByPaidAtDesc(bookingId);
            return payments.isEmpty() ? null : payments.get(0);
        } catch (Exception e) {
            System.err.println("Error retrieving existing payment for booking ID " + bookingId + ": " + e.getMessage());
            return null;
        }
    }
}


