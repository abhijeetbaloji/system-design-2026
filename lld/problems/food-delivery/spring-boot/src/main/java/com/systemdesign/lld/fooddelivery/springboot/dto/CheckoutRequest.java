package com.systemdesign.lld.fooddelivery.springboot.dto;

import com.systemdesign.lld.fooddelivery.springboot.payment.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckoutRequest(
        @NotBlank(message = "Customer ID cannot be blank") String customerId,
        @NotNull(message = "Payment method cannot be null") PaymentMethod paymentMethod,
        String paymentDetail,
        String promoCode
) {}
