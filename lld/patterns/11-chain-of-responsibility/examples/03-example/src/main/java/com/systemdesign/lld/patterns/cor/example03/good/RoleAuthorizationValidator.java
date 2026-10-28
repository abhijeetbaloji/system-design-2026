package com.systemdesign.lld.patterns.cor.example03.good;

public class RoleAuthorizationValidator extends RequestValidator {

    @Override
    public ValidationResult validate(HttpRequest request) {
        if (request == null) return ValidationResult.fail("RoleAuthorizationValidator", "Null request", 400);

        if (request.path().startsWith("/admin") && !"ADMIN".equalsIgnoreCase(request.userRole())) {
            System.err.println("[RoleAuthorizationValidator] 403 Forbidden: Admin privileges required for " + request.path());
            return ValidationResult.fail("RoleAuthorizationValidator", "User role '" + request.userRole() + "' not authorized for admin endpoint", 403);
        }

        System.out.println("[RoleAuthorizationValidator] Role '" + request.userRole() + "' authorized for path '" + request.path() + "'");
        return validateNext(request);
    }
}
