package com.systemdesign.lld.googledocs.dto;

import com.systemdesign.lld.googledocs.domain.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ShareDocumentRequest(
        @NotBlank(message = "Actor ID is required") String actorId,
        @NotBlank(message = "Target user ID is required") String targetUserId,
        @NotNull(message = "Role is required (OWNER, EDITOR, VIEWER)") Role role
) {}
