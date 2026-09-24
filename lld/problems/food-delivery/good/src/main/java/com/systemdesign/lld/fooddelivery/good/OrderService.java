package com.systemdesign.lld.fooddelivery.good;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Design Intent:
 * OrderService coordinates order placement, bill calculation, payment processing,
 * and lifecycle transitions.
 * Depends on abstractions (PaymentProcessor) via constructor injection.
 * Creates immutable OrderItem snapshots from CartItem.
 */
public class OrderService {
    private final RestaurantService restaurantService;
    private final PaymentProcessor paymentProcessor;
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    private static final double BASE_DELIVERY_FEE = 20.0;
    private static final double PER_KM_FEE = 5.0;
    private static final double TAX_RATE = 0.05; // 5% GST/tax

    public OrderService(RestaurantService restaurantService, PaymentProcessor paymentProcessor) {
        this.restaurantService = Objects.requireNonNull(restaurantService, "RestaurantService cannot be null");
        this.paymentProcessor = Objects.requireNonNull(paymentProcessor, "PaymentProcessor cannot be null");
    }

    public Order placeOrder(Cart cart, Customer customer, String paymentDetail) {
        Objects.requireNonNull(cart, "Cart cannot be null");
        Objects.requireNonNull(customer, "Customer cannot be null");

        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot place order with an empty cart");
        }

        String restaurantId = cart.getRestaurantId();
        Restaurant restaurant = restaurantService.getRestaurant(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId));

        if (!restaurant.isActive()) {
            throw new IllegalStateException("Restaurant is currently inactive: " + restaurant.getName());
        }

        // 1. Calculate Delivery Fee & Bill
        double distance = restaurant.getLocation().distanceTo(customer.location());
        double deliveryFee = BASE_DELIVERY_FEE + (distance * PER_KM_FEE);
        double subtotal = cart.getSubtotal();
        OrderBill bill = OrderBill.of(subtotal, deliveryFee, TAX_RATE);

        // 2. Authorize Payment via PaymentProcessor abstraction
        PaymentResult result = paymentProcessor.process(bill.totalAmount(), paymentDetail);
        if (!result.successful()) {
            throw new PaymentFailedException("Payment failed: " + result.message());
        }

        // 3. Snapshot Order Items immutably
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cart.getItems().values()) {
            orderItems.add(new OrderItem(
                    ci.getItem().getId(),
                    ci.getItem().getName(),
                    ci.getItem().getPrice(),
                    ci.getQuantity()
            ));
        }

        // 4. Create and store Order
        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(orderId, customer.id(), restaurantId, orderItems, bill);
        orders.put(orderId, order);

        // 5. Clear Cart post checkout
        cart.clear();

        return order;
    }

    public void updateOrderStatus(String orderId, OrderStatus nextStatus) {
        Order order = orders.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        order.transitionTo(nextStatus);
    }

    public void cancelOrder(String orderId) {
        updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    public Optional<Order> getOrder(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }
}
