package com.nurtore.notification_spring.repository;

import com.nurtore.notification_spring.model.Document;
import com.nurtore.notification_spring.model.DocumentAssignment;
import com.nurtore.notification_spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentAssignmentRepository extends JpaRepository<DocumentAssignment, UUID> {
    List<DocumentAssignment> findByUser(User user);
    List<DocumentAssignment> findByDocument(Document document);
    Optional<DocumentAssignment> findByDocumentAndUser(Document document, User user);
    List<DocumentAssignment> findByUserAndIsPrimaryTrue(User user);
    void deleteByDocumentAndUser(Document document, User user);
} 