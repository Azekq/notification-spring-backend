package com.nurtore.notification_spring.controller;

import com.nurtore.notification_spring.model.Document;
import com.nurtore.notification_spring.model.DocumentCategory;
import com.nurtore.notification_spring.model.DocumentStatus;
import com.nurtore.notification_spring.service.DocumentService;
import com.nurtore.notification_spring.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Document> createDocument(@Valid @RequestBody Document document) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.createDocument(document));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @documentPermissionEvaluator.isOwner(#id, authentication.principal)")
    public ResponseEntity<Document> updateDocument(@PathVariable UUID id, @Valid @RequestBody Document document) {
        document.setId(id);
        return ResponseEntity.ok(documentService.updateDocument(document));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @documentPermissionEvaluator.hasAccess(#id, authentication.principal)")
    public ResponseEntity<Document> getDocumentById(@PathVariable UUID id) {
        return documentService.getDocumentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasRole('ADMIN') or #ownerId == authentication.principal.id")
    public ResponseEntity<List<Document>> getDocumentsByOwner(@PathVariable UUID ownerId) {
        return userService.getUserById(ownerId)
                .map(owner -> ResponseEntity.ok(documentService.getDocumentsByOwner(owner)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<Document>> getDocumentsByStatus(@PathVariable DocumentStatus status) {
        return ResponseEntity.ok(documentService.getDocumentsByStatus(status));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<Document>> getDocumentsByCategory(@PathVariable DocumentCategory category) {
        return ResponseEntity.ok(documentService.getDocumentsByCategory(category));
    }

    @GetMapping("/expiring")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<Document>> getExpiringDocuments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expirationDate) {
        return ResponseEntity.ok(documentService.getExpiringDocuments(expirationDate));
    }

    @GetMapping("/expiring/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<Document>> getDocumentsExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(documentService.getDocumentsExpiringBetween(startDate, endDate));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    public ResponseEntity<List<Document>> searchDocumentsByTitle(@RequestParam String title) {
        return ResponseEntity.ok(documentService.searchDocumentsByTitle(title));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @documentPermissionEvaluator.isOwner(#id, authentication.principal)")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER') or @documentPermissionEvaluator.isOwner(#id, authentication.principal)")
    public ResponseEntity<Document> updateDocumentStatus(
            @PathVariable UUID id,
            @RequestParam DocumentStatus status) {
        return ResponseEntity.ok(documentService.updateDocumentStatus(id, status));
    }
} 