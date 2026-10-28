package com.systemdesign.lld.patterns.cor.example03.good;

public class PayloadSanitizationValidator extends RequestValidator {

    @Override
    public ValidationResult validate(HttpRequest request) {
        if (request == null) return ValidationResult.fail("PayloadSanitizationValidator", "Null request", 400);

        String payload = request.payloadBody();
        if (payload != null) {
            String lower = payload.toLowerCase();
            if (lower.contains("<script>") || lower.contains("drop table") || lower.contains("javascript:")) {
                System.err.println("[PayloadSanitizationValidator] 400 Bad Request: Malicious script/SQL token detected!");
                return ValidationResult.fail("PayloadSanitizationValidator", "Malicious input detected in payload", 400);
            }
        }

        System.out.println("[PayloadSanitizationValidator] Payload sanitized and safe.");
        return validateNext(request);
    }
}
