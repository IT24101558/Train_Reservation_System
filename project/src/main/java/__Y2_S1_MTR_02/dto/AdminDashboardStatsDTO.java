package __Y2_S1_MTR_02.dto;

import java.math.BigDecimal;

public class AdminDashboardStatsDTO {
    private long totalBookings;
    private BigDecimal totalRevenue;
    private long todaysTickets;
    private long confirmedBookings;
    private long pendingBookings;
    private long cancelledBookings;

    public AdminDashboardStatsDTO() {}

    public AdminDashboardStatsDTO(long totalBookings, BigDecimal totalRevenue, long todaysTickets, 
                                 long confirmedBookings, long pendingBookings, long cancelledBookings) {
        this.totalBookings = totalBookings;
        this.totalRevenue = totalRevenue;
        this.todaysTickets = todaysTickets;
        this.confirmedBookings = confirmedBookings;
        this.pendingBookings = pendingBookings;
        this.cancelledBookings = cancelledBookings;
    }

    public long getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(long totalBookings) {
        this.totalBookings = totalBookings;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public long getTodaysTickets() {
        return todaysTickets;
    }

    public void setTodaysTickets(long todaysTickets) {
        this.todaysTickets = todaysTickets;
    }

    public long getConfirmedBookings() {
        return confirmedBookings;
    }

    public void setConfirmedBookings(long confirmedBookings) {
        this.confirmedBookings = confirmedBookings;
    }

    public long getPendingBookings() {
        return pendingBookings;
    }

    public void setPendingBookings(long pendingBookings) {
        this.pendingBookings = pendingBookings;
    }

    public long getCancelledBookings() {
        return cancelledBookings;
    }

    public void setCancelledBookings(long cancelledBookings) {
        this.cancelledBookings = cancelledBookings;
    }
}
