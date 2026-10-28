package com.systemdesign.lld.patterns.cor.example03.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Request Validation Chain of Responsibility Unit Tests")
class RequestValidationChainTest {

    private ApiGateway gateway;

    @BeforeEach
    void setUp() {
        RequestValidator rateLimiter = new IpRateLimitValidator(3);
        RequestValidator auth = new AuthenticationValidator();
        RequestValidator roleAuth = new RoleAuthorizationValidator();
        RequestValidator sanitizer = new PayloadSanitizationValidator();

        rateLimiter.setNext(auth).setNext(roleAuth).setNext(sanitizer);
        gateway = new ApiGateway(rateLimiter);
    }

    @Test
    @DisplayName("Should pass completely through all validators when all parameters are valid")
    void testAllValidatorsPass() {
        HttpRequest req = new HttpRequest(
                "/admin/metrics", "192.168.1.1", "Bearer valid-token-999", "{\"filter\": \"all\"}", "ADMIN"
        );

        ValidationResult result = gateway.handleRequest(req);

        assertTrue(result.valid());
        assertEquals(200, result.httpStatusCode());
        assertEquals("CHAIN_COMPLETED", result.validatorName());
    }

    @Test
    @DisplayName("Should short-circuit at AuthenticationValidator (401) if Bearer token is invalid")
    void testAuthenticationShortCircuit() {
        HttpRequest req = new HttpRequest(
                "/api/orders", "192.168.1.2", "invalid-token", "{}", "USER"
        );

        ValidationResult result = gateway.handleRequest(req);

        assertFalse(result.valid());
        assertEquals(401, result.httpStatusCode());
        assertEquals("AuthenticationValidator", result.validatorName());
    }

    @Test
    @DisplayName("Should short-circuit at RoleAuthorizationValidator (403) if role is insufficient")
    void testRoleAuthorizationShortCircuit() {
        HttpRequest req = new HttpRequest(
                "/admin/settings", "192.168.1.3", "Bearer valid-token-111", "{}", "CUSTOMER"
        );

        ValidationResult result = gateway.handleRequest(req);

        assertFalse(result.valid());
        assertEquals(403, result.httpStatusCode());
        assertEquals("RoleAuthorizationValidator", result.validatorName());
    }

    @Test
    @DisplayName("Should short-circuit at PayloadSanitizationValidator (400) if script injection is present")
    void testSanitizationShortCircuit() {
        HttpRequest req = new HttpRequest(
                "/api/comments", "192.168.1.4", "Bearer valid-token-111", "Hello <script>stealCookies()</script>", "USER"
        );

        ValidationResult result = gateway.handleRequest(req);

        assertFalse(result.valid());
        assertEquals(400, result.httpStatusCode());
        assertEquals("PayloadSanitizationValidator", result.validatorName());
    }

    @Test
    @DisplayName("Should short-circuit at IpRateLimitValidator (429) when IP request limit is exceeded")
    void testRateLimitShortCircuit() {
        HttpRequest req = new HttpRequest(
                "/api/test", "10.99.99.99", "Bearer valid-token-111", "safe", "USER"
        );

        // Limit is 3 requests
        assertTrue(gateway.handleRequest(req).valid());
        assertTrue(gateway.handleRequest(req).valid());
        assertTrue(gateway.handleRequest(req).valid());

        // 4th request must be rejected
        ValidationResult rejected = gateway.handleRequest(req);
        assertFalse(rejected.valid());
        assertEquals(429, rejected.httpStatusCode());
        assertEquals("IpRateLimitValidator", rejected.validatorName());
    }

    @Test
    @DisplayName("Dynamic Pipeline: Public endpoint chain omitting auth and role validators")
    void testPublicPipeline() {
        // Public pipeline: RateLimit -> Sanitizer
        RequestValidator publicRate = new IpRateLimitValidator(5);
        publicRate.setNext(new PayloadSanitizationValidator());
        ApiGateway publicGateway = new ApiGateway(publicRate);

        // No token provided, but public endpoint passes!
        HttpRequest publicReq = new HttpRequest("/public/health", "10.0.0.1", null, "ping", "ANONYMOUS");
        ValidationResult result = publicGateway.handleRequest(publicReq);

        assertTrue(result.valid());
    }
}
