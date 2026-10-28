package com.systemdesign.lld.patterns.cor.example03.good;

public class GatewayDemo {
    public static void main(String[] args) {
        // Assemble validation filter chain:
        // RateLimit -> Auth -> RoleAuth -> Sanitizer
        RequestValidator rateLimiter = new IpRateLimitValidator(3);
        RequestValidator auth = new AuthenticationValidator();
        RequestValidator roleAuth = new RoleAuthorizationValidator();
        RequestValidator sanitizer = new PayloadSanitizationValidator();

        rateLimiter.setNext(auth).setNext(roleAuth).setNext(sanitizer);

        ApiGateway gateway = new ApiGateway(rateLimiter);

        // 1. Valid Admin Request
        HttpRequest validAdmin = new HttpRequest("/admin/dashboard", "10.0.0.1", "Bearer valid-token-abc", "{\"action\": \"refresh\"}", "ADMIN");
        System.out.println(gateway.handleRequest(validAdmin) + "\n");

        // 2. Forbidden (Role check fails)
        HttpRequest forbidden = new HttpRequest("/admin/users", "10.0.0.2", "Bearer valid-token-xyz", "{}", "GUEST");
        System.out.println(gateway.handleRequest(forbidden) + "\n");

        // 3. XSS Attack (Sanitizer fails)
        HttpRequest xssAttack = new HttpRequest("/api/comments", "10.0.0.3", "Bearer valid-token-123", "<script>alert('pwned')</script>", "USER");
        System.out.println(gateway.handleRequest(xssAttack) + "\n");
    }
}
