package com.systemdesign.lld.patterns.facade.example02.good;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PricingService {

    private static final BigDecimal TAX_RATE = BigDecimal.valueOf(0.08); // 8% sales tax

    public BigDecimal calculateTotal(List<CartItem> items) {
        BigDecimal subtotal = BigDecimal.ZERO;
        for (CartItem item : items) {
            subtotal = subtotal.add(item.unitPrice().multiply(BigDecimal.valueOf(item.quantity())));
        }
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(tax);
        System.out.println("[PricingService] Subtotal: $" + subtotal + " + Tax: $" + tax + " = Total: $" + total);
        return total;
    }
}
