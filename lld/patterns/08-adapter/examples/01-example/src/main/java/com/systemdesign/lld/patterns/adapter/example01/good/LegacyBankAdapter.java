package com.systemdesign.lld.patterns.adapter.example01.good;

import java.math.BigDecimal;

/**
 * Adapter Class:
 * Bridges the Target (PaymentProcessor) and Adaptee (LegacyBankGateway).
 *
 * Responsibilities:
 * 1. Converts modern domain types (BigDecimal dollars) to legacy primitives (long cents).
 * 2. Injects vendor credentials (terminal code, secret keys) transparently.
 * 3. Translates primitive legacy status codes (-401, -100, 200) into domain PaymentResponse.
 */
public class LegacyBankAdapter implements PaymentProcessor {

    private final LegacyBankGateway legacyGateway;
    private final String terminalCode;
    private final String secretKey;

    public LegacyBankAdapter(LegacyBankGateway legacyGateway, String terminalCode, String secretKey) {
        this.legacyGateway = legacyGateway;
        this.terminalCode = terminalCode;
        this.secretKey = secretKey;
    }

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {
        if (request == null || request.amount() == null) {
            return PaymentResponse.fail("UNKNOWN", "Null payment request", "ERR_NULL");
        }

        // 1. Data translation: Dollars -> Cents
        long amountInCents = request.amount()
                .multiply(BigDecimal.valueOf(100))
                .longValue();

        // 2. Delegate to Adaptee
        long statusCode = legacyGateway.makeDirectDebit(
                terminalCode,
                secretKey,
                request.accountOrCardIdentifier(),
                amountInCents
        );

        // 3. Response translation: Legacy status code -> PaymentResponse
        if (statusCode == 200L) {
            return PaymentResponse.ok(
                    request.transactionReference(),
                    "Legacy debit approved successfully",
                    "LEGACY_TX_OK"
            );
        } else if (statusCode == -100L) {
            return PaymentResponse.fail(
                    request.transactionReference(),
                    "Insufficient funds or debit limit exceeded",
                    "LEGACY_CODE_-100"
            );
        } else if (statusCode == -401L) {
            return PaymentResponse.fail(
                    request.transactionReference(),
                    "Unauthorized legacy terminal credentials",
                    "LEGACY_CODE_-401"
            );
        } else {
            return PaymentResponse.fail(
                    request.transactionReference(),
                    "Payment rejected with legacy code: " + statusCode,
                    "LEGACY_CODE_" + statusCode
            );
        }
    }
}
