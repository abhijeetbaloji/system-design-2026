package com.systemdesign.lld.patterns.singleton.example01.good;

import java.util.Objects;

public class PaymentService {
    private final ConfigurationService config;

    public PaymentService(ConfigurationService config) {
        this.config = Objects.requireNonNull(config, "ConfigurationService cannot be null");
    }

    public String getGatewayEndpoint() {
        return config.getString("payment.gateway.url", "https://default.gateway.local");
    }

    public int getDbConnectionLimit() {
        return config.getInt("db.pool.max_connections", 10);
    }
}
