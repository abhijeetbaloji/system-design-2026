package com.systemdesign.lld.patterns.facade.example02.good;

public class ShippingService {

    public String generateShippingLabel(String customerAddress) {
        if (customerAddress == null || customerAddress.isBlank()) {
            throw new IllegalArgumentException("Shipping address cannot be empty");
        }
        String trackingId = "SHIP-FEDEX-" + System.currentTimeMillis();
        System.out.println("[ShippingService] Created shipping label for " + customerAddress + " | Tracking: " + trackingId);
        return trackingId;
    }
}
