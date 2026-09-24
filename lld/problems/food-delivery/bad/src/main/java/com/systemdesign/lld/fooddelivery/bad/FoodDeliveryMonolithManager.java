package com.systemdesign.lld.fooddelivery.bad;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * Design Intent (Bad Design):
 * FoodDeliveryMonolithManager is an archetype "God Class" that attempts to do everything:
 * 1. Restaurant catalog & menu management
 * 2. Shopping cart management
 * 3. Order placement & checkout
 * 4. Hardcoded payment branching (switch-case conditional explosion)
 * 5. Hardcoded distance & delivery fee mathematics
 * 6. Hardcoded driver dispatch loop
 * 7. Unchecked order status transitions
 * 8. Direct console printing for notifications (tightly coupled to System.out)
 *
 * This violates SRP, OCP, DIP, and makes isolated unit testing virtually impossible.
 */
public class FoodDeliveryMonolithManager {

    public Map<String, Restaurant> restaurants = new HashMap<>();
    public Map<String, Cart> carts = new HashMap<>();
    public Map<String, Order> orders = new HashMap<>();
    public Map<String, DeliveryPartner> deliveryPartners = new HashMap<>();

    // 1. Restaurant & Menu Management
    public void addRestaurant(String id, String name, double lat, double lon) {
        restaurants.put(id, new Restaurant(id, name, lat, lon));
    }

    public void addMenuItem(String restId, String itemId, String name, double price) {
        Restaurant r = restaurants.get(restId);
        if (r != null) {
            r.items.put(itemId, new MenuItem(itemId, name, price, true));
        }
    }

    public void addDeliveryPartner(String id, String name, double lat, double lon) {
        deliveryPartners.put(id, new DeliveryPartner(id, name, lat, lon, true));
    }

    // 2. Cart Management - Note: Does not validate single restaurant!
    public void addToCart(String customerId, String restId, String itemId, int quantity) {
        Cart cart = carts.computeIfAbsent(customerId, Cart::new);
        Restaurant r = restaurants.get(restId);
        if (r != null && r.items.containsKey(itemId)) {
            MenuItem item = r.items.get(itemId);
            cart.items.add(new CartItem(item, quantity));
        }
    }

    // 3. Place & Pay Order - Monolithic checkout with conditional explosion
    public Order placeOrder(String customerId, String restId, String paymentType, String paymentDetail,
                           double custLat, double custLon) {
        Cart cart = carts.get(customerId);
        if (cart == null || cart.items.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        Restaurant restaurant = restaurants.get(restId);
        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found");
        }

        // Hardcoded Euclidean distance & delivery fee calculation
        double distance = Math.sqrt(Math.pow(restaurant.latitude - custLat, 2) + Math.pow(restaurant.longitude - custLon, 2));
        double deliveryFee = 20.0 + (distance * 5.0);

        double subtotal = cart.calculateSubtotal();
        double total = subtotal + deliveryFee;

        // Hardcoded payment switch: Conditional explosion violating OCP
        boolean paymentSuccess = false;
        if ("UPI".equalsIgnoreCase(paymentType)) {
            if (paymentDetail != null && paymentDetail.contains("@")) {
                paymentSuccess = true;
                System.out.println("Processing UPI payment of " + total + " for VPA: " + paymentDetail);
            }
        } else if ("CREDIT_CARD".equalsIgnoreCase(paymentType)) {
            if (paymentDetail != null && paymentDetail.length() == 16) {
                paymentSuccess = true;
                System.out.println("Processing Credit Card payment of " + total + " for card: " + paymentDetail);
            }
        } else if ("COD".equalsIgnoreCase(paymentType)) {
            paymentSuccess = true;
            System.out.println("Order marked as Cash on Delivery for: " + total);
        } else {
            throw new RuntimeException("Unsupported payment type: " + paymentType);
        }

        if (!paymentSuccess) {
            throw new RuntimeException("Payment authorization failed");
        }

        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(orderId, customerId, restId, cart.items, deliveryFee, total, "SUCCESS");
        orders.put(orderId, order);

        // Clear cart
        cart.items.clear();

        // Direct console notification
        System.out.println("Order " + orderId + " placed successfully. Customer notified via console!");
        return order;
    }

    // 4. Inlined Driver Dispatch: Greedily grabs first available partner
    public boolean assignDeliveryPartner(String orderId) {
        Order order = orders.get(orderId);
        if (order == null) return false;

        for (DeliveryPartner partner : deliveryPartners.values()) {
            if (partner.isAvailable) {
                partner.isAvailable = false;
                order.deliveryPartnerId = partner.id;
                System.out.println("Assigned driver " + partner.name + " to order " + orderId);
                return true;
            }
        }
        System.out.println("No available driver found for order " + orderId);
        return false;
    }

    // 5. Unconstrained status updater: No state transition validation
    public void updateOrderStatus(String orderId, String newStatus) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus(newStatus);
            System.out.println("Order " + orderId + " transitioned to status: " + newStatus);
        }
    }

    // 6. Cancellation without checking preparation status
    public void cancelOrder(String orderId) {
        Order order = orders.get(orderId);
        if (order != null) {
            order.setStatus("CANCELLED");
            System.out.println("Order " + orderId + " cancelled.");
        }
    }
}
