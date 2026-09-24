package com.systemdesign.lld.fooddelivery.springboot.dto;

public record CartItemResponse(
        String menuItemId,
        String itemName,
        double unitPrice,
        int quantity,
        double subtotal
) {}
