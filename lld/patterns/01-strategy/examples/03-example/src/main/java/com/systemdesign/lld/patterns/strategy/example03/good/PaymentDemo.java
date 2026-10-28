package com.systemdesign.lld.patterns.strategy.example03.good;

import java.math.BigDecimal;

/**
 * Demonstrates the Strategy pattern for payment processing.
 */
public class PaymentDemo {
    public static void main(String[] args) {
        BigDecimal cartTotal = new BigDecimal("199.95");

        // 1. Customer initiates checkout with Credit Card
        PaymentStrategy cardStrategy = new CreditCardPaymentStrategy(
            "4111222233334444", "Sarah Connor", "456", "11/29"
        );
        PaymentContext context = new PaymentContext(cardStrategy);
        PaymentResult res1 = context.pay(cartTotal);
        printResult("Credit Card", res1);

        // 2. Customer changes mind at checkout and switches to PayPal
        context.setPaymentStrategy(new PayPalPaymentStrategy("sarah.c@cyberdyne.org", "token_auth_991823"));
        PaymentResult res2 = context.pay(cartTotal);
        printResult("PayPal", res2);

        // 3. Customer switches to UPI
        context.setPaymentStrategy(new UpiPaymentStrategy("sarah@okhdfcbank", "9988"));
        PaymentResult res3 = context.pay(cartTotal);
        printResult("UPI", res3);

        // 4. Customer switches to Crypto
        context.setPaymentStrategy(new CryptoPaymentStrategy("0x71C84943265Ae3ECaDC9C61A22", "Polygon PoS", new BigDecimal("0.02")));
        PaymentResult res4 = context.pay(cartTotal);
        printResult("Crypto", res4);
    }

    private static void printResult(String method, PaymentResult result) {
        System.out.println("=== " + method + " ===");
        System.out.println("Status:  " + (result.successful() ? "SUCCESS" : "FAILED"));
        System.out.println("TxId:    " + result.transactionId());
        System.out.println("Amount:  $" + result.amount());
        System.out.println("Fee:     $" + result.fee());
        System.out.println("Message: " + result.message());
        System.out.println();
    }
}
