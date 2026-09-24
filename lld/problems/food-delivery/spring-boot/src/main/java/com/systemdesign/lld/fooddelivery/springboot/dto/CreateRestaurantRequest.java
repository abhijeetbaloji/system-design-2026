package com.systemdesign.lld.fooddelivery.springboot.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRestaurantRequest(
        @NotBlank(message = "Restaurant ID cannot be blank") String id,
        @NotBlank(message = "Name cannot be blank") String name,
        @NotNull(message = "Latitude cannot be null") Double latitude,
        @NotNull(message = "Longitude cannot be null") Double longitude
) {}
