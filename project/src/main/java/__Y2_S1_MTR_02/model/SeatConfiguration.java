// File: src/main/java/com/yourpackage/model/SeatConfiguration.java

package com.__Y2_S1_MTR_02.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seat_configuration")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeatConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_class_seats", nullable = false)
    private int firstClassSeats;

    @Column(name = "second_class_seats", nullable = false)
    private int secondClassSeats;

    @Column(name = "view_360_link", length = 500)
    private String view360Link;

    // You might want to add trainId if this is per-train configuration
    // @Column(name = "train_id")
    // private String trainId;
}