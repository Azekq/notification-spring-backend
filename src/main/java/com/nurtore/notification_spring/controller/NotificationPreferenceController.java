package com.nurtore.notification_spring.controller;

import com.nurtore.notification_spring.model.NotificationChannel;
import com.nurtore.notification_spring.model.NotificationPreference;
import com.nurtore.notification_spring.service.NotificationPreferenceService;
import com.nurtore.notification_spring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notification-preferences")
@RequiredArgsConstructor
public class NotificationPreferenceController {
    private final NotificationPreferenceService preferenceService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN') or #preference.user.id == authentication.principal.id")
    public ResponseEntity<NotificationPreference> createPreference(@Valid @RequestBody NotificationPreference preference) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(preferenceService.createPreference(preference));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @preferencePermissionEvaluator.isOwner(#id, authentication.principal)")
    public ResponseEntity<NotificationPreference> updatePreference(
            @PathVariable UUID id,
            @Valid @RequestBody NotificationPreference preference) {
        preference.setId(id);
        return ResponseEntity.ok(preferenceService.updatePreference(preference));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @preferencePermissionEvaluator.isOwner(#id, authentication.principal)")
    public ResponseEntity<NotificationPreference> getPreferenceById(@PathVariable UUID id) {
        return preferenceService.getPreferenceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<NotificationPreference>> getPreferencesByUser(@PathVariable UUID userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(preferenceService.getPreferencesByUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/channel/{channel}")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<NotificationPreference> getPreferenceByUserAndChannel(
            @PathVariable UUID userId,
            @PathVariable NotificationChannel channel) {
        return userService.getUserById(userId)
                .flatMap(user -> preferenceService.getPreferenceByUserAndChannel(user, channel))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}/enabled")
    @PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
    public ResponseEntity<List<NotificationPreference>> getEnabledPreferencesByUser(@PathVariable UUID userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(preferenceService.getEnabledPreferencesByUser(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @preferencePermissionEvaluator.isOwner(#id, authentication.principal)")
    public ResponseEntity<Void> deletePreference(@PathVariable UUID id) {
        preferenceService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN') or @preferencePermissionEvaluator.isOwner(#id, authentication.principal)")
    public ResponseEntity<NotificationPreference> togglePreference(
            @PathVariable UUID id,
            @RequestParam boolean enabled) {
        return ResponseEntity.ok(preferenceService.togglePreference(id, enabled));
    }

    @GetMapping("/channel/{channel}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationPreference>> getPreferencesByChannel(
            @PathVariable NotificationChannel channel) {
        return ResponseEntity.ok(preferenceService.getPreferencesByChannel(channel));
    }
} 