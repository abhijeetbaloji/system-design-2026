package com.systemdesign.lld.patterns.adapter.example01.bad;

/**
 * Legacy 3rd-party banking class with incompatible interface.
 * - Expects amounts in cents as primitive long.
 * - Requires proprietary terminal code and authentication key.
 * - Returns primitive long status code (e.g., 200 = OK, -100 = INSUFFICIENT_FUNDS).
 */
public class LegacyBankGateway {

    public long makeDirectDebit(String terminalCode, String authKey, String bankAccount, long amountInCents) {
        if (!"TERM-991".equals(terminalCode) || !"SECRET-KEY-XYZ".equals(authKey)) {
            return -401L; // Unauthorized
        }
        if (amountInCents <= 0) {
            return -400L; // Invalid amount
        }
        if (amountInCents > 500_000L) {
            return -100L; // Insufficient funds limit
        }
        System.out.println("[LegacyBankGateway] Direct debit of " + amountInCents + " cents from account " + bankAccount + " approved.");
        return 200L; // Success code
    }
}
