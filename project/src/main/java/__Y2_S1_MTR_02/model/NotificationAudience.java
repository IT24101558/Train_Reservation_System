package __Y2_S1_MTR_02.model;

public enum NotificationAudience {
    ALL("All Users"),
    PASSENGERS("Passengers"),
    STAFF("Staff"),
    ADMINS("Administrators");
    
    private final String displayName;
    
    NotificationAudience(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
