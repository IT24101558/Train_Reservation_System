// File: src/main/java/com/yourpackage/dto/SeatConfigurationDTO.java

package com.__Y2_S1_MTR_02.dto;

public class SeatConfigurationDTO {
    private int firstClassSeats;
    private int secondClassSeats;
    private String view360Link;

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