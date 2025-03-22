package com.nurtore.notification_spring.security;

import com.nurtore.notification_spring.model.User;
import com.nurtore.notification_spring.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("notificationPermissionEvaluator")
@RequiredArgsConstructor
public class NotificationPermissionEvaluator {
    private final NotificationRepository notificationRepository;

    public boolean hasAccess(UUID notificationId, User user) {
        return notificationRepository.findById(notificationId)
                .map(notification -> notification.getUser().getId().equals(user.getId()))
                .orElse(false);
    }
} 