package com.nurtore.notification_spring.repository;

import com.nurtore.notification_spring.model.Document;
import com.nurtore.notification_spring.model.DocumentCategory;
import com.nurtore.notification_spring.model.DocumentStatus;
import com.nurtore.notification_spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    List<Document> findByOwner(User owner);
    List<Document> findByStatus(DocumentStatus status);
    List<Document> findByCategory(DocumentCategory category);
    
    @Query("SELECT d FROM Document d WHERE d.expirationDate <= :expirationDate AND d.status = 'ACTIVE'")
    List<Document> findExpiringDocuments(LocalDate expirationDate);
    
    @Query("SELECT d FROM Document d WHERE d.expirationDate BETWEEN :startDate AND :endDate AND d.status = 'ACTIVE'")
    List<Document> findDocumentsExpiringBetween(LocalDate startDate, LocalDate endDate);
    
    List<Document> findByTitleContainingIgnoreCase(String titlePart);
} 