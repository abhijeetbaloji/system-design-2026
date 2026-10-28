package com.systemdesign.lld.patterns.strategy.example03.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad Design: Monolithic PaymentService Tests")
class BadPaymentServiceTest {

    @Test
    @DisplayName("Monolithic service processes valid card map")
    void testValidCardMap() {
        PaymentService service = new PaymentService();
        Map<String, String> creds = Map.of(
            "cardNumber", "4111222233334444",
            "cvv", "123",
            "expiry", "12/28"
        );
        PaymentResult res = service.processPayment(PaymentType.CREDIT_CARD, new BigDecimal("50.00"), creds);
        assertTrue(res.successful());
    }

    @Test
    @DisplayName("Fails cleanly when required credential key is missing in untyped map")
    void testMissingCredentials() {
        PaymentService service = new PaymentService();
        Map<String, String> creds = Map.of(); // empty map missing email
        PaymentResult res = service.processPayment(PaymentType.PAYPAL, new BigDecimal("50.00"), creds);
        assertFalse(res.successful());
    }
}
