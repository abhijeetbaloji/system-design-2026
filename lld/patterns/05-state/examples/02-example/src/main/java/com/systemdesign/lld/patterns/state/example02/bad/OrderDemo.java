package com.systemdesign.lld.patterns.state.example02.bad;

/**
 * Runner demonstrating bad design problems.
 */
public class OrderDemo {
    public static void main(String[] args) {
        Order order = new Order("ORD-1001");
        order.addItem("Wireless Earbuds", 79.99);
        order.pay("TXN_8819");
        order.ship("FEDEX-992144");
        order.deliver();

        // Attempt invalid actions
        order.addItem("Extra Charger", 19.99);
        order.pay("TXN_DUPLICATE");
        order.cancel("Customer changed mind");
    }
}
