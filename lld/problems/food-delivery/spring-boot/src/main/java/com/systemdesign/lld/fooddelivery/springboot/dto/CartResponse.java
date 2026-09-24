package com.systemdesign.lld.fooddelivery.springboot.dto;

import java.util.List;

public record CartResponse(
        String customerId,
        String restaurantId,
        List<CartItemResponse> items,
        double subtotal,
        boolean empty
) {}
