package __Y2_S1_MTR_02.model;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "train_schedules")
public class TrainSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String trainName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainType trainType;

    @Column(nullable = false)
    private String routeFrom;

    @Column(nullable = false)
    private String routeTo;

    @Column(nullable = false)
    private LocalTime departureTime;

    @Column(nullable = false)
    private LocalTime arrivalTime;

    @Column(nullable = false)
    private Integer platform; // 1-15

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainStatus status;

    @Column(nullable = false)
    private Double economyPrice;

    @Column(nullable = false)
    private Double businessPrice;

    @Column(nullable = false)
    private Double firstPrice;

    @Column(length = 500)
    private String notes;

    // Constructors
    public TrainSchedule() {}

    public TrainSchedule(Long id, String trainName, TrainType trainType, String routeFrom, String routeTo, 
                        LocalTime departureTime, LocalTime arrivalTime, Integer platform, TrainStatus status,
                        Double economyPrice, Double businessPrice, Double firstPrice, String notes) {
        this.id = id;
        this.trainName = trainName;
        this.trainType = trainType;
        this.routeFrom = routeFrom;
        this.routeTo = routeTo;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.platform = platform;
        this.status = status;
        this.economyPrice = economyPrice;
        this.businessPrice = businessPrice;
        this.firstPrice = firstPrice;
        this.notes = notes;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTrainName() {
        return trainName;
    }

    public TrainType getTrainType() {
        return trainType;
    }

    public String getRouteFrom() {
        return routeFrom;
    }

    public String getRouteTo() {
        return routeTo;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public Integer getPlatform() {
        return platform;
    }

    public TrainStatus getStatus() {
        return status;
    }

    public Double getEconomyPrice() {
        return economyPrice;
    }

    public Double getBusinessPrice() {
        return businessPrice;
    }

    public Double getFirstPrice() {
        return firstPrice;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTrainName(String trainName) {
        this.trainName = trainName;
    }

    public void setTrainType(TrainType trainType) {
        this.trainType = trainType;
    }

    public void setRouteFrom(String routeFrom) {
        this.routeFrom = routeFrom;
    }

    public void setRouteTo(String routeTo) {
        this.routeTo = routeTo;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public void setStatus(TrainStatus status) {
        this.status = status;
    }

    public void setEconomyPrice(Double economyPrice) {
        this.economyPrice = economyPrice;
    }

    public void setBusinessPrice(Double businessPrice) {
        this.businessPrice = businessPrice;
    }

    public void setFirstPrice(Double firstPrice) {
        this.firstPrice = firstPrice;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Optional: auto-calculate duration if needed
    @Transient
    public String getDuration() {
        long minutes = java.time.Duration.between(departureTime, arrivalTime).toMinutes();
        long hours = minutes / 60;
        long mins = minutes % 60;
        return hours + "h " + mins + "m";
    }

    // Helper method to format price string as "$45 / $75 / $120"
    @Transient
    public String getPriceDisplay() {
        return String.format("$%.2f / $%.2f / $%.2f", economyPrice, businessPrice, firstPrice);
    }

    public String getTrainNumber() {

        return "";}
    }
