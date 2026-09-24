package com.systemdesign.lld.fooddelivery.interview.service;

import com.systemdesign.lld.fooddelivery.interview.domain.Order;
import com.systemdesign.lld.fooddelivery.interview.domain.OrderStatus;
import com.systemdesign.lld.fooddelivery.interview.observer.OrderEventPublisher;
import com.systemdesign.lld.fooddelivery.interview.repository.OrderRepository;

import java.util.Objects;
import java.util.Optional;

/*
 * Design Intent:
 * OrderFulfillmentService coordinates kitchen preparation states and order cancellations.
 * Emits state transition notifications via OrderEventPublisher.
 */
public class OrderFulfillmentService {
    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    public OrderFulfillmentService(OrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "OrderEventPublisher cannot be null");
    }

    public void confirmOrder(String orderId) {
        transition(orderId, OrderStatus.CONFIRMED);
    }

    public void startPreparing(String orderId) {
        transition(orderId, OrderStatus.PREPARING);
    }

    public void markReadyForPickup(String orderId) {
        transition(orderId, OrderStatus.READY_FOR_PICKUP);
    }

    public void cancelOrder(String orderId) {
        transition(orderId, OrderStatus.CANCELLED);
    }

    private void transition(String orderId, OrderStatus nextStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        OrderStatus previous = order.getStatus();
        order.transitionTo(nextStatus);
        eventPublisher.publishStatusChange(order, previous, nextStatus);
    }

    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }
}
