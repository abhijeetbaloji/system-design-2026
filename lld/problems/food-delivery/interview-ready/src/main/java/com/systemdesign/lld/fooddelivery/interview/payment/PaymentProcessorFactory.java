package com.systemdesign.lld.fooddelivery.interview.payment;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/*
 * Design Intent (Factory / Strategy Registry):
 * Resolves the appropriate PaymentProcessor based on the requested PaymentMethod.
 * Encapsulates processor instantiations and lookups.
 */
public class PaymentProcessorFactory {
    private final Map<PaymentMethod, PaymentProcessor> processors = new EnumMap<>(PaymentMethod.class);

    public PaymentProcessorFactory() {
        processors.put(PaymentMethod.CREDIT_CARD, new CreditCardPaymentProcessor());
        processors.put(PaymentMethod.UPI, new UpiPaymentProcessor());
        processors.put(PaymentMethod.CASH_ON_DELIVERY, new CashOnDeliveryPaymentProcessor());
    }

    public PaymentProcessor getProcessor(PaymentMethod method) {
        Objects.requireNonNull(method, "PaymentMethod cannot be null");
        PaymentProcessor processor = processors.get(method);
        if (processor == null) {
            throw new IllegalArgumentException("No processor registered for payment method: " + method);
        }
        return processor;
    }

    public void registerProcessor(PaymentMethod method, PaymentProcessor processor) {
        processors.put(method, processor);
    }
}
