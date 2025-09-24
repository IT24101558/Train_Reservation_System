# Notification System Documentation

## Overview
This notification system provides a complete backend solution for managing notifications in the SLRailE train reservation system. It includes both admin and passenger interfaces with full CRUD operations.

## Features

### Admin Features
- Create new notifications with title, message, type, audience, priority, and expiry date
- View all notifications (including inactive and expired)
- Edit existing notifications
- Delete notifications
- Toggle notification active status
- Clean up expired notifications

### Passenger Features
- View active notifications relevant to passengers
- See unread notification count
- Mark notifications as read
- Real-time updates when admin adds/modifies notifications

## Database Schema

### Notifications Table
```sql
CREATE TABLE notifications (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    title NVARCHAR(200) NOT NULL,
    message NVARCHAR(1000) NOT NULL,
    type NVARCHAR(50) NOT NULL,
    audience NVARCHAR(50) NOT NULL,
    priority NVARCHAR(50) NOT NULL,
    created_at DATETIME2 NOT NULL,
    expires_at DATETIME2,
    is_active BIT NOT NULL DEFAULT 1
);
```

## API Endpoints

### Admin Endpoints
- `GET /api/admin/notifications` - Get all notifications
- `GET /api/admin/notifications/{id}` - Get notification by ID
- `POST /api/admin/notifications` - Create new notification
- `PUT /api/admin/notifications/{id}` - Update notification
- `DELETE /api/admin/notifications/{id}` - Delete notification
- `PATCH /api/admin/notifications/{id}/toggle-status` - Toggle active status
- `POST /api/admin/notifications/cleanup` - Clean up expired notifications

### Passenger Endpoints
- `GET /api/passenger/notifications` - Get active notifications for passengers
- `GET /api/passenger/notifications/count` - Get unread notification count
- `GET /api/passenger/notifications/summary` - Get notification summary

## Notification Types
- **SYSTEM** - System-related notifications
- **ALERT** - Important alerts and warnings
- **MAINTENANCE** - Maintenance notifications
- **UPDATE** - Update notifications
- **INFO** - General information

## Notification Audiences
- **ALL** - All users (passengers, staff, admins)
- **PASSENGERS** - Only passengers
- **STAFF** - Only staff members
- **ADMINS** - Only administrators

## Notification Priorities
- **LOW** - Low priority
- **MEDIUM** - Medium priority
- **HIGH** - High priority
- **URGENT** - Urgent priority

## Usage

### For Admins
1. Navigate to the admin notification page
2. Fill in the form to create a new notification
3. Select appropriate type, audience, and priority
4. Set expiry date if needed
5. Click "Add Notification" to save

### For Passengers
1. Navigate to the passenger notification page
2. View all active notifications relevant to passengers
3. Mark notifications as read by clicking the "✕" button
4. Use "Mark All as Read" to mark all notifications as read

## File Structure
```
src/main/java/__Y2_S1_MTR_02/
├── model/
│   ├── Notification.java
│   ├── NotificationType.java
│   ├── NotificationAudience.java
│   └── NotificationPriority.java
├── repository/
│   └── NotificationRepository.java
├── service/
│   └── NotificationService.java
├── controller/
│   ├── AdminNotificationController.java
│   └── PassengerNotificationController.java
└── dto/
    ├── NotificationDTO.java
    └── CreateNotificationRequest.java
```

## Frontend Integration
- Admin interface: `src/main/resources/static/templetes/IT24300073/HTML/admin-notification.html`
- Passenger interface: `src/main/resources/static/templetes/IT24300073/HTML/notification.html`

Both interfaces are fully integrated with the backend API and provide real-time updates.

## Testing
To test the system:
1. Start the Spring Boot application
2. Navigate to the admin notification page
3. Create a notification with audience "ALL" or "PASSENGERS"
4. Navigate to the passenger notification page to see the notification
5. Test edit and delete operations from the admin interface

## Notes
- Notifications are automatically filtered based on audience
- Expired notifications are not shown to passengers
- The system supports real-time updates when notifications are added/modified
- All API endpoints include proper error handling and validation
