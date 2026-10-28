package com.systemdesign.lld.patterns.adapter.example01.good;

public record PaymentResponse(
        String transactionId,
        boolean success,
        String message,
        String gatewayReference
) {
    public static PaymentResponse ok(String txId, String message, String gatewayRef) {
        return new PaymentResponse(txId, true, message, gatewayRef);
    }

    public static PaymentResponse fail(String txId, String message, String gatewayRef) {
        return new PaymentResponse(txId, false, message, gatewayRef);
    }
}
