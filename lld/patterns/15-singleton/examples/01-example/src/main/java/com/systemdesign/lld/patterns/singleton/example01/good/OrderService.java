package com.systemdesign.lld.patterns.singleton.example01.good;

import java.util.Objects;

public class OrderService {
    private final ConfigurationService config;

    public OrderService(ConfigurationService config) {
        this.config = Objects.requireNonNull(config, "ConfigurationService cannot be null");
    }

    public int getMaxAllowedOrders() {
        return config.getInt("max.order.limit", 50);
    }

    public boolean isInstantCheckoutEnabled() {
        return config.getBoolean("feature.instant_checkout", false);
    }
}
