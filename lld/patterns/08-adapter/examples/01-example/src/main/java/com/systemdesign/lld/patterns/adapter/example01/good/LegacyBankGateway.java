package com.systemdesign.lld.patterns.adapter.example01.good;

/**
 * Adaptee:
 * Closed-source or legacy third-party banking class with incompatible method signatures.
 */
public class LegacyBankGateway {

    public long makeDirectDebit(String terminalCode, String authKey, String bankAccount, long amountInCents) {
        if (!"TERM-991".equals(terminalCode) || !"SECRET-KEY-XYZ".equals(authKey)) {
            return -401L; // Unauthorized
        }
        if (amountInCents <= 0) {
            return -400L; // Bad request
        }
        if (amountInCents > 500_000L) {
            return -100L; // Limit exceeded / Insufficient funds
        }
        System.out.println("[LegacyBankGateway] Debit executed: " + amountInCents + " cents for account " + bankAccount);
        return 200L; // Success status code
    }
}
