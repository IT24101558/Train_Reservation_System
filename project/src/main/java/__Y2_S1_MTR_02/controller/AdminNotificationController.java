package __Y2_S1_MTR_02.controller;

import __Y2_S1_MTR_02.dto.CreateNotificationRequest;
import __Y2_S1_MTR_02.dto.NotificationDTO;
import __Y2_S1_MTR_02.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/notifications")
@CrossOrigin(origins = "*")
public class AdminNotificationController {
    
    @Autowired
    private NotificationService notificationService;
    
    // Get all notifications for admin
    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        try {
            List<NotificationDTO> notifications = notificationService.getAllNotificationsForAdmin();
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Get notification by ID
    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        try {
            Optional<NotificationDTO> notification = notificationService.getNotificationById(id);
            if (notification.isPresent()) {
                return ResponseEntity.ok(notification.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Create new notification
    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody CreateNotificationRequest request) {
        try {
            // Validate required fields
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getType() == null) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getAudience() == null) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getPriority() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            NotificationDTO createdNotification = notificationService.createNotification(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Update notification
    @PutMapping("/{id}")
    public ResponseEntity<NotificationDTO> updateNotification(@PathVariable Long id, @RequestBody CreateNotificationRequest request) {
        try {
            // Validate required fields
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getType() == null) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getAudience() == null) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getPriority() == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Optional<NotificationDTO> updatedNotification = notificationService.updateNotification(id, request);
            if (updatedNotification.isPresent()) {
                return ResponseEntity.ok(updatedNotification.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Delete notification
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        try {
            boolean deleted = notificationService.deleteNotification(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Toggle notification active status
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<NotificationDTO> toggleNotificationStatus(@PathVariable Long id) {
        try {
            Optional<NotificationDTO> updatedNotification = notificationService.toggleNotificationStatus(id);
            if (updatedNotification.isPresent()) {
                return ResponseEntity.ok(updatedNotification.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // Clean up expired notifications
    @PostMapping("/cleanup")
    public ResponseEntity<Void> cleanupExpiredNotifications() {
        try {
            notificationService.cleanupExpiredNotifications();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
