package com.nurtore.notification_spring.service.impl;

import com.nurtore.notification_spring.model.Document;
import com.nurtore.notification_spring.model.DocumentCategory;
import com.nurtore.notification_spring.model.DocumentStatus;
import com.nurtore.notification_spring.model.User;
import com.nurtore.notification_spring.repository.DocumentRepository;
import com.nurtore.notification_spring.service.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;

    @Override
    public Document createDocument(Document document) {
        // Set initial status if not set
        if (document.getStatus() == null) {
            document.setStatus(DocumentStatus.ACTIVE);
        }
        return documentRepository.save(document);
    }

    @Override
    public Document updateDocument(Document document) {
        if (!documentRepository.existsById(document.getId())) {
            throw new EntityNotFoundException("Document not found with id: " + document.getId());
        }
        return documentRepository.save(document);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Document> getDocumentById(UUID id) {
        return documentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getDocumentsByOwner(User owner) {
        return documentRepository.findByOwner(owner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getDocumentsByStatus(DocumentStatus status) {
        return documentRepository.findByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getDocumentsByCategory(DocumentCategory category) {
        return documentRepository.findByCategory(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getExpiringDocuments(LocalDate expirationDate) {
        return documentRepository.findExpiringDocuments(expirationDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getDocumentsExpiringBetween(LocalDate startDate, LocalDate endDate) {
        return documentRepository.findDocumentsExpiringBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> searchDocumentsByTitle(String titlePart) {
        return documentRepository.findByTitleContainingIgnoreCase(titlePart);
    }

    @Override
    public void deleteDocument(UUID id) {
        if (!documentRepository.existsById(id)) {
            throw new EntityNotFoundException("Document not found with id: " + id);
        }
        documentRepository.deleteById(id);
    }

    @Override
    public Document updateDocumentStatus(UUID id, DocumentStatus status) {
        Document document = documentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Document not found with id: " + id));
        document.setStatus(status);
        return documentRepository.save(document);
    }
} 