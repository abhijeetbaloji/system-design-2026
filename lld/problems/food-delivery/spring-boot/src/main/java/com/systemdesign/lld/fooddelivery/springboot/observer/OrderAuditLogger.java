package com.systemdesign.lld.fooddelivery.springboot.observer;

import com.systemdesign.lld.fooddelivery.springboot.domain.Order;
import com.systemdesign.lld.fooddelivery.springboot.domain.OrderStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderAuditLogger implements OrderObserver {

    public record AuditEntry(String orderId, OrderStatus previousStatus, OrderStatus newStatus, Instant timestamp) {}

    private final List<AuditEntry> auditLog = new ArrayList<>();

    @Override
    public synchronized void onOrderStatusChanged(Order order, OrderStatus previousStatus, OrderStatus newStatus) {
        auditLog.add(new AuditEntry(order.getOrderId(), previousStatus, newStatus, Instant.now()));
    }

    public synchronized List<AuditEntry> getAuditLog() {
        return Collections.unmodifiableList(auditLog);
    }
}
