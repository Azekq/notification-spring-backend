package com.nurtore.notification_spring.service.impl;

import com.nurtore.notification_spring.model.NotificationChannel;
import com.nurtore.notification_spring.model.NotificationPreference;
import com.nurtore.notification_spring.model.User;
import com.nurtore.notification_spring.repository.NotificationPreferenceRepository;
import com.nurtore.notification_spring.service.NotificationPreferenceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationPreferenceServiceImpl implements NotificationPreferenceService {
    private final NotificationPreferenceRepository preferenceRepository;

    @Override
    public NotificationPreference createPreference(NotificationPreference preference) {
        // Check if preference already exists for user and channel
        Optional<NotificationPreference> existingPreference = 
            preferenceRepository.findByUserAndChannel(preference.getUser(), preference.getChannel());
        
        if (existingPreference.isPresent()) {
            throw new IllegalArgumentException(
                "Preference already exists for user and channel: " + preference.getUser().getId() + 
                ", " + preference.getChannel());
        }
        
        // Enable by default if not specified
        if (preference.getEnabled() == null) {
            preference.setEnabled(Boolean.TRUE);
        }
        
        return preferenceRepository.save(preference);
    }

    @Override
    public NotificationPreference updatePreference(NotificationPreference preference) {
        if (!preferenceRepository.existsById(preference.getId())) {
            throw new EntityNotFoundException("Preference not found with id: " + preference.getId());
        }
        return preferenceRepository.save(preference);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationPreference> getPreferenceById(UUID id) {
        return preferenceRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationPreference> getPreferencesByUser(User user) {
        return preferenceRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificationPreference> getPreferenceByUserAndChannel(User user, NotificationChannel channel) {
        return preferenceRepository.findByUserAndChannel(user, channel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationPreference> getEnabledPreferencesByUser(User user) {
        return preferenceRepository.findByUserAndEnabledTrue(user);
    }

    @Override
    public void deletePreference(UUID id) {
        if (!preferenceRepository.existsById(id)) {
            throw new EntityNotFoundException("Preference not found with id: " + id);
        }
        preferenceRepository.deleteById(id);
    }

    @Override
    public NotificationPreference togglePreference(UUID id, boolean enabled) {
        NotificationPreference preference = preferenceRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Preference not found with id: " + id));
        preference.setEnabled(enabled);
        return preferenceRepository.save(preference);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationPreference> getPreferencesByChannel(NotificationChannel channel) {
        return preferenceRepository.findByChannel(channel);
    }
} 