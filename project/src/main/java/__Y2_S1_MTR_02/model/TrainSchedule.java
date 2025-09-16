// src/main/java/com/_Y2_S1_MTR_02/model/TrainSchedule.java
package _Y2_S1_MTR_02.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "train_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}

// Add this enum inside TrainSchedule.java or in separate file
enum TrainStatus {
    ACTIVE, DELAYED, CANCELED, MAINTENANCE
}