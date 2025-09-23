// src/main/java/com/_Y2_S1_MTR_02/dto/TrainScheduleDTO.java
package __Y2_S1_MTR_02.dto;

import __Y2_S1_MTR_02.model.TrainType;
import __Y2_S1_MTR_02.model.TrainStatus;

public class TrainScheduleDTO {

    private Long id;
    private String trainName;
    private TrainType trainType;
    private String routeFrom;
    private String routeTo;
    private String departureTime; // HH:mm
    private String arrivalTime;   // HH:mm
    private Integer platform;
    private TrainStatus status;
    private Double economyPrice;
    private Double businessPrice;
    private Double firstPrice;
    private String notes;
    private String duration; // calculated
    private String priceDisplay; // calculated

    // Constructors
    public TrainScheduleDTO() {}

    public TrainScheduleDTO(Long id, String trainName, TrainType trainType, String routeFrom, String routeTo,
                            String departureTime, String arrivalTime, Integer platform, TrainStatus status,
                            Double economyPrice, Double businessPrice, Double firstPrice, String notes,
                            String duration, String priceDisplay) {
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
        this.duration = duration;
        this.priceDisplay = priceDisplay;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTrainName() { return trainName; }
    public void setTrainName(String trainName) { this.trainName = trainName; }
    
    public TrainType getTrainType() { return trainType; }
    public void setTrainType(TrainType trainType) { this.trainType = trainType; }
    
    public String getRouteFrom() { return routeFrom; }
    public void setRouteFrom(String routeFrom) { this.routeFrom = routeFrom; }
    
    public String getRouteTo() { return routeTo; }
    public void setRouteTo(String routeTo) { this.routeTo = routeTo; }
    
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    
    public String getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }
    
    public Integer getPlatform() { return platform; }
    public void setPlatform(Integer platform) { this.platform = platform; }
    
    public TrainStatus getStatus() { return status; }
    public void setStatus(TrainStatus status) { this.status = status; }
    
    public Double getEconomyPrice() { return economyPrice; }
    public void setEconomyPrice(Double economyPrice) { this.economyPrice = economyPrice; }
    
    public Double getBusinessPrice() { return businessPrice; }
    public void setBusinessPrice(Double businessPrice) { this.businessPrice = businessPrice; }
    
    public Double getFirstPrice() { return firstPrice; }
    public void setFirstPrice(Double firstPrice) { this.firstPrice = firstPrice; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    
    public String getPriceDisplay() { return priceDisplay; }
    public void setPriceDisplay(String priceDisplay) { this.priceDisplay = priceDisplay; }
}