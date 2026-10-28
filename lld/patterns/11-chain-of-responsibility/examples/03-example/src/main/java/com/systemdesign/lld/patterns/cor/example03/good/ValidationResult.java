package com.systemdesign.lld.patterns.cor.example03.good;

public record ValidationResult(
        boolean valid,
        String validatorName,
        String message,
        int httpStatusCode
) {
    public static ValidationResult pass() {
        return new ValidationResult(true, "CHAIN_COMPLETED", "All validation checks passed successfully", 200);
    }

    public static ValidationResult fail(String validator, String reason, int statusCode) {
        return new ValidationResult(false, validator, reason, statusCode);
    }
}
