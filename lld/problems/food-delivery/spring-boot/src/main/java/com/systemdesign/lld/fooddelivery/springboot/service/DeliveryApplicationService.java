package com.systemdesign.lld.fooddelivery.springboot.service;

import com.systemdesign.lld.fooddelivery.springboot.domain.DeliveryPartner;
import com.systemdesign.lld.fooddelivery.springboot.domain.Location;
import com.systemdesign.lld.fooddelivery.springboot.domain.Order;
import com.systemdesign.lld.fooddelivery.springboot.domain.OrderStatus;
import com.systemdesign.lld.fooddelivery.springboot.dto.CreatePartnerRequest;
import com.systemdesign.lld.fooddelivery.springboot.dto.OrderResponse;
import com.systemdesign.lld.fooddelivery.springboot.dto.PartnerResponse;
import com.systemdesign.lld.fooddelivery.springboot.exception.ResourceNotFoundException;
import com.systemdesign.lld.fooddelivery.springboot.observer.OrderEventPublisher;
import com.systemdesign.lld.fooddelivery.springboot.repository.DeliveryPartnerRepository;
import com.systemdesign.lld.fooddelivery.springboot.repository.OrderRepository;
import com.systemdesign.lld.fooddelivery.springboot.repository.RestaurantRepository;
import com.systemdesign.lld.fooddelivery.springboot.strategy.DeliveryPartnerMatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DeliveryApplicationService {
    private final DeliveryPartnerRepository partnerRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final DeliveryPartnerMatchingStrategy matchingStrategy;
    private final OrderEventPublisher eventPublisher;
    private final OrderApplicationService orderApplicationService;

    public DeliveryApplicationService(DeliveryPartnerRepository partnerRepository,
                                      OrderRepository orderRepository,
                                      RestaurantRepository restaurantRepository,
                                      DeliveryPartnerMatchingStrategy matchingStrategy,
                                      OrderEventPublisher eventPublisher,
                                      OrderApplicationService orderApplicationService) {
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
        this.orderRepository = Objects.requireNonNull(orderRepository);
        this.restaurantRepository = Objects.requireNonNull(restaurantRepository);
        this.matchingStrategy = Objects.requireNonNull(matchingStrategy);
        this.eventPublisher = Objects.requireNonNull(eventPublisher);
        this.orderApplicationService = Objects.requireNonNull(orderApplicationService);
    }

    public PartnerResponse registerPartner(CreatePartnerRequest request) {
        DeliveryPartner partner = new DeliveryPartner(
                request.id(),
                request.name(),
                new Location(request.latitude(), request.longitude()),
                request.rating() != null ? request.rating() : 5.0
        );
        partnerRepository.save(partner);
        return toPartnerResponse(partner);
    }

    public PartnerResponse dispatchPartner(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        Location restaurantLocation = restaurantRepository.findById(order.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found: " + order.getRestaurantId()))
                .getLocation();

        List<DeliveryPartner> available = partnerRepository.findAvailablePartners();
        Optional<DeliveryPartner> matched = matchingStrategy.matchPartner(available, restaurantLocation);

        if (matched.isEmpty()) {
            throw new IllegalStateException("No available delivery partner found for order: " + orderId);
        }

        DeliveryPartner partner = matched.get();
        partner.assignOrder(orderId);
        order.assignDeliveryPartner(partner.getId());

        return toPartnerResponse(partner);
    }

    public OrderResponse markPickedUp(String partnerId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        OrderStatus prev = order.getStatus();
        order.transitionTo(OrderStatus.OUT_FOR_DELIVERY);
        eventPublisher.publishStatusChange(order, prev, OrderStatus.OUT_FOR_DELIVERY);

        return orderApplicationService.toOrderResponse(order);
    }

    public OrderResponse markDelivered(String partnerId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        DeliveryPartner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResourceNotFoundException("DeliveryPartner not found: " + partnerId));

        OrderStatus prev = order.getStatus();
        order.transitionTo(OrderStatus.DELIVERED);
        partner.completeDelivery();
        eventPublisher.publishStatusChange(order, prev, OrderStatus.DELIVERED);

        return orderApplicationService.toOrderResponse(order);
    }

    private PartnerResponse toPartnerResponse(DeliveryPartner p) {
        return new PartnerResponse(p.getId(), p.getName(), p.getLocation().latitude(),
                p.getLocation().longitude(), p.getRating(), p.getStatus(), p.getCurrentOrderId());
    }
}
