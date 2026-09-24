package com.systemdesign.lld.fooddelivery.good;

import java.util.Objects;

/*
 * Design Intent:
 * DeliveryPartner encapsulates courier state, current geographic Location,
 * and operational status.
 */
public class DeliveryPartner {
    private final String id;
    private final String name;
    private Location location;
    private PartnerStatus status;
    private String currentOrderId;

    public DeliveryPartner(String id, String name, Location location) {
        this.id = Objects.requireNonNull(id, "DeliveryPartner ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.status = PartnerStatus.AVAILABLE;
    }

    public synchronized void assignOrder(String orderId) {
        this.currentOrderId = Objects.requireNonNull(orderId, "OrderId cannot be null");
        this.status = PartnerStatus.BUSY;
    }

    public synchronized void completeDelivery() {
        this.currentOrderId = null;
        this.status = PartnerStatus.AVAILABLE;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public synchronized Location getLocation() {
        return location;
    }

    public synchronized void setLocation(Location location) {
        this.location = Objects.requireNonNull(location);
    }

    public synchronized PartnerStatus getStatus() {
        return status;
    }

    public synchronized void setStatus(PartnerStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    public synchronized String getCurrentOrderId() {
        return currentOrderId;
    }
}
