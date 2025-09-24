package __Y2_S1_MTR_02.dto;

import __Y2_S1_MTR_02.model.Notification;
import __Y2_S1_MTR_02.model.NotificationAudience;
import __Y2_S1_MTR_02.model.NotificationPriority;
import __Y2_S1_MTR_02.model.NotificationType;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private String title;
    private String message;
    private NotificationType type;
    private NotificationAudience audience;
    private NotificationPriority priority;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private Boolean isActive;
    
    // Constructors
    public NotificationDTO() {}
    
    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.title = notification.getTitle();
        this.message = notification.getMessage();
        this.type = notification.getType();
        this.audience = notification.getAudience();
        this.priority = notification.getPriority();
        this.createdAt = notification.getCreatedAt();
        this.expiresAt = notification.getExpiresAt();
        this.isActive = notification.getIsActive();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
