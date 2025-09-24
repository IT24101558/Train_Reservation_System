package __Y2_S1_MTR_02.service;

import __Y2_S1_MTR_02.dto.CreateNotificationRequest;
import __Y2_S1_MTR_02.dto.NotificationDTO;
import __Y2_S1_MTR_02.model.Notification;
import __Y2_S1_MTR_02.model.NotificationAudience;
import __Y2_S1_MTR_02.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    // Create a new notification
    public NotificationDTO createNotification(CreateNotificationRequest request) {
        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());
        notification.setAudience(request.getAudience());
        notification.setPriority(request.getPriority());
        notification.setExpiresAt(request.getExpiresAt());
        notification.setIsActive(true);
        
        Notification savedNotification = notificationRepository.save(notification);
        return new NotificationDTO(savedNotification);
    }
    
    // Get all notifications for admin (including inactive and expired)
    public List<NotificationDTO> getAllNotificationsForAdmin() {
        List<Notification> notifications = notificationRepository.findAllForAdmin();
        return notifications.stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
    }
    
    // Get active notifications for passengers
    public List<NotificationDTO> getActiveNotificationsForPassengers() {
        LocalDateTime currentTime = LocalDateTime.now();
        
        // Use simpler query for better performance
        List<Notification> allActiveNotifications = notificationRepository.findAllActiveNotifications();
        
        // Filter for passengers and non-expired notifications
        List<Notification> notifications = allActiveNotifications.stream()
            .filter(n -> n.getAudience() == NotificationAudience.ALL || n.getAudience() == NotificationAudience.PASSENGERS)
            .filter(n -> n.getExpiresAt() == null || n.getExpiresAt().isAfter(currentTime))
            .collect(Collectors.toList());
        
        return notifications.stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
    }
    
    // Get notification by ID
    public Optional<NotificationDTO> getNotificationById(Long id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        return notification.map(NotificationDTO::new);
    }
    
    // Update notification
    public Optional<NotificationDTO> updateNotification(Long id, CreateNotificationRequest request) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setTitle(request.getTitle());
            notification.setMessage(request.getMessage());
            notification.setType(request.getType());
            notification.setAudience(request.getAudience());
            notification.setPriority(request.getPriority());
            notification.setExpiresAt(request.getExpiresAt());
            
            Notification updatedNotification = notificationRepository.save(notification);
            return Optional.of(new NotificationDTO(updatedNotification));
        }
        
        return Optional.empty();
    }
    
    // Delete notification
    public boolean deleteNotification(Long id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // Toggle notification active status
    public Optional<NotificationDTO> toggleNotificationStatus(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setIsActive(!notification.getIsActive());
            Notification updatedNotification = notificationRepository.save(notification);
            return Optional.of(new NotificationDTO(updatedNotification));
        }
        
        return Optional.empty();
    }
    
    // Count unread notifications for passengers
    public long getUnreadNotificationCountForPassengers() {
        LocalDateTime currentTime = LocalDateTime.now();
        return notificationRepository.countUnreadNotificationsForPassengers(
            currentTime, 
            NotificationAudience.ALL, 
            NotificationAudience.PASSENGERS
        );
    }
    
    // Get notifications by audience
    public List<NotificationDTO> getNotificationsByAudience(NotificationAudience audience) {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Notification> notifications = notificationRepository.findActiveNotificationsByAudience(
            currentTime, 
            audience
        );
        return notifications.stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
    }
    
    // Clean up expired notifications (can be called by a scheduled task)
    public void cleanupExpiredNotifications() {
        LocalDateTime currentTime = LocalDateTime.now();
        List<Notification> expiredNotifications = notificationRepository.findExpiredNotifications(currentTime);
        
        // Optionally, you can delete expired notifications or mark them as inactive
        for (Notification notification : expiredNotifications) {
            notification.setIsActive(false);
            notificationRepository.save(notification);
        }
    }
}
