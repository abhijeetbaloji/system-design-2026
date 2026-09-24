package com.systemdesign.lld.fooddelivery.springboot.dto;

import com.systemdesign.lld.fooddelivery.springboot.domain.PartnerStatus;

public record PartnerResponse(
        String id,
        String name,
        double latitude,
        double longitude,
        double rating,
        PartnerStatus status,
        String currentOrderId
) {}
