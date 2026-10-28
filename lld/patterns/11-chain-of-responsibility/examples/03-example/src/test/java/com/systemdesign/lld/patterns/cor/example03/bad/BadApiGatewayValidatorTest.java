package com.systemdesign.lld.patterns.cor.example03.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad ApiGatewayValidator Unit Tests")
class BadApiGatewayValidatorTest {

    @Test
    @DisplayName("Should validate request through monolithic checks")
    void testMonolithicPass() {
        BadApiGatewayValidator validator = new BadApiGatewayValidator();
        HttpRequest req = new HttpRequest("/api/data", "192.168.1.1", "Bearer valid-token", "safe", "USER");
        assertTrue(validator.validateRequest(req));
    }

    @Test
    @DisplayName("Should reject request with bad token")
    void testMonolithicFail() {
        BadApiGatewayValidator validator = new BadApiGatewayValidator();
        HttpRequest req = new HttpRequest("/api/data", "192.168.1.1", "bad-token", "safe", "USER");
        assertFalse(validator.validateRequest(req));
    }
}
