package com.systemdesign.lld.patterns.cor.example03.bad;

import java.util.HashMap;
import java.util.Map;

/**
 * BAD DESIGN:
 * Monolithic gateway validator checking rate limits, auth tokens, role permissions,
 * and payload security in one giant method.
 *
 * Flaws:
 * 1. Violates Single Responsibility Principle (SRP):
 *    IP throttling, token verification, role checks, and SQL/XSS sanitization are all mashed together.
 * 2. Inflexible Pipeline:
 *    Public APIs cannot easily skip the token check without adding complex boolean flags.
 * 3. Violates Open/Closed Principle (OCP):
 *    Adding CORS or API key validation requires modifying this class.
 */
public class BadApiGatewayValidator {

    private final Map<String, Integer> ipRequestCounts = new HashMap<>();

    public boolean validateRequest(HttpRequest request) {
        if (request == null) return false;

        // 1. IP Rate Limiting
        int count = ipRequestCounts.getOrDefault(request.ipAddress(), 0);
        if (count >= 5) {
            System.err.println("REJECTED: Rate limit exceeded for IP: " + request.ipAddress());
            return false;
        }
        ipRequestCounts.put(request.ipAddress(), count + 1);

        // 2. Authentication check
        if (request.authToken() == null || !request.authToken().startsWith("Bearer valid-token")) {
            System.err.println("REJECTED: Missing or invalid Bearer token");
            return false;
        }

        // 3. Authorization check
        if (request.path().startsWith("/admin") && !"ADMIN".equalsIgnoreCase(request.userRole())) {
            System.err.println("REJECTED: Insufficient permissions for path: " + request.path());
            return false;
        }

        // 4. Payload Sanitization check
        if (request.payloadBody() != null) {
            String lower = request.payloadBody().toLowerCase();
            if (lower.contains("<script>") || lower.contains("drop table")) {
                System.err.println("REJECTED: Malicious payload detected");
                return false;
            }
        }

        System.out.println("ACCEPTED: Request passed all checks.");
        return true;
    }
}
