package com.nurtore.notification_spring.security;

import com.nurtore.notification_spring.model.User;
import com.nurtore.notification_spring.repository.NotificationPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("preferencePermissionEvaluator")
@RequiredArgsConstructor
public class PreferencePermissionEvaluator {
    private final NotificationPreferenceRepository preferenceRepository;

    public boolean isOwner(UUID preferenceId, User user) {
        return preferenceRepository.findById(preferenceId)
                .map(preference -> preference.getUser().getId().equals(user.getId()))
                .orElse(false);
    }
} 