package com.systemdesign.lld.googledocs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OperationRequest(
        @NotBlank(message = "Operation type is required (INSERT, DELETE, FORMAT)") String type,
        @NotBlank(message = "Author ID is required") String authorId,
        @NotNull(message = "Base revision is required") Integer baseRevision,
        @NotNull(message = "Position is required") Integer position,
        String text,
        Integer length,
        Boolean bold,
        Boolean italic,
        Integer fontSize
) {}
