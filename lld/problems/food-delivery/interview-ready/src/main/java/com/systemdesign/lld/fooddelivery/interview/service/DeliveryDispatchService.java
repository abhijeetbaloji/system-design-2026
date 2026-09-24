package com.systemdesign.lld.fooddelivery.interview.service;

import com.systemdesign.lld.fooddelivery.interview.domain.DeliveryPartner;
import com.systemdesign.lld.fooddelivery.interview.domain.Location;
import com.systemdesign.lld.fooddelivery.interview.domain.Order;
import com.systemdesign.lld.fooddelivery.interview.domain.OrderStatus;
import com.systemdesign.lld.fooddelivery.interview.observer.OrderEventPublisher;
import com.systemdesign.lld.fooddelivery.interview.repository.DeliveryPartnerRepository;
import com.systemdesign.lld.fooddelivery.interview.repository.OrderRepository;
import com.systemdesign.lld.fooddelivery.interview.repository.RestaurantRepository;
import com.systemdesign.lld.fooddelivery.interview.strategy.dispatch.DeliveryPartnerMatchingStrategy;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/*
 * Design Intent:
 * DeliveryDispatchService orchestrates courier matching, dispatching, pickup, and delivery.
 * Uses DeliveryPartnerMatchingStrategy to dynamically select an optimal courier,
 * and OrderEventPublisher to notify all observers as the order travels to the customer.
 */
public class DeliveryDispatchService {
    private final DeliveryPartnerRepository partnerRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final DeliveryPartnerMatchingStrategy matchingStrategy;
    private final OrderEventPublisher eventPublisher;

    public DeliveryDispatchService(DeliveryPartnerRepository partnerRepository,
                                  OrderRepository orderRepository,
                                  RestaurantRepository restaurantRepository,
                                  DeliveryPartnerMatchingStrategy matchingStrategy,
                                  OrderEventPublisher eventPublisher) {
        this.partnerRepository = Objects.requireNonNull(partnerRepository, "DeliveryPartnerRepository cannot be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository cannot be null");
        this.restaurantRepository = Objects.requireNonNull(restaurantRepository, "RestaurantRepository cannot be null");
        this.matchingStrategy = Objects.requireNonNull(matchingStrategy, "DeliveryPartnerMatchingStrategy cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "OrderEventPublisher cannot be null");
    }

    public synchronized Optional<DeliveryPartner> dispatchPartner(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        Location restaurantLocation = restaurantRepository.findById(order.getRestaurantId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + order.getRestaurantId()))
                .getLocation();

        List<DeliveryPartner> available = partnerRepository.findAvailablePartners();
        Optional<DeliveryPartner> matched = matchingStrategy.matchPartner(available, restaurantLocation);

        if (matched.isPresent()) {
            DeliveryPartner partner = matched.get();
            partner.assignOrder(orderId);
            order.assignDeliveryPartner(partner.getId());
            return Optional.of(partner);
        }

        return Optional.empty();
    }

    public void markPickedUp(String partnerId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        OrderStatus prev = order.getStatus();
        order.transitionTo(OrderStatus.OUT_FOR_DELIVERY);
        eventPublisher.publishStatusChange(order, prev, OrderStatus.OUT_FOR_DELIVERY);
    }

    public void markDelivered(String partnerId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        DeliveryPartner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new IllegalArgumentException("Partner not found: " + partnerId));

        OrderStatus prev = order.getStatus();
        order.transitionTo(OrderStatus.DELIVERED);
        partner.completeDelivery();
        eventPublisher.publishStatusChange(order, prev, OrderStatus.DELIVERED);
    }
}
