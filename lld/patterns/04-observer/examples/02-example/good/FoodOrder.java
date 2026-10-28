package good;

/**
 * Domain entity representing an active customer food delivery order.
 */
public class FoodOrder {
    private final String orderId;
    private final String customerName;
    private final String customerPhone;
    private final String restaurantName;
    private final String itemsDescription;
    private final double totalAmount;
    private OrderStatus status;

    public FoodOrder(String orderId, String customerName, String customerPhone,
                     String restaurantName, String itemsDescription, double totalAmount) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be blank.");
        }
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.restaurantName = restaurantName;
        this.itemsDescription = itemsDescription;
        this.totalAmount = totalAmount;
        this.status = OrderStatus.PLACED;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getItemsDescription() {
        return itemsDescription;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
