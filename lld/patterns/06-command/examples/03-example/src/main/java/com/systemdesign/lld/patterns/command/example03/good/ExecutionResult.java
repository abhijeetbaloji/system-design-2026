package com.systemdesign.lld.patterns.command.example03.good;

public record ExecutionResult(
        String taskName,
        boolean success,
        String message,
        long executedAtMillis
) {}
