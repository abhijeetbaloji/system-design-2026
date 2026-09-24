package com.systemdesign.lld.fooddelivery.springboot.controller;

import com.systemdesign.lld.fooddelivery.springboot.dto.CreatePartnerRequest;
import com.systemdesign.lld.fooddelivery.springboot.dto.OrderResponse;
import com.systemdesign.lld.fooddelivery.springboot.dto.PartnerResponse;
import com.systemdesign.lld.fooddelivery.springboot.service.DeliveryApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/delivery")
public class DeliveryPartnerController {
    private final DeliveryApplicationService deliveryService;

    public DeliveryPartnerController(DeliveryApplicationService deliveryService) {
        this.deliveryService = Objects.requireNonNull(deliveryService);
    }

    @PostMapping("/partners")
    public ResponseEntity<PartnerResponse> registerPartner(@Valid @RequestBody CreatePartnerRequest request) {
        PartnerResponse response = deliveryService.registerPartner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/dispatch/{orderId}")
    public ResponseEntity<PartnerResponse> dispatchPartner(@PathVariable String orderId) {
        return ResponseEntity.ok(deliveryService.dispatchPartner(orderId));
    }

    @PostMapping("/partners/{partnerId}/orders/{orderId}/pickup")
    public ResponseEntity<OrderResponse> markPickedUp(@PathVariable String partnerId,
                                                      @PathVariable String orderId) {
        return ResponseEntity.ok(deliveryService.markPickedUp(partnerId, orderId));
    }

    @PostMapping("/partners/{partnerId}/orders/{orderId}/deliver")
    public ResponseEntity<OrderResponse> markDelivered(@PathVariable String partnerId,
                                                       @PathVariable String orderId) {
        return ResponseEntity.ok(deliveryService.markDelivered(partnerId, orderId));
    }
}
