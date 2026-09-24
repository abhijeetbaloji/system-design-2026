package com.systemdesign.lld.fooddelivery.springboot.controller;

import com.systemdesign.lld.fooddelivery.springboot.dto.CheckoutRequest;
import com.systemdesign.lld.fooddelivery.springboot.dto.OrderResponse;
import com.systemdesign.lld.fooddelivery.springboot.service.OrderApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderApplicationService orderService;

    public OrderController(OrderApplicationService orderService) {
        this.orderService = Objects.requireNonNull(orderService);
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkout(@Valid @RequestBody CheckoutRequest request) {
        OrderResponse response = orderService.checkout(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @PostMapping("/{orderId}/confirm")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.confirmOrder(orderId));
    }

    @PostMapping("/{orderId}/preparing")
    public ResponseEntity<OrderResponse> startPreparing(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.startPreparing(orderId));
    }

    @PostMapping("/{orderId}/ready-for-pickup")
    public ResponseEntity<OrderResponse> markReadyForPickup(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.markReadyForPickup(orderId));
    }

    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.cancelOrder(orderId));
    }
}
