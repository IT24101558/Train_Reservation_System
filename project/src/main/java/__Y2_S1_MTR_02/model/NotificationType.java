package __Y2_S1_MTR_02.model;

public enum NotificationType {
    SYSTEM("System"),
    ALERT("Alert"),
    MAINTENANCE("Maintenance"),
    UPDATE("Update"),
    INFO("Information");
    
    private final String displayName;
    
    NotificationType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
