package com.systemdesign.lld.googledocs.dto;

import java.time.Instant;

public record CommittedOperationResponse(
        int revisionNumber,
        String authorId,
        String operationType,
        int position,
        String text,
        Integer length,
        Instant timestamp
) {}
