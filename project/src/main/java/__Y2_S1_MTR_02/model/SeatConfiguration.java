// File: src/main/java/com/yourpackage/model/SeatConfiguration.java

package __Y2_S1_MTR_02.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seat_configuration")
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

    public SeatConfiguration() {}

    public SeatConfiguration(Long id, int firstClassSeats, int secondClassSeats, String view360Link) {
        this.id = id;
        this.firstClassSeats = firstClassSeats;
        this.secondClassSeats = secondClassSeats;
        this.view360Link = view360Link;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFirstClassSeats() {
        return firstClassSeats;
    }

    public void setFirstClassSeats(int firstClassSeats) {
        this.firstClassSeats = firstClassSeats;
    }

    public int getSecondClassSeats() {
        return secondClassSeats;
    }

    public void setSecondClassSeats(int secondClassSeats) {
        this.secondClassSeats = secondClassSeats;
    }

    public String getView360Link() {
        return view360Link;
    }

    public void setView360Link(String view360Link) {
        this.view360Link = view360Link;
    }
}