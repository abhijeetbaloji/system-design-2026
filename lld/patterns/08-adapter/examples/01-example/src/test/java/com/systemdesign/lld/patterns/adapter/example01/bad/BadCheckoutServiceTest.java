package com.systemdesign.lld.patterns.adapter.example01.bad;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Bad CheckoutService Unit Tests")
class BadCheckoutServiceTest {

    @Test
    @DisplayName("Should process checkout with legacy bank string flag")
    void testBadCheckoutSuccess() {
        BadCheckoutService service = new BadCheckoutService();
        PaymentOrder order = new PaymentOrder("ORD-1", "C-1", BigDecimal.valueOf(50.00), "ACC-1");
        assertTrue(service.processCheckout(order, "LEGACY_BANK"));
    }

    @Test
    @DisplayName("Should throw unsupported operation for other providers")
    void testUnsupportedProviderThrows() {
        BadCheckoutService service = new BadCheckoutService();
        PaymentOrder order = new PaymentOrder("ORD-2", "C-2", BigDecimal.valueOf(50.00), "ACC-2");
        assertThrows(UnsupportedOperationException.class, () -> service.processCheckout(order, "STRIPE"));
    }
}
