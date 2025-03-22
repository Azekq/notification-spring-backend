package com.nurtore.notification_spring.repository;

import com.nurtore.notification_spring.model.NotificationChannel;
import com.nurtore.notification_spring.model.NotificationPreference;
import com.nurtore.notification_spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, UUID> {
    List<NotificationPreference> findByUser(User user);
    Optional<NotificationPreference> findByUserAndChannel(User user, NotificationChannel channel);
    List<NotificationPreference> findByUserAndEnabledTrue(User user);
    List<NotificationPreference> findByChannel(NotificationChannel channel);
} 