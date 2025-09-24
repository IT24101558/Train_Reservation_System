package __Y2_S1_MTR_02.dto;

import __Y2_S1_MTR_02.model.NotificationAudience;
import __Y2_S1_MTR_02.model.NotificationPriority;
import __Y2_S1_MTR_02.model.NotificationType;

import java.time.LocalDateTime;

public class CreateNotificationRequest {
    private String title;
    private String message;
    private NotificationType type;
    private NotificationAudience audience;
    private NotificationPriority priority;
    private LocalDateTime expiresAt;
    
    // Constructors
    public CreateNotificationRequest() {}
    
    public CreateNotificationRequest(String title, String message, NotificationType type, 
                                   NotificationAudience audience, NotificationPriority priority) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.audience = audience;
        this.priority = priority;
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public NotificationType getType() {
        return type;
    }
    
    public void setType(NotificationType type) {
        this.type = type;
    }
    
    public NotificationAudience getAudience() {
        return audience;
    }
    
    public void setAudience(NotificationAudience audience) {
        this.audience = audience;
    }
    
    public NotificationPriority getPriority() {
        return priority;
    }
    
    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    @Override
    public String toString() {
        return "CreateNotificationRequest{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", type=" + type +
                ", audience=" + audience +
                ", priority=" + priority +
                ", expiresAt=" + expiresAt +
                '}';
    }
}
