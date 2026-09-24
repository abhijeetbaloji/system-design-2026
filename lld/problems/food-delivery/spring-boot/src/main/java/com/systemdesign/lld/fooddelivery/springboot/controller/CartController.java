package com.systemdesign.lld.fooddelivery.springboot.controller;

import com.systemdesign.lld.fooddelivery.springboot.dto.AddToCartRequest;
import com.systemdesign.lld.fooddelivery.springboot.dto.CartResponse;
import com.systemdesign.lld.fooddelivery.springboot.service.CartApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    private final CartApplicationService cartService;

    public CartController(CartApplicationService cartService) {
        this.cartService = Objects.requireNonNull(cartService);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponse> getCart(@PathVariable String customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    @PostMapping("/{customerId}/items")
    public ResponseEntity<CartResponse> addToCart(@PathVariable String customerId,
                                                  @Valid @RequestBody AddToCartRequest request) {
        return ResponseEntity.ok(cartService.addToCart(customerId, request));
    }

    @DeleteMapping("/{customerId}/items/{menuItemId}")
    public ResponseEntity<CartResponse> removeItem(@PathVariable String customerId,
                                                   @PathVariable String menuItemId) {
        return ResponseEntity.ok(cartService.removeItem(customerId, menuItemId));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> clearCart(@PathVariable String customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}
