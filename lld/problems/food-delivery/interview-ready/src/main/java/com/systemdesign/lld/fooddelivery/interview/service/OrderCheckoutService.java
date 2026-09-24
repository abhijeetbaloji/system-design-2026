package com.systemdesign.lld.fooddelivery.interview.service;

import com.systemdesign.lld.fooddelivery.interview.domain.*;
import com.systemdesign.lld.fooddelivery.interview.exception.PaymentFailedException;
import com.systemdesign.lld.fooddelivery.interview.observer.OrderEventPublisher;
import com.systemdesign.lld.fooddelivery.interview.payment.PaymentMethod;
import com.systemdesign.lld.fooddelivery.interview.payment.PaymentProcessor;
import com.systemdesign.lld.fooddelivery.interview.payment.PaymentProcessorFactory;
import com.systemdesign.lld.fooddelivery.interview.payment.PaymentResult;
import com.systemdesign.lld.fooddelivery.interview.repository.OrderRepository;
import com.systemdesign.lld.fooddelivery.interview.repository.RestaurantRepository;
import com.systemdesign.lld.fooddelivery.interview.strategy.discount.DiscountStrategy;
import com.systemdesign.lld.fooddelivery.interview.strategy.discount.NoDiscountStrategy;
import com.systemdesign.lld.fooddelivery.interview.strategy.fee.DeliveryFeeStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/*
 * Design Intent:
 * OrderCheckoutService orchestrates order placement.
 * Uses constructor injection to depend on:
 * - DeliveryFeeStrategy (computes distance/surge delivery fees)
 * - DiscountStrategy (evaluates marketing coupon discounts)
 * - PaymentProcessorFactory (resolves payment method processors)
 * - OrderEventPublisher (broadcasts status changes to registered observers)
 */
public class OrderCheckoutService {
    private final RestaurantRepository restaurantRepository;
    private final OrderRepository orderRepository;
    private final DeliveryFeeStrategy deliveryFeeStrategy;
    private final PaymentProcessorFactory paymentProcessorFactory;
    private final OrderEventPublisher eventPublisher;

    private static final double TAX_RATE = 0.05; // 5% tax

    public OrderCheckoutService(RestaurantRepository restaurantRepository,
                                OrderRepository orderRepository,
                                DeliveryFeeStrategy deliveryFeeStrategy,
                                PaymentProcessorFactory paymentProcessorFactory,
                                OrderEventPublisher eventPublisher) {
        this.restaurantRepository = Objects.requireNonNull(restaurantRepository, "RestaurantRepository cannot be null");
        this.orderRepository = Objects.requireNonNull(orderRepository, "OrderRepository cannot be null");
        this.deliveryFeeStrategy = Objects.requireNonNull(deliveryFeeStrategy, "DeliveryFeeStrategy cannot be null");
        this.paymentProcessorFactory = Objects.requireNonNull(paymentProcessorFactory, "PaymentProcessorFactory cannot be null");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "OrderEventPublisher cannot be null");
    }

    public Order checkout(Cart cart, Customer customer, PaymentMethod paymentMethod,
                         String paymentDetail, DiscountStrategy discountStrategy) {
        Objects.requireNonNull(cart, "Cart cannot be null");
        Objects.requireNonNull(customer, "Customer cannot be null");
        Objects.requireNonNull(paymentMethod, "PaymentMethod cannot be null");

        DiscountStrategy activeDiscount = discountStrategy != null ? discountStrategy : new NoDiscountStrategy();

        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot checkout with an empty cart");
        }

        String restaurantId = cart.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));

        if (!restaurant.isActive()) {
            throw new IllegalStateException("Restaurant is currently inactive: " + restaurant.getName());
        }

        // 1. Calculate Financial Breakdown using Injected Strategies
        double subtotal = cart.getSubtotal();
        double deliveryFee = deliveryFeeStrategy.calculateFee(restaurant.getLocation(), customer.location(), subtotal);
        double discount = activeDiscount.calculateDiscount(subtotal);
        OrderBill bill = OrderBill.of(subtotal, deliveryFee, discount, TAX_RATE);

        // 2. Process Payment via PaymentProcessor resolved from Factory
        PaymentProcessor processor = paymentProcessorFactory.getProcessor(paymentMethod);
        PaymentResult result = processor.process(bill.totalAmount(), paymentDetail);
        if (!result.successful()) {
            throw new PaymentFailedException("Payment failed: " + result.message());
        }

        // 3. Snapshot Order Items Immutably
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cart.getItems().values()) {
            orderItems.add(new OrderItem(
                    ci.getItem().getId(),
                    ci.getItem().getName(),
                    ci.getItem().getPrice(),
                    ci.getQuantity()
            ));
        }

        // 4. Create and Save Order
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(orderId, customer.id(), restaurantId, orderItems, bill);
        orderRepository.save(order);

        // 5. Clear Cart post checkout
        cart.clear();

        // 6. Notify Observers (PLACED event)
        eventPublisher.publishStatusChange(order, null, OrderStatus.PLACED);

        return order;
    }
}
