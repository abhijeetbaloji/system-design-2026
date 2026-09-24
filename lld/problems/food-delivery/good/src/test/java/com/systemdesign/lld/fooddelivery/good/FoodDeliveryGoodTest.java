package com.systemdesign.lld.fooddelivery.good;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FoodDeliveryGoodTest {

    private RestaurantService restaurantService;
    private CartService cartService;
    private OrderService orderService;
    private DeliveryService deliveryService;
    private Customer customer;
    private Restaurant pizzaRestaurant;
    private Restaurant burgerRestaurant;

    @BeforeEach
    void setUp() {
        restaurantService = new RestaurantService();
        cartService = new CartService();
        orderService = new OrderService(restaurantService, new UpiPaymentProcessor());
        deliveryService = new DeliveryService(orderService);

        customer = new Customer("CUST-1", "Bob", "bob@example.com", "9999999999", new Location(12.98, 77.60));

        pizzaRestaurant = new Restaurant("REST-1", "Pizza Palace", new Location(12.97, 77.59));
        pizzaRestaurant.getMenu().addItem(new MenuItem("ITEM-1", "Margherita Pizza", "Cheesy classic", 250.0, FoodCategory.VEG, true));
        pizzaRestaurant.getMenu().addItem(new MenuItem("ITEM-2", "Garlic Bread", "Crispy garlic bread", 100.0, FoodCategory.VEG, true));
        restaurantService.registerRestaurant(pizzaRestaurant);

        burgerRestaurant = new Restaurant("REST-2", "Burger Hub", new Location(12.95, 77.58));
        burgerRestaurant.getMenu().addItem(new MenuItem("ITEM-3", "Cheese Burger", "Juicy patty", 150.0, FoodCategory.NON_VEG, true));
        restaurantService.registerRestaurant(burgerRestaurant);

        DeliveryPartner partner1 = new DeliveryPartner("DRV-1", "Alice", new Location(12.975, 77.595));
        DeliveryPartner partner2 = new DeliveryPartner("DRV-2", "Dave", new Location(13.10, 77.80)); // Farther
        deliveryService.registerPartner(partner1);
        deliveryService.registerPartner(partner2);
    }

    @Test
    @DisplayName("Should successfully place order, assign nearest partner, and transition to DELIVERED")
    void testHappyPathEndToEnd() {
        Cart cart = cartService.getOrCreateCart(customer.id());
        MenuItem pizza = pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow();
        MenuItem bread = pizzaRestaurant.getMenu().getItem("ITEM-2").orElseThrow();

        cartService.addItem(customer.id(), pizzaRestaurant.getId(), pizza, 2);
        cartService.addItem(customer.id(), pizzaRestaurant.getId(), bread, 1);

        assertEquals(600.0, cart.getSubtotal());

        Order order = orderService.placeOrder(cart, customer, "bob@upi");

        assertNotNull(order);
        assertEquals(OrderStatus.PLACED, order.getStatus());
        assertEquals(2, order.getItems().size());
        assertTrue(cart.isEmpty()); // Cart cleared after checkout

        // Kitchen workflow
        orderService.updateOrderStatus(order.getOrderId(), OrderStatus.CONFIRMED);
        orderService.updateOrderStatus(order.getOrderId(), OrderStatus.PREPARING);
        orderService.updateOrderStatus(order.getOrderId(), OrderStatus.READY_FOR_PICKUP);

        // Dispatch nearest partner (Alice should be chosen over Dave)
        var partnerOpt = deliveryService.assignNearestAvailablePartner(order.getOrderId(), pizzaRestaurant.getLocation());
        assertTrue(partnerOpt.isPresent());
        assertEquals("DRV-1", partnerOpt.get().getId());
        assertEquals(PartnerStatus.BUSY, partnerOpt.get().getStatus());

        // Delivery workflow
        deliveryService.markOrderPickedUp(partnerOpt.get().getId(), order.getOrderId());
        assertEquals(OrderStatus.OUT_FOR_DELIVERY, order.getStatus());

        deliveryService.markOrderDelivered(partnerOpt.get().getId(), order.getOrderId());
        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertEquals(PartnerStatus.AVAILABLE, partnerOpt.get().getStatus());
    }

    @Test
    @DisplayName("Should preserve immutable order price snapshot when menu item price is modified later")
    void testPriceImmutability() {
        Cart cart = cartService.getOrCreateCart(customer.id());
        MenuItem pizza = pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow();
        cartService.addItem(customer.id(), pizzaRestaurant.getId(), pizza, 1);

        Order order = orderService.placeOrder(cart, customer, "bob@upi");
        double originalTotal = order.getBill().totalAmount();
        double orderItemPrice = order.getItems().getFirst().unitPrice();

        assertEquals(250.0, orderItemPrice);

        // Restaurant manager doubles pizza price to 500
        pizza.setPrice(500.0);

        // Historical order item price and bill are preserved
        assertEquals(250.0, order.getItems().getFirst().unitPrice());
        assertEquals(originalTotal, order.getBill().totalAmount());
    }

    @Test
    @DisplayName("Should reject adding item from a second restaurant into an active cart")
    void testSingleRestaurantCartConstraint() {
        MenuItem pizza = pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow();
        MenuItem burger = burgerRestaurant.getMenu().getItem("ITEM-3").orElseThrow();

        cartService.addItem(customer.id(), pizzaRestaurant.getId(), pizza, 1);

        // Attempting to add burger from Burger Hub should throw RestaurantMismatchException
        assertThrows(RestaurantMismatchException.class, () ->
                cartService.addItem(customer.id(), burgerRestaurant.getId(), burger, 1));
    }

    @Test
    @DisplayName("Should reject invalid order status transitions (e.g. DELIVERED -> CANCELLED)")
    void testInvalidStateTransitions() {
        Cart cart = cartService.getOrCreateCart(customer.id());
        MenuItem pizza = pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow();
        cartService.addItem(customer.id(), pizzaRestaurant.getId(), pizza, 1);
        Order order = orderService.placeOrder(cart, customer, "bob@upi");

        orderService.updateOrderStatus(order.getOrderId(), OrderStatus.CONFIRMED);
        orderService.updateOrderStatus(order.getOrderId(), OrderStatus.PREPARING);

        // Cannot cancel once food cooking (PREPARING) has started!
        assertThrows(InvalidOrderStateException.class, () ->
                orderService.cancelOrder(order.getOrderId()));
    }

    @Test
    @DisplayName("Should throw PaymentFailedException when payment processor rejects payment")
    void testPaymentFailure() {
        Cart cart = cartService.getOrCreateCart(customer.id());
        MenuItem pizza = pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow();
        cartService.addItem(customer.id(), pizzaRestaurant.getId(), pizza, 1);

        // Invalid UPI address without '@'
        assertThrows(PaymentFailedException.class, () ->
                orderService.placeOrder(cart, customer, "invalid-vpa-without-at"));
    }
}
