package com.systemdesign.lld.fooddelivery.springboot.observer;

import com.systemdesign.lld.fooddelivery.springboot.domain.Order;
import com.systemdesign.lld.fooddelivery.springboot.domain.OrderStatus;

public interface OrderObserver {
    void onOrderStatusChanged(Order order, OrderStatus previousStatus, OrderStatus newStatus);
}
