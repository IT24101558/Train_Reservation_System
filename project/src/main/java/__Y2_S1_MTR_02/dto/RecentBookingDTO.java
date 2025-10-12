package __Y2_S1_MTR_02.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class RecentBookingDTO {
    private Long id;
    private String bookingId;
    private String passengerName;
    private String passengerEmail;
    private String trainName;
    private String trainNumber;
    private LocalDate travelDate;
    private String status;
    private BigDecimal amount;
    private OffsetDateTime createdAt;

    public RecentBookingDTO() {}

    public RecentBookingDTO(Long id, String bookingId, String passengerName, String passengerEmail, 
                           String trainName, String trainNumber, LocalDate travelDate, 
                           String status, BigDecimal amount, OffsetDateTime createdAt) {
        this.id = id;
        this.bookingId = bookingId;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.travelDate = travelDate;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }

    public String getTrainName() {
        return trainName;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public LocalDate getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(LocalDate travelDate) {
        this.travelDate = travelDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
