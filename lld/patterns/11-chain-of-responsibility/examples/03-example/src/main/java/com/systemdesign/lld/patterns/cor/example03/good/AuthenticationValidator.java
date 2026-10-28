package com.systemdesign.lld.patterns.cor.example03.good;

public class AuthenticationValidator extends RequestValidator {

    @Override
    public ValidationResult validate(HttpRequest request) {
        if (request == null) return ValidationResult.fail("AuthenticationValidator", "Null request", 400);

        String token = request.authToken();
        if (token == null || !token.startsWith("Bearer valid-token")) {
            System.err.println("[AuthenticationValidator] 401 Unauthorized: Invalid or missing Bearer token");
            return ValidationResult.fail("AuthenticationValidator", "Invalid or missing Bearer authentication token", 401);
        }

        System.out.println("[AuthenticationValidator] Bearer token verified successfully.");
        return validateNext(request);
    }
}
