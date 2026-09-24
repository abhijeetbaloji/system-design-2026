package com.systemdesign.lld.fooddelivery.springboot.dto;

import com.systemdesign.lld.fooddelivery.springboot.domain.OrderStatus;

import java.time.Instant;
import java.util.List;

public record OrderResponse(
        String orderId,
        String customerId,
        String restaurantId,
        List<OrderItemResponse> items,
        OrderBillResponse bill,
        OrderStatus status,
        String deliveryPartnerId,
        Instant createdAt
) {}
