package com.systemdesign.lld.patterns.singleton.example02.good;

import java.util.Objects;

public class PaymentService {
    private final LoggerService logger;

    public PaymentService(LoggerService logger) {
        this.logger = Objects.requireNonNull(logger, "Logger cannot be null");
    }

    public boolean processPayment(String orderId, double amount) {
        logger.info("Processing transaction of $" + String.format("%.2f", amount) + " for order " + orderId);
        if (amount <= 0) {
            logger.error("Invalid payment amount: " + amount + " for order " + orderId);
            return false;
        }
        logger.info("Payment settled successfully for order: " + orderId);
        return true;
    }
}
