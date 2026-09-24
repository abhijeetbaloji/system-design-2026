package com.systemdesign.lld.fooddelivery.springboot.service;

import com.systemdesign.lld.fooddelivery.springboot.domain.FoodCategory;
import com.systemdesign.lld.fooddelivery.springboot.domain.Location;
import com.systemdesign.lld.fooddelivery.springboot.domain.MenuItem;
import com.systemdesign.lld.fooddelivery.springboot.domain.Restaurant;
import com.systemdesign.lld.fooddelivery.springboot.dto.AddMenuItemRequest;
import com.systemdesign.lld.fooddelivery.springboot.dto.CreateRestaurantRequest;
import com.systemdesign.lld.fooddelivery.springboot.dto.MenuItemResponse;
import com.systemdesign.lld.fooddelivery.springboot.dto.RestaurantResponse;
import com.systemdesign.lld.fooddelivery.springboot.exception.ResourceNotFoundException;
import com.systemdesign.lld.fooddelivery.springboot.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class RestaurantApplicationService {
    private final RestaurantRepository restaurantRepository;

    public RestaurantApplicationService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = Objects.requireNonNull(restaurantRepository);
    }

    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {
        Restaurant restaurant = new Restaurant(
                request.id(),
                request.name(),
                new Location(request.latitude(), request.longitude())
        );
        restaurantRepository.save(restaurant);
        return toResponse(restaurant);
    }

    public RestaurantResponse getRestaurant(String id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));
        return toResponse(restaurant);
    }

    public Collection<RestaurantResponse> getAllActiveRestaurants() {
        return restaurantRepository.findAll().stream()
                .filter(Restaurant::isActive)
                .map(this::toResponse)
                .toList();
    }

    public MenuItemResponse addMenuItem(String restaurantId, AddMenuItemRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));

        MenuItem item = new MenuItem(
                request.id(),
                request.name(),
                request.description(),
                request.price(),
                request.category() != null ? request.category() : FoodCategory.VEG,
                request.available() != null ? request.available() : true
        );
        restaurant.getMenu().addItem(item);
        return toItemResponse(item);
    }

    public List<MenuItemResponse> getMenu(String restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + restaurantId));
        return restaurant.getMenu().getItems().values().stream()
                .map(this::toItemResponse)
                .toList();
    }

    private RestaurantResponse toResponse(Restaurant r) {
        List<MenuItemResponse> menu = r.getMenu().getItems().values().stream()
                .map(this::toItemResponse)
                .toList();
        return new RestaurantResponse(r.getId(), r.getName(), r.getLocation().latitude(),
                r.getLocation().longitude(), r.isActive(), menu);
    }

    private MenuItemResponse toItemResponse(MenuItem i) {
        return new MenuItemResponse(i.getId(), i.getName(), i.getDescription(), i.getPrice(), i.getCategory(), i.isAvailable());
    }
}
