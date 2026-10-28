package com.systemdesign.lld.patterns.cor.example03.bad;

public record HttpRequest(
        String path,
        String ipAddress,
        String authToken,
        String payloadBody,
        String userRole
) {}
