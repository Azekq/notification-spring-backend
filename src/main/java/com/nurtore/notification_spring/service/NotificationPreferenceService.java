package com.nurtore.notification_spring.service;

import com.nurtore.notification_spring.model.NotificationChannel;
import com.nurtore.notification_spring.model.NotificationPreference;
import com.nurtore.notification_spring.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationPreferenceService {
    NotificationPreference createPreference(NotificationPreference preference);
    NotificationPreference updatePreference(NotificationPreference preference);
    Optional<NotificationPreference> getPreferenceById(UUID id);
    List<NotificationPreference> getPreferencesByUser(User user);
    Optional<NotificationPreference> getPreferenceByUserAndChannel(User user, NotificationChannel channel);
    List<NotificationPreference> getEnabledPreferencesByUser(User user);
    void deletePreference(UUID id);
    NotificationPreference togglePreference(UUID id, boolean enabled);
    List<NotificationPreference> getPreferencesByChannel(NotificationChannel channel);
} 