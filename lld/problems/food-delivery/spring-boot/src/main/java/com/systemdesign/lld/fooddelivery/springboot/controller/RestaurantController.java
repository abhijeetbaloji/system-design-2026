package com.systemdesign.lld.fooddelivery.springboot.controller;

import com.systemdesign.lld.fooddelivery.springboot.dto.AddMenuItemRequest;
import com.systemdesign.lld.fooddelivery.springboot.dto.CreateRestaurantRequest;
import com.systemdesign.lld.fooddelivery.springboot.dto.MenuItemResponse;
import com.systemdesign.lld.fooddelivery.springboot.dto.RestaurantResponse;
import com.systemdesign.lld.fooddelivery.springboot.service.RestaurantApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {
    private final RestaurantApplicationService restaurantService;

    public RestaurantController(RestaurantApplicationService restaurantService) {
        this.restaurantService = Objects.requireNonNull(restaurantService);
    }

    @PostMapping
    public ResponseEntity<RestaurantResponse> createRestaurant(@Valid @RequestBody CreateRestaurantRequest request) {
        RestaurantResponse response = restaurantService.createRestaurant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Collection<RestaurantResponse>> getAllActiveRestaurants() {
        return ResponseEntity.ok(restaurantService.getAllActiveRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurant(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getRestaurant(id));
    }

    @PostMapping("/{id}/menu-items")
    public ResponseEntity<MenuItemResponse> addMenuItem(@PathVariable String id,
                                                        @Valid @RequestBody AddMenuItemRequest request) {
        MenuItemResponse response = restaurantService.addMenuItem(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuItemResponse>> getMenu(@PathVariable String id) {
        return ResponseEntity.ok(restaurantService.getMenu(id));
    }
}
