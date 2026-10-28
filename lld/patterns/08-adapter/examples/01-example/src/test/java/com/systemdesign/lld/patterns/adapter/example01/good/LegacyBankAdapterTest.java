package com.systemdesign.lld.patterns.adapter.example01.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Legacy Bank Adapter Unit Tests")
class LegacyBankAdapterTest {

    private LegacyBankGateway gateway;
    private PaymentProcessor adapter;
    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        gateway = new LegacyBankGateway();
        adapter = new LegacyBankAdapter(gateway, "TERM-991", "SECRET-KEY-XYZ");
        checkoutService = new CheckoutService(adapter);
    }

    @Test
    @DisplayName("Should successfully adapt valid dollar payment to legacy cents debit")
    void testSuccessfulPayment() {
        PaymentResponse response = checkoutService.checkout(
                "ORD-100",
                "CUST-01",
                BigDecimal.valueOf(125.50),
                "ACC-12345"
        );

        assertTrue(response.success());
        assertEquals("ORD-100", response.transactionId());
        assertEquals("LEGACY_TX_OK", response.gatewayReference());
    }

    @Test
    @DisplayName("Should translate insufficient funds (-100) error code properly")
    void testInsufficientFundsTranslation() {
        // Amount over 5,000 dollars exceeds 500,000 cents limit in gateway
        PaymentResponse response = checkoutService.checkout(
                "ORD-101",
                "CUST-02",
                BigDecimal.valueOf(6000.00),
                "ACC-12345"
        );

        assertFalse(response.success());
        assertTrue(response.message().contains("limit exceeded"));
        assertEquals("LEGACY_CODE_-100", response.gatewayReference());
    }

    @Test
    @DisplayName("Should translate invalid credentials (-401) error code")
    void testUnauthorizedTerminal() {
        PaymentProcessor badCredsAdapter = new LegacyBankAdapter(gateway, "WRONG-TERM", "INVALID-KEY");
        CheckoutService badCheckout = new CheckoutService(badCredsAdapter);

        PaymentResponse response = badCheckout.checkout(
                "ORD-102",
                "CUST-03",
                BigDecimal.valueOf(50.00),
                "ACC-12345"
        );

        assertFalse(response.success());
        assertEquals("LEGACY_CODE_-401", response.gatewayReference());
    }

    @Test
    @DisplayName("Should handle null request safely")
    void testNullRequestHandling() {
        PaymentResponse response = adapter.processPayment(null);
        assertFalse(response.success());
    }

    @Test
    @DisplayName("Should work polymorphically with modern Stripe processor")
    void testPolymorphicProcessor() {
        PaymentProcessor stripe = new ModernStripeProcessor();
        CheckoutService stripeCheckout = new CheckoutService(stripe);

        PaymentResponse response = stripeCheckout.checkout(
                "ORD-103",
                "CUST-04",
                BigDecimal.valueOf(88.00),
                "4000-0000-0000-0000"
        );

        assertTrue(response.success());
        assertTrue(response.gatewayReference().startsWith("ch_"));
    }
}
