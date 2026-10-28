package com.systemdesign.lld.patterns.singleton.example03.good;

public class StripeGateway implements PaymentGateway {
    @Override
    public String charge(double amount) {
        return "Stripe charged $" + String.format("%.2f", amount) + " (Token: ch_live_901)";
    }
}
