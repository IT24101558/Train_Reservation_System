package __Y2_S1_MTR_02.repository;

import __Y2_S1_MTR_02.model.Notification;
import __Y2_S1_MTR_02.model.NotificationAudience;
import __Y2_S1_MTR_02.model.NotificationPriority;
import __Y2_S1_MTR_02.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    // Find all active notifications visible to passengers
    @Query("SELECT n FROM Notification n WHERE n.isActive = true " +
           "AND (n.expiresAt IS NULL OR n.expiresAt > :currentTime) " +
           "AND (n.audience = :allAudience OR n.audience = :passengerAudience) " +
           "ORDER BY n.priority DESC, n.createdAt DESC")
    List<Notification> findActiveNotificationsForPassengers(
        @Param("currentTime") LocalDateTime currentTime,
        @Param("allAudience") NotificationAudience allAudience,
        @Param("passengerAudience") NotificationAudience passengerAudience
    );
    
    // Alternative simpler query for debugging
    @Query("SELECT n FROM Notification n WHERE n.isActive = true ORDER BY n.createdAt DESC")
    List<Notification> findAllActiveNotifications();
    
    // Find all notifications for admin (including inactive and expired)
    @Query("SELECT n FROM Notification n ORDER BY n.createdAt DESC")
    List<Notification> findAllForAdmin();
    
    // Find active notifications by audience
    @Query("SELECT n FROM Notification n WHERE n.isActive = true " +
           "AND (n.expiresAt IS NULL OR n.expiresAt > :currentTime) " +
           "AND n.audience = :audience " +
           "ORDER BY n.priority DESC, n.createdAt DESC")
    List<Notification> findActiveNotificationsByAudience(
        @Param("currentTime") LocalDateTime currentTime,
        @Param("audience") NotificationAudience audience
    );
    
    // Count unread notifications for passengers (assuming all are unread for simplicity)
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.isActive = true " +
           "AND (n.expiresAt IS NULL OR n.expiresAt > :currentTime) " +
           "AND (n.audience = :allAudience OR n.audience = :passengerAudience)")
    long countUnreadNotificationsForPassengers(
        @Param("currentTime") LocalDateTime currentTime,
        @Param("allAudience") NotificationAudience allAudience,
        @Param("passengerAudience") NotificationAudience passengerAudience
    );
    
    // Find notifications by priority
    List<Notification> findByPriorityOrderByCreatedAtDesc(NotificationPriority priority);
    
    // Find notifications by type
    List<Notification> findByTypeOrderByCreatedAtDesc(NotificationType type);
    
    // Find expired notifications
    @Query("SELECT n FROM Notification n WHERE n.expiresAt IS NOT NULL AND n.expiresAt < :currentTime")
    List<Notification> findExpiredNotifications(@Param("currentTime") LocalDateTime currentTime);
}
