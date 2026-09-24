package com.systemdesign.lld.fooddelivery.good;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Design Intent:
 * DeliveryService manages delivery partner registration and coordinates
 * dispatching and delivery lifecycle.
 */
public class DeliveryService {
    private final Map<String, DeliveryPartner> partners = new ConcurrentHashMap<>();
    private final OrderService orderService;

    public DeliveryService(OrderService orderService) {
        this.orderService = orderService;
    }

    public void registerPartner(DeliveryPartner partner) {
        if (partner != null) {
            partners.put(partner.getId(), partner);
        }
    }

    public Optional<DeliveryPartner> assignNearestAvailablePartner(String orderId, Location pickupLocation) {
        Optional<Order> orderOpt = orderService.getOrder(orderId);
        if (orderOpt.isEmpty()) return Optional.empty();

        Order order = orderOpt.get();

        DeliveryPartner bestPartner = null;
        double minDistance = Double.MAX_VALUE;

        for (DeliveryPartner partner : partners.values()) {
            if (partner.getStatus() == PartnerStatus.AVAILABLE) {
                double dist = partner.getLocation().distanceTo(pickupLocation);
                if (dist < minDistance) {
                    minDistance = dist;
                    bestPartner = partner;
                }
            }
        }

        if (bestPartner != null) {
            bestPartner.assignOrder(orderId);
            order.assignDeliveryPartner(bestPartner.getId());
            return Optional.of(bestPartner);
        }

        return Optional.empty();
    }

    public void markOrderPickedUp(String partnerId, String orderId) {
        orderService.updateOrderStatus(orderId, OrderStatus.OUT_FOR_DELIVERY);
    }

    public void markOrderDelivered(String partnerId, String orderId) {
        orderService.updateOrderStatus(orderId, OrderStatus.DELIVERED);
        DeliveryPartner partner = partners.get(partnerId);
        if (partner != null) {
            partner.completeDelivery();
        }
    }

    public Optional<DeliveryPartner> getPartner(String partnerId) {
        return Optional.ofNullable(partners.get(partnerId));
    }
}
