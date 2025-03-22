package com.nurtore.notification_spring.repository;

import com.nurtore.notification_spring.model.Document;
import com.nurtore.notification_spring.model.Notification;
import com.nurtore.notification_spring.model.NotificationStatus;
import com.nurtore.notification_spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByUser(User user);
    List<Notification> findByDocument(Document document);
    List<Notification> findByStatus(NotificationStatus status);
    
    @Query("SELECT n FROM Notification n WHERE n.scheduledAt <= :now AND n.status = 'PENDING'")
    List<Notification> findPendingNotificationsDue(LocalDateTime now);
    
    List<Notification> findByUserAndStatusOrderByScheduledAtDesc(User user, NotificationStatus status);
    
    @Query("SELECT n FROM Notification n WHERE n.user = :user AND n.scheduledAt BETWEEN :startDate AND :endDate")
    List<Notification> findUserNotificationsInDateRange(User user, LocalDateTime startDate, LocalDateTime endDate);
} 