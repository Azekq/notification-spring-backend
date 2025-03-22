package com.nurtore.notification_spring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "notification_preferences")
public class NotificationPreference {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationChannel channel;

    @ElementCollection
    @CollectionTable(name = "notification_lead_days", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "lead_days")
    private Set<Integer> leadDays;

    @Column(name = "daily_time")
    private LocalTime dailyTime;

    @Column(nullable = false)
    private Boolean enabled = true;
} 