package com.systemdesign.lld.fooddelivery.springboot.domain;

import java.util.Objects;

public class DeliveryPartner {
    private final String id;
    private final String name;
    private Location location;
    private double rating;
    private PartnerStatus status;
    private String currentOrderId;

    public DeliveryPartner(String id, String name, Location location, double rating) {
        this.id = Objects.requireNonNull(id, "DeliveryPartner ID cannot be null");
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.location = Objects.requireNonNull(location, "Location cannot be null");
        this.rating = Math.max(0.0, Math.min(5.0, rating));
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = Math.max(0.0, Math.min(5.0, rating));
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
