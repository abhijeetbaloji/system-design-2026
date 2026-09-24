package com.systemdesign.lld.fooddelivery.springboot.dto;

import com.systemdesign.lld.fooddelivery.springboot.domain.FoodCategory;

public record MenuItemResponse(
        String id,
        String name,
        String description,
        double price,
        FoodCategory category,
        boolean available
) {}
