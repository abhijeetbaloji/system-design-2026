package com.systemdesign.lld.fooddelivery.interview;

import com.systemdesign.lld.fooddelivery.interview.domain.*;
import com.systemdesign.lld.fooddelivery.interview.exception.InvalidOrderStateException;
import com.systemdesign.lld.fooddelivery.interview.exception.PaymentFailedException;
import com.systemdesign.lld.fooddelivery.interview.exception.RestaurantMismatchException;
import com.systemdesign.lld.fooddelivery.interview.observer.*;
import com.systemdesign.lld.fooddelivery.interview.payment.PaymentMethod;
import com.systemdesign.lld.fooddelivery.interview.payment.PaymentProcessorFactory;
import com.systemdesign.lld.fooddelivery.interview.repository.CustomerRepository;
import com.systemdesign.lld.fooddelivery.interview.repository.DeliveryPartnerRepository;
import com.systemdesign.lld.fooddelivery.interview.repository.OrderRepository;
import com.systemdesign.lld.fooddelivery.interview.repository.RestaurantRepository;
import com.systemdesign.lld.fooddelivery.interview.service.DeliveryDispatchService;
import com.systemdesign.lld.fooddelivery.interview.service.OrderCheckoutService;
import com.systemdesign.lld.fooddelivery.interview.service.OrderFulfillmentService;
import com.systemdesign.lld.fooddelivery.interview.strategy.discount.FlatDiscountStrategy;
import com.systemdesign.lld.fooddelivery.interview.strategy.discount.PercentageDiscountStrategy;
import com.systemdesign.lld.fooddelivery.interview.strategy.dispatch.HighestRatedPartnerMatchingStrategy;
import com.systemdesign.lld.fooddelivery.interview.strategy.dispatch.NearestPartnerMatchingStrategy;
import com.systemdesign.lld.fooddelivery.interview.strategy.fee.DistanceBasedDeliveryFeeStrategy;
import com.systemdesign.lld.fooddelivery.interview.strategy.fee.SurgeDeliveryFeeStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FoodDeliveryInterviewTest {

    private RestaurantRepository restaurantRepository;
    private OrderRepository orderRepository;
    private DeliveryPartnerRepository partnerRepository;
    private CustomerRepository customerRepository;

    private OrderEventPublisher eventPublisher;
    private CustomerNotifier customerNotifier;
    private RestaurantNotifier restaurantNotifier;
    private DeliveryPartnerNotifier partnerNotifier;
    private OrderAuditLogger auditLogger;

    private OrderCheckoutService checkoutService;
    private OrderFulfillmentService fulfillmentService;
    private DeliveryDispatchService dispatchService;

    private Customer customer;
    private Restaurant pizzaRestaurant;
    private Restaurant burgerRestaurant;

    @BeforeEach
    void setUp() {
        restaurantRepository = new RestaurantRepository();
        orderRepository = new OrderRepository();
        partnerRepository = new DeliveryPartnerRepository();
        customerRepository = new CustomerRepository();

        eventPublisher = new OrderEventPublisher();
        customerNotifier = new CustomerNotifier();
        restaurantNotifier = new RestaurantNotifier();
        partnerNotifier = new DeliveryPartnerNotifier();
        auditLogger = new OrderAuditLogger();

        eventPublisher.registerObserver(customerNotifier);
        eventPublisher.registerObserver(restaurantNotifier);
        eventPublisher.registerObserver(partnerNotifier);
        eventPublisher.registerObserver(auditLogger);

        PaymentProcessorFactory paymentFactory = new PaymentProcessorFactory();
        DistanceBasedDeliveryFeeStrategy feeStrategy = new DistanceBasedDeliveryFeeStrategy(20.0, 5.0, 1000.0);

        checkoutService = new OrderCheckoutService(
                restaurantRepository, orderRepository, feeStrategy, paymentFactory, eventPublisher);
        fulfillmentService = new OrderFulfillmentService(orderRepository, eventPublisher);
        dispatchService = new DeliveryDispatchService(
                partnerRepository, orderRepository, restaurantRepository,
                new NearestPartnerMatchingStrategy(), eventPublisher);

        customer = new Customer("CUST-1", "Alice", "alice@example.com", "9876543210", new Location(12.98, 77.60));
        customerRepository.save(customer);

        pizzaRestaurant = new Restaurant("REST-1", "Pizza Palace", new Location(12.97, 77.59));
        pizzaRestaurant.getMenu().addItem(new MenuItem("ITEM-1", "Margherita Pizza", "Cheesy", 300.0, FoodCategory.VEG, true));
        pizzaRestaurant.getMenu().addItem(new MenuItem("ITEM-2", "Garlic Bread", "Crispy", 120.0, FoodCategory.VEG, true));
        restaurantRepository.save(pizzaRestaurant);

        burgerRestaurant = new Restaurant("REST-2", "Burger Point", new Location(12.90, 77.50));
        burgerRestaurant.getMenu().addItem(new MenuItem("ITEM-3", "Aloo Tikki Burger", "Spicy", 80.0, FoodCategory.VEG, true));
        restaurantRepository.save(burgerRestaurant);

        partnerRepository.save(new DeliveryPartner("DRV-1", "John", new Location(12.972, 77.592), 4.5));
        partnerRepository.save(new DeliveryPartner("DRV-2", "Sam", new Location(13.05, 77.70), 4.9));
    }

    @Test
    @DisplayName("Should execute complete happy-path lifecycle and trigger all observers")
    void testHappyPathWithObserverNotifications() {
        Cart cart = new Cart(customer.id());
        cart.addItem(pizzaRestaurant.getId(), pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow(), 2);
        cart.addItem(pizzaRestaurant.getId(), pizzaRestaurant.getMenu().getItem("ITEM-2").orElseThrow(), 1);

        Order order = checkoutService.checkout(cart, customer, PaymentMethod.UPI, "alice@upi", null);

        assertNotNull(order);
        assertEquals(OrderStatus.PLACED, order.getStatus());
        assertTrue(cart.isEmpty());

        // Kitchen updates
        fulfillmentService.confirmOrder(order.getOrderId());
        fulfillmentService.startPreparing(order.getOrderId());
        fulfillmentService.markReadyForPickup(order.getOrderId());

        // Dispatch driver
        Optional<DeliveryPartner> matched = dispatchService.dispatchPartner(order.getOrderId());
        assertTrue(matched.isPresent());
        assertEquals("DRV-1", matched.get().getId());
        assertEquals(PartnerStatus.BUSY, matched.get().getStatus());

        // Transit updates
        dispatchService.markPickedUp(matched.get().getId(), order.getOrderId());
        dispatchService.markDelivered(matched.get().getId(), order.getOrderId());

        assertEquals(OrderStatus.DELIVERED, order.getStatus());
        assertEquals(PartnerStatus.AVAILABLE, matched.get().getStatus());

        // Verify Observers
        assertFalse(customerNotifier.getNotifications().isEmpty());
        assertFalse(restaurantNotifier.getNotifications().isEmpty());
        assertFalse(partnerNotifier.getNotifications().isEmpty());
        assertEquals(6, auditLogger.getAuditLog().size()); // PLACED, CONFIRMED, PREPARING, READY_FOR_PICKUP, OUT_FOR_DELIVERY, DELIVERED
    }

    @Test
    @DisplayName("Should correctly apply Surge Pricing Strategy and Percentage Discount Strategy")
    void testSurgePricingAndDiscountStrategies() {
        SurgeDeliveryFeeStrategy surgeFeeStrategy = new SurgeDeliveryFeeStrategy(
                new DistanceBasedDeliveryFeeStrategy(20.0, 5.0, 1000.0), 1.5);
        PaymentProcessorFactory paymentFactory = new PaymentProcessorFactory();
        OrderCheckoutService surgeCheckout = new OrderCheckoutService(
                restaurantRepository, orderRepository, surgeFeeStrategy, paymentFactory, eventPublisher);

        Cart cart = new Cart(customer.id());
        cart.addItem(pizzaRestaurant.getId(), pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow(), 2); // 600.0 subtotal

        // 20% discount up to max $50 -> 600 * 0.20 = 120, capped at 50.0
        PercentageDiscountStrategy discountStrategy = new PercentageDiscountStrategy(20.0, 50.0);

        Order order = surgeCheckout.checkout(cart, customer, PaymentMethod.CREDIT_CARD, "1234567812345678", discountStrategy);

        OrderBill bill = order.getBill();
        assertEquals(600.0, bill.subtotal());
        assertEquals(50.0, bill.discount());
        assertTrue(bill.deliveryFee() > 20.0); // Base fee scaled by 1.5x surge
        assertEquals(550.0 * 0.05, bill.tax(), 0.001); // 5% tax on discounted subtotal
    }

    @Test
    @DisplayName("Should select highest-rated driver using HighestRatedPartnerMatchingStrategy")
    void testHighestRatedPartnerMatchingStrategy() {
        DeliveryDispatchService ratedDispatchService = new DeliveryDispatchService(
                partnerRepository, orderRepository, restaurantRepository,
                new HighestRatedPartnerMatchingStrategy(), eventPublisher);

        Cart cart = new Cart(customer.id());
        cart.addItem(pizzaRestaurant.getId(), pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow(), 1);
        Order order = checkoutService.checkout(cart, customer, PaymentMethod.CASH_ON_DELIVERY, null, null);

        // DRV-2 has rating 4.9, while DRV-1 has rating 4.5. DRV-2 should be selected despite being farther.
        Optional<DeliveryPartner> matched = ratedDispatchService.dispatchPartner(order.getOrderId());
        assertTrue(matched.isPresent());
        assertEquals("DRV-2", matched.get().getId());
    }

    @Test
    @DisplayName("Should reject adding items from multiple restaurants to single cart")
    void testSingleRestaurantCartConstraint() {
        Cart cart = new Cart(customer.id());
        cart.addItem(pizzaRestaurant.getId(), pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow(), 1);

        assertThrows(RestaurantMismatchException.class, () ->
                cart.addItem(burgerRestaurant.getId(), burgerRestaurant.getMenu().getItem("ITEM-3").orElseThrow(), 1));
    }

    @Test
    @DisplayName("Should reject order cancellation once food preparation has commenced")
    void testGuardedCancellationLifecycle() {
        Cart cart = new Cart(customer.id());
        cart.addItem(pizzaRestaurant.getId(), pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow(), 1);
        Order order = checkoutService.checkout(cart, customer, PaymentMethod.CASH_ON_DELIVERY, null, null);

        fulfillmentService.confirmOrder(order.getOrderId());
        fulfillmentService.startPreparing(order.getOrderId());

        assertThrows(InvalidOrderStateException.class, () ->
                fulfillmentService.cancelOrder(order.getOrderId()));
    }

    @Test
    @DisplayName("Should fail checkout when payment processor rejects authorization")
    void testPaymentFailure() {
        Cart cart = new Cart(customer.id());
        cart.addItem(pizzaRestaurant.getId(), pizzaRestaurant.getMenu().getItem("ITEM-1").orElseThrow(), 1);

        // Short 10-digit invalid card number
        assertThrows(PaymentFailedException.class, () ->
                checkoutService.checkout(cart, customer, PaymentMethod.CREDIT_CARD, "12345", null));
    }
}
