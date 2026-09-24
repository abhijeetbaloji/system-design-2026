package com.systemdesign.lld.fooddelivery.springboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(
        @NotBlank(message = "Restaurant ID cannot be blank") String restaurantId,
        @NotBlank(message = "Menu item ID cannot be blank") String menuItemId,
        @NotNull(message = "Quantity cannot be null") @Min(value = 1, message = "Quantity must be at least 1") Integer quantity
) {}
