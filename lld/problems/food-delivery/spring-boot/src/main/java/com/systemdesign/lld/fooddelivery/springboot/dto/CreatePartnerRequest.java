package com.systemdesign.lld.fooddelivery.springboot.dto;

import com.systemdesign.lld.fooddelivery.springboot.domain.PartnerStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePartnerRequest(
        @NotBlank(message = "Partner ID cannot be blank") String id,
        @NotBlank(message = "Name cannot be blank") String name,
        @NotNull(message = "Latitude cannot be null") Double latitude,
        @NotNull(message = "Longitude cannot be null") Double longitude,
        @DecimalMin(value = "0.0") @DecimalMax(value = "5.0") Double rating
) {}
