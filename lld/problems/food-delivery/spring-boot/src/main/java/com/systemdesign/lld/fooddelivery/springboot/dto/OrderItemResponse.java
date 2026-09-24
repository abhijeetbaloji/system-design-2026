package com.systemdesign.lld.fooddelivery.springboot.dto;

public record OrderItemResponse(
        String menuItemId,
        String itemName,
        double unitPrice,
        int quantity,
        double subtotal
) {}
