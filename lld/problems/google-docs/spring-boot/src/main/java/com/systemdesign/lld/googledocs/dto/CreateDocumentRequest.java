package com.systemdesign.lld.googledocs.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateDocumentRequest(
        @NotBlank(message = "Document ID is required") String documentId,
        @NotBlank(message = "Title is required") String title,
        @NotBlank(message = "Owner ID is required") String ownerId
) {}
