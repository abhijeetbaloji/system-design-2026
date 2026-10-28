package com.systemdesign.lld.patterns.strategy.example03.good;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Good Design: Strategy Pattern Payment Processing Tests")
class PaymentStrategyTest {

    private final BigDecimal amount = new BigDecimal("100.00");

    @Test
    @DisplayName("CreditCardPaymentStrategy succeeds with valid card credentials")
    void testCreditCard_Success() {
        CreditCardPaymentStrategy strategy = new CreditCardPaymentStrategy(
            "4111222233334444", "John Doe", "123", "08/28"
        );
        PaymentResult result = strategy.processPayment(amount);

        assertTrue(result.successful());
        assertNotNull(result.transactionId());
        assertEquals(new BigDecimal("2.00"), result.fee()); // 2% fee
        assertTrue(result.message().contains("John Doe"));
    }

    @Test
    @DisplayName("CreditCardPaymentStrategy rejects invalid card number or CVV")
    void testCreditCard_ValidationRejections() {
        CreditCardPaymentStrategy badCard = new CreditCardPaymentStrategy("123", "John Doe", "123", "08/28");
        PaymentResult res1 = badCard.processPayment(amount);
        assertFalse(res1.successful());

        CreditCardPaymentStrategy badCvv = new CreditCardPaymentStrategy("4111222233334444", "John Doe", "99", "08/28");
        PaymentResult res2 = badCvv.processPayment(amount);
        assertFalse(res2.successful());
    }

    @Test
    @DisplayName("PayPalPaymentStrategy validates email format and token")
    void testPayPal() {
        PayPalPaymentStrategy valid = new PayPalPaymentStrategy("user@example.com", "token_valid_1234");
        PaymentResult res1 = valid.processPayment(amount);
        assertTrue(res1.successful());

        PayPalPaymentStrategy invalidEmail = new PayPalPaymentStrategy("notanemail", "token_valid_1234");
        PaymentResult res2 = invalidEmail.processPayment(amount);
        assertFalse(res2.successful());
    }

    @Test
    @DisplayName("UpiPaymentStrategy validates VPA address and MPIN")
    void testUpi() {
        UpiPaymentStrategy valid = new UpiPaymentStrategy("alice@upi", "1234");
        PaymentResult res1 = valid.processPayment(amount);
        assertTrue(res1.successful());
        assertEquals(BigDecimal.ZERO, res1.fee());

        UpiPaymentStrategy invalidVpa = new UpiPaymentStrategy("badaddress", "1234");
        PaymentResult res2 = invalidVpa.processPayment(amount);
        assertFalse(res2.successful());
    }

    @Test
    @DisplayName("CryptoPaymentStrategy validates wallet address length")
    void testCrypto() {
        CryptoPaymentStrategy valid = new CryptoPaymentStrategy("0x1234567890abcdef1234567890");
        PaymentResult res1 = valid.processPayment(amount);
        assertTrue(res1.successful());

        CryptoPaymentStrategy invalid = new CryptoPaymentStrategy("0xshort");
        PaymentResult res2 = invalid.processPayment(amount);
        assertFalse(res2.successful());
    }

    @Test
    @DisplayName("PaymentContext allows dynamic runtime strategy swapping")
    void testContextRuntimeSwapping() {
        PaymentContext context = new PaymentContext(new UpiPaymentStrategy("bob@okhdfcbank", "4321"));
        PaymentResult res1 = context.pay(amount);
        assertTrue(res1.successful());
        assertTrue(res1.message().contains("UPI"));

        // Swap to Credit Card
        context.setPaymentStrategy(new CreditCardPaymentStrategy("5500112233445566", "Bob Smith", "789", "05/27"));
        PaymentResult res2 = context.pay(amount);
        assertTrue(res2.successful());
        assertTrue(res2.message().contains("Bob Smith"));
    }

    @Test
    @DisplayName("Negative or zero amount throws IllegalArgumentException")
    void testInvalidAmount() {
        PaymentContext context = new PaymentContext(new UpiPaymentStrategy("bob@okhdfcbank", "4321"));
        assertThrows(IllegalArgumentException.class, () -> context.pay(BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> context.pay(new BigDecimal("-10.00")));
    }
}
