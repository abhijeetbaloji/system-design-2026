package com.systemdesign.lld.fooddelivery.bad;

/*
 * Design Intent (Bad Design):
 * MenuItem has public mutable fields. If a restaurant manager updates the price
 * or name of an item tomorrow, any historical Order holding a reference to this
 * object will have its price retroactively mutated, corrupting past invoices.
 */
public class MenuItem {
    public String id;
    public String name;
    public double price;
    public boolean isAvailable;

    public MenuItem(String id, String name, double price, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.isAvailable = isAvailable;
    }
}
