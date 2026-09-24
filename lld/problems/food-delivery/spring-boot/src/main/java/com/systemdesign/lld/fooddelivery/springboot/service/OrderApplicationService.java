package com.systemdesign.lld.fooddelivery.springboot.service;

import com.systemdesign.lld.fooddelivery.springboot.domain.*;
import com.systemdesign.lld.fooddelivery.springboot.dto.*;
import com.systemdesign.lld.fooddelivery.springboot.exception.PaymentFailedException;
import com.systemdesign.lld.fooddelivery.springboot.exception.ResourceNotFoundException;
import com.systemdesign.lld.fooddelivery.springboot.observer.OrderEventPublisher;
import com.systemdesign.lld.fooddelivery.springboot.payment.PaymentProcessor;
import com.systemdesign.lld.fooddelivery.springboot.payment.PaymentProcessorFactory;
import com.systemdesign.lld.fooddelivery.springboot.payment.PaymentResult;
import com.systemdesign.lld.fooddelivery.springboot.repository.CartRepository;
import com.systemdesign.lld.fooddelivery.springboot.repository.CustomerRepository;
import com.systemdesign.lld.fooddelivery.springboot.repository.OrderRepository;
import com.systemdesign.lld.fooddelivery.springboot.repository.RestaurantRepository;
import com.systemdesign.lld.fooddelivery.springboot.strategy.DeliveryFeeStrategy;
import com.systemdesign.lld.fooddelivery.springboot.strategy.DiscountStrategy;
import com.systemdesign.lld.fooddelivery.springboot.strategy.FlatDiscountStrategy;
import com.systemdesign.lld.fooddelivery.springboot.strategy.NoDiscountStrategy;
import com.systemdesign.lld.fooddelivery.springboot.strategy.PercentageDiscountStrategy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class OrderApplicationService {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final DeliveryFeeStrategy deliveryFeeStrategy;
    private final PaymentProcessorFactory paymentProcessorFactory;
    private final OrderEventPublisher eventPublisher;

    private static final double TAX_RATE = 0.05;

    public OrderApplicationService(OrderRepository orderRepository,
                                  CartRepository cartRepository,
                                  CustomerRepository customerRepository,
                                  RestaurantRepository restaurantRepository,
                                  DeliveryFeeStrategy deliveryFeeStrategy,
                                  PaymentProcessorFactory paymentProcessorFactory,
                                  OrderEventPublisher eventPublisher) {
        this.orderRepository = Objects.requireNonNull(orderRepository);
        this.cartRepository = Objects.requireNonNull(cartRepository);
        this.customerRepository = Objects.requireNonNull(customerRepository);
        this.restaurantRepository = Objects.requireNonNull(restaurantRepository);
        this.deliveryFeeStrategy = Objects.requireNonNull(deliveryFeeStrategy);
        this.paymentProcessorFactory = Objects.requireNonNull(paymentProcessorFactory);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
    }

    public OrderResponse checkout(CheckoutRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseGet(() -> {
                    // Auto-register default customer location if not explicitly stored
                    Customer c = new Customer(request.customerId(), "Customer " + request.customerId(),
                            "cust@example.com", "9999999999", new Location(12.98, 77.60));
                    customerRepository.save(c);
                    return c;
                });

        Cart cart = cartRepository.getOrCreate(request.customerId());
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot checkout with an empty cart");
        }

        String restaurantId = cart.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + restaurantId));

        if (!restaurant.isActive()) {
            throw new IllegalStateException("Restaurant is currently inactive: " + restaurant.getName());
        }

        DiscountStrategy discountStrategy = resolveDiscountStrategy(request.promoCode());
        double subtotal = cart.getSubtotal();
        double deliveryFee = deliveryFeeStrategy.calculateFee(restaurant.getLocation(), customer.location(), subtotal);
        double discount = discountStrategy.calculateDiscount(subtotal);
        OrderBill bill = OrderBill.of(subtotal, deliveryFee, discount, TAX_RATE);

        PaymentProcessor processor = paymentProcessorFactory.getProcessor(request.paymentMethod());
        PaymentResult result = processor.process(bill.totalAmount(), request.paymentDetail());
        if (!result.successful()) {
            throw new PaymentFailedException("Payment failed: " + result.message());
        }

        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem ci : cart.getItems().values()) {
            orderItems.add(new OrderItem(
                    ci.getItem().getId(),
                    ci.getItem().getName(),
                    ci.getItem().getPrice(),
                    ci.getQuantity()
            ));
        }

        String orderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        Order order = new Order(orderId, customer.id(), restaurantId, orderItems, bill);
        orderRepository.save(order);

        cart.clear();
        eventPublisher.publishStatusChange(order, null, OrderStatus.PLACED);

        return toOrderResponse(order);
    }

    public OrderResponse getOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return toOrderResponse(order);
    }

    public OrderResponse confirmOrder(String orderId) {
        return transition(orderId, OrderStatus.CONFIRMED);
    }

    public OrderResponse startPreparing(String orderId) {
        return transition(orderId, OrderStatus.PREPARING);
    }

    public OrderResponse markReadyForPickup(String orderId) {
        return transition(orderId, OrderStatus.READY_FOR_PICKUP);
    }

    public OrderResponse cancelOrder(String orderId) {
        return transition(orderId, OrderStatus.CANCELLED);
    }

    private OrderResponse transition(String orderId, OrderStatus nextStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        OrderStatus prev = order.getStatus();
        order.transitionTo(nextStatus);
        eventPublisher.publishStatusChange(order, prev, nextStatus);
        return toOrderResponse(order);
    }

    private DiscountStrategy resolveDiscountStrategy(String promoCode) {
        if (promoCode == null || promoCode.isBlank()) {
            return new NoDiscountStrategy();
        }
        if ("FLAT50".equalsIgnoreCase(promoCode)) {
            return new FlatDiscountStrategy(200.0, 50.0);
        } else if ("PERCENT20".equalsIgnoreCase(promoCode)) {
            return new PercentageDiscountStrategy(20.0, 100.0);
        }
        return new NoDiscountStrategy();
    }

    public OrderResponse toOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems().stream()
                .map(oi -> new OrderItemResponse(oi.menuItemId(), oi.itemName(), oi.unitPrice(), oi.quantity(), oi.getSubtotal()))
                .toList();

        OrderBill b = order.getBill();
        OrderBillResponse billResponse = new OrderBillResponse(b.subtotal(), b.deliveryFee(), b.discount(), b.tax(), b.totalAmount());

        return new OrderResponse(
                order.getOrderId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                itemResponses,
                billResponse,
                order.getStatus(),
                order.getDeliveryPartnerId(),
                order.getCreatedAt()
        );
    }
}
