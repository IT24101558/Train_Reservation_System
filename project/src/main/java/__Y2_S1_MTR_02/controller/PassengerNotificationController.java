package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.dto.NotificationDTO;
import __Y2_S1_MTR_02.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger/notifications")
@CrossOrigin(origins = "*")
public class PassengerNotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    // Get all active notifications for passengers
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getActiveNotifications() {
        try {
            List<NotificationDTO> notifications = notificationService.getActiveNotificationsForPassengers();
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // Get unread notification count for passengers
    @GetMapping("/count")
    public ResponseEntity<Long> getUnreadNotificationCount() {
        try {
            long count = notificationService.getUnreadNotificationCountForPassengers();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // Get notification summary for passengers (count + recent notifications)
    @GetMapping("/summary")
    public ResponseEntity<NotificationSummary> getNotificationSummary() {
        try {
            List<NotificationDTO> notifications = notificationService.getActiveNotificationsForPassengers();
            long unreadCount = notificationService.getUnreadNotificationCountForPassengers();
            
            NotificationSummary summary = new NotificationSummary();
            summary.setUnreadCount(unreadCount);
            summary.setNotifications(notifications);
            
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // Inner class for notification summary
    public static class NotificationSummary {
        private long unreadCount;
        private List<NotificationDTO> notifications;
        
        public long getUnreadCount() {
            return unreadCount;
        }
        
        public void setUnreadCount(long unreadCount) {
            this.unreadCount = unreadCount;
        }
        
        public List<NotificationDTO> getNotifications() {
            return notifications;
        }
        
        public void setNotifications(List<NotificationDTO> notifications) {
            this.notifications = notifications;
        }
    }
}
