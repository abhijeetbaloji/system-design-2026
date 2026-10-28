package com.systemdesign.lld.patterns.facade.example02.good;

import java.util.List;

public record CheckoutRequest(
        String customerId,
        String customerEmail,
        String shippingAddress,
        List<CartItem> items,
        String paymentToken
) {}
