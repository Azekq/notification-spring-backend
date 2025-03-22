package com.nurtore.notification_spring.security;

import com.nurtore.notification_spring.model.Document;
import com.nurtore.notification_spring.model.DocumentAssignment;
import com.nurtore.notification_spring.model.User;
import com.nurtore.notification_spring.repository.DocumentAssignmentRepository;
import com.nurtore.notification_spring.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("documentPermissionEvaluator")
@RequiredArgsConstructor
public class DocumentPermissionEvaluator {
    private final DocumentRepository documentRepository;
    private final DocumentAssignmentRepository assignmentRepository;

    public boolean isOwner(UUID documentId, User user) {
        return documentRepository.findById(documentId)
                .map(document -> document.getOwner().getId().equals(user.getId()))
                .orElse(false);
    }

    public boolean hasAccess(UUID documentId, User user) {
        // Check if user is owner
        if (isOwner(documentId, user)) {
            return true;
        }

        // Check if user is assigned to the document
        return assignmentRepository.findByDocumentAndUser(
                Document.builder().id(documentId).build(),
                user
        ).isPresent();
    }
} 