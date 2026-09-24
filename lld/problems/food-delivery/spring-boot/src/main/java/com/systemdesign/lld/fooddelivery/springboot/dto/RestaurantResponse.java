package com.systemdesign.lld.fooddelivery.springboot.dto;

import java.util.List;

public record RestaurantResponse(
        String id,
        String name,
        double latitude,
        double longitude,
        boolean active,
        List<MenuItemResponse> menu
) {}
