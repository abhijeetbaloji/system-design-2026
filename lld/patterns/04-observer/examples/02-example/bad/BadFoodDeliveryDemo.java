package bad;

public class BadFoodDeliveryDemo {
    public static void main(String[] args) {
        System.out.println("=== Bad Food Delivery Notification Demo ===");

        BadOrderService orderService = new BadOrderService();

        String orderId = "ORD-8821";
        String phone = "+1-555-4091";
        String restaurant = "Tokyo Ramen Bar";
        String items = "2x Tonkotsu Ramen, 1x Gyoza";

        orderService.updateStatus(orderId, phone, restaurant, items, OrderStatus.PLACED);
        orderService.updateStatus(orderId, phone, restaurant, items, OrderStatus.PREPARING);
        orderService.updateStatus(orderId, phone, restaurant, items, OrderStatus.OUT_FOR_DELIVERY);
        orderService.updateStatus(orderId, phone, restaurant, items, OrderStatus.DELIVERED);

        System.out.println("\nProblems Exposed:");
        System.out.println("1. BadOrderService is hardcoded to SMS, kitchen printer, and driver service.");
        System.out.println("2. Adding customer in-app notifications requires changing BadOrderService.");
        System.out.println("3. If SMS gateway throws an exception, kitchen ticket printing never executes.");
    }
}
