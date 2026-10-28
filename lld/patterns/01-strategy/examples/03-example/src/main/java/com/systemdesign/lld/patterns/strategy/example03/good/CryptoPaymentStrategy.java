package com.systemdesign.lld.patterns.strategy.example03.good;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Concrete Strategy:
 * Implements settlement using cryptocurrency on a decentralized blockchain network.
 */
public class CryptoPaymentStrategy implements PaymentStrategy {

    private final String walletAddress;
    private final String networkName;
    private final BigDecimal estimatedGasFee;

    public CryptoPaymentStrategy(String walletAddress, String networkName, BigDecimal estimatedGasFee) {
        this.walletAddress = walletAddress != null ? walletAddress.trim() : "";
        this.networkName = networkName != null ? networkName.trim() : "Ethereum Mainnet";
        this.estimatedGasFee = estimatedGasFee != null ? estimatedGasFee : new BigDecimal("4.50");
    }

    public CryptoPaymentStrategy(String walletAddress) {
        this(walletAddress, "Ethereum Mainnet", new BigDecimal("4.50"));
    }

    @Override
    public PaymentResult processPayment(BigDecimal amount) {
        String txId = "0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        if (walletAddress.length() < 26 || walletAddress.length() > 44) {
            return new PaymentResult(false, txId, amount, BigDecimal.ZERO, "Invalid blockchain wallet address", Instant.now());
        }

        String msg = "Crypto transaction broadcast to " + networkName + " for wallet " + walletAddress;
        return new PaymentResult(true, txId, amount, estimatedGasFee, msg, Instant.now());
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public String getNetworkName() {
        return networkName;
    }
}
