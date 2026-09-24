package com.systemdesign.lld.googledocs.dto;

import com.systemdesign.lld.googledocs.domain.model.Role;
import java.util.Map;

public record DocumentResponse(
        String documentId,
        String title,
        String ownerId,
        int revision,
        int length,
        String text,
        Map<String, Role> permissions
) {}
