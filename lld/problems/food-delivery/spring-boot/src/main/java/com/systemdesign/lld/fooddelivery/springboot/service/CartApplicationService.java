package com.systemdesign.lld.fooddelivery.springboot.service;

import com.systemdesign.lld.fooddelivery.springboot.domain.Cart;
import com.systemdesign.lld.fooddelivery.springboot.domain.MenuItem;
import com.systemdesign.lld.fooddelivery.springboot.domain.Restaurant;
import com.systemdesign.lld.fooddelivery.springboot.dto.AddToCartRequest;
import com.systemdesign.lld.fooddelivery.springboot.dto.CartItemResponse;
import com.systemdesign.lld.fooddelivery.springboot.dto.CartResponse;
import com.systemdesign.lld.fooddelivery.springboot.exception.ResourceNotFoundException;
import com.systemdesign.lld.fooddelivery.springboot.repository.CartRepository;
import com.systemdesign.lld.fooddelivery.springboot.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CartApplicationService {
    private final CartRepository cartRepository;
    private final RestaurantRepository restaurantRepository;

    public CartApplicationService(CartRepository cartRepository, RestaurantRepository restaurantRepository) {
        this.cartRepository = Objects.requireNonNull(cartRepository);
        this.restaurantRepository = Objects.requireNonNull(restaurantRepository);
    }

    public CartResponse getCart(String customerId) {
        Cart cart = cartRepository.getOrCreate(customerId);
        return toCartResponse(cart);
    }

    public CartResponse addToCart(String customerId, AddToCartRequest request) {
        Restaurant restaurant = restaurantRepository.findById(request.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + request.restaurantId()));

        MenuItem item = restaurant.getMenu().getItem(request.menuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found: " + request.menuItemId()));

        Cart cart = cartRepository.getOrCreate(customerId);
        cart.addItem(request.restaurantId(), item, request.quantity());

        return toCartResponse(cart);
    }

    public CartResponse removeItem(String customerId, String menuItemId) {
        Cart cart = cartRepository.getOrCreate(customerId);
        cart.removeItem(menuItemId);
        return toCartResponse(cart);
    }

    public void clearCart(String customerId) {
        Cart cart = cartRepository.getOrCreate(customerId);
        cart.clear();
    }

    private CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> items = cart.getItems().values().stream()
                .map(ci -> new CartItemResponse(
                        ci.getItem().getId(),
                        ci.getItem().getName(),
                        ci.getItem().getPrice(),
                        ci.getQuantity(),
                        ci.getSubtotal()
                ))
                .toList();

        return new CartResponse(
                cart.getCustomerId(),
                cart.getRestaurantId(),
                items,
                cart.getSubtotal(),
                cart.isEmpty()
        );
    }
}
