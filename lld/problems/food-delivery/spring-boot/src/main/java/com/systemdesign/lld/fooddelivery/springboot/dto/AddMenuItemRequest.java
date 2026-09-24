package com.systemdesign.lld.fooddelivery.springboot.dto;

import com.systemdesign.lld.fooddelivery.springboot.domain.FoodCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddMenuItemRequest(
        @NotBlank(message = "Item ID cannot be blank") String id,
        @NotBlank(message = "Name cannot be blank") String name,
        String description,
        @NotNull(message = "Price cannot be null") @Min(value = 0, message = "Price cannot be negative") Double price,
        FoodCategory category,
        Boolean available
) {}
