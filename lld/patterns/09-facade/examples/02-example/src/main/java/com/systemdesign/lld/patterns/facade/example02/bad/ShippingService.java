package com.systemdesign.lld.patterns.facade.example02.bad;

public class ShippingService {

    public String createShipment(String customerAddress) {
        if (customerAddress == null || customerAddress.isBlank()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        String trackingNumber = "TRACK-" + System.currentTimeMillis();
        System.out.println("[ShippingService] Created shipment to " + customerAddress + " | Tracking: " + trackingNumber);
        return trackingNumber;
    }
}
