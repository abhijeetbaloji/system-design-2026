package com.systemdesign.lld.fooddelivery.springboot.dto;

public record OrderBillResponse(
        double subtotal,
        double deliveryFee,
        double discount,
        double tax,
        double totalAmount
) {}
