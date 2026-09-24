package com.systemdesign.lld.googledocs.controller;

import com.systemdesign.lld.googledocs.dto.*;
import com.systemdesign.lld.googledocs.service.DocumentAppService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentAppService service;

    public DocumentController(DocumentAppService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DocumentResponse> createDocument(@Valid @RequestBody CreateDocumentRequest request) {
        DocumentResponse response = service.createDocument(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocument(
            @PathVariable("id") String docId,
            @RequestParam("userId") String userId) {
        return ResponseEntity.ok(service.getDocument(docId, userId));
    }

    @PostMapping("/{id}/operations")
    public ResponseEntity<CommittedOperationResponse> applyOperation(
            @PathVariable("id") String docId,
            @Valid @RequestBody OperationRequest request) {
        CommittedOperationResponse response = service.applyOperation(docId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/share")
    public ResponseEntity<Void> shareDocument(
            @PathVariable("id") String docId,
            @Valid @RequestBody ShareDocumentRequest request) {
        service.shareDocument(docId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<String> exportDocument(
            @PathVariable("id") String docId,
            @RequestParam("userId") String userId,
            @RequestParam(value = "format", defaultValue = "TXT") String format) {
        String exported = service.exportDocument(docId, userId, format);
        return ResponseEntity.ok(exported);
    }

    @GetMapping("/{id}/revisions")
    public ResponseEntity<List<CommittedOperationResponse>> getRevisions(
            @PathVariable("id") String docId,
            @RequestParam("userId") String userId,
            @RequestParam(value = "sinceRev", defaultValue = "0") int sinceRev) {
        return ResponseEntity.ok(service.getRevisionsSince(docId, userId, sinceRev));
    }
}
