package good;

/**
 * Concrete Observer: Restaurant Kitchen Display System (KDS).
 * Displays order tickets to chefs and controls the physical thermal kitchen printer.
 */
public class KitchenDisplaySystem implements OrderObserver {
    private int ticketCount = 0;

    @Override
    public void onOrderStatusChanged(FoodOrder order, OrderStatus previousStatus) {
        switch (order.getStatus()) {
            case PLACED -> {
                ticketCount++;
                System.out.printf("  [Kitchen Monitor] *** NEW ORDER CHIME ***%n");
                System.out.printf("  [Kitchen Monitor] Ticket #%s | %s | Items: %s ($%.2f)%n",
                        order.getOrderId(), order.getRestaurantName(), order.getItemsDescription(), order.getTotalAmount());
            }
            case PREPARING -> System.out.printf("  [Kitchen Monitor] Ticket #%s marked: IN_PROGRESS (Timer started)%n",
                    order.getOrderId());
            case OUT_FOR_DELIVERY -> System.out.printf("  [Kitchen Monitor] Ticket #%s marked: HANDED_OVER_TO_COURIER%n",
                    order.getOrderId());
            case DELIVERED -> System.out.printf("  [Kitchen Monitor] Ticket #%s archived from active order queue%n",
                    order.getOrderId());
            case CANCELLED -> System.out.printf("  [Kitchen Monitor] Ticket #%s marked: VOID / CANCELLED%n",
                    order.getOrderId());
        }
    }

    @Override
    public String getObserverId() {
        return "KITCHEN_DISPLAY_SYSTEM";
    }

    public int getTicketCount() {
        return ticketCount;
    }
}
