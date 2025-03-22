package com.nurtore.notification_spring.repository;

import com.nurtore.notification_spring.model.AuditAction;
import com.nurtore.notification_spring.model.AuditLog;
import com.nurtore.notification_spring.model.Document;
import com.nurtore.notification_spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUser(User user);
    List<AuditLog> findByDocument(Document document);
    List<AuditLog> findByAction(AuditAction action);
    
    @Query("SELECT a FROM AuditLog a WHERE a.timestamp BETWEEN :startDate AND :endDate")
    List<AuditLog> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    List<AuditLog> findByUserAndActionOrderByTimestampDesc(User user, AuditAction action);
    
    @Query("SELECT a FROM AuditLog a WHERE a.document = :document ORDER BY a.timestamp DESC")
    List<AuditLog> findDocumentHistory(Document document);
} 