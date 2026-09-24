package com.systemdesign.lld.fooddelivery.bad;

/*
 * Design Intent (Bad Design):
 * DeliveryPartner has public mutable fields with raw primitive coordinates.
 * Partner status is represented as a raw boolean without supporting busy/offline states cleanly.
 */
public class DeliveryPartner {
    public String id;
    public String name;
    public double latitude;
    public double longitude;
    public boolean isAvailable;

    public DeliveryPartner(String id, String name, double latitude, double longitude, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isAvailable = isAvailable;
    }
}
