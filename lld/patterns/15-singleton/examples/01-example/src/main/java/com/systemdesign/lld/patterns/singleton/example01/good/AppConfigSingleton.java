package com.systemdesign.lld.patterns.singleton.example01.good;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-Safe Bill Pugh Singleton for shared application configuration with
 * reflection defense and serialization defense.
 */
public class AppConfigSingleton implements ConfigurationService, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static volatile boolean initialized = false;

    private final Map<String, String> properties = new ConcurrentHashMap<>();

    // Private constructor with reflection defense
    private AppConfigSingleton() {
        if (initialized) {
            throw new IllegalStateException("Singleton instance already exists! Reflection instantiation is prohibited.");
        }
        initialized = true;
        loadDefaultSettings();
    }

    private void loadDefaultSettings() {
        properties.put("app.name", "EnterpriseCore");
        properties.put("app.version", "2026.1.0");
        properties.put("db.pool.max_connections", "50");
        properties.put("payment.gateway.url", "https://api.stripe.com/v1");
        properties.put("max.order.limit", "100");
        properties.put("feature.instant_checkout", "true");
    }

    // Bill Pugh Static Inner Holder
    private static class Holder {
        private static final AppConfigSingleton INSTANCE = new AppConfigSingleton();
    }

    public static AppConfigSingleton getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public String getString(String key) {
        Objects.requireNonNull(key, "Key cannot be null");
        return properties.get(key);
    }

    @Override
    public String getString(String key, String defaultValue) {
        Objects.requireNonNull(key, "Key cannot be null");
        return properties.getOrDefault(key, defaultValue);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        String val = getString(key);
        if (val == null) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        String val = getString(key);
        if (val == null) return defaultValue;
        return Boolean.parseBoolean(val);
    }

    @Override
    public void setProperty(String key, String value) {
        Objects.requireNonNull(key, "Key cannot be null");
        Objects.requireNonNull(value, "Value cannot be null");
        properties.put(key, value);
    }

    @Override
    public boolean contains(String key) {
        return properties.containsKey(key);
    }

    // Defend against duplicate instances created via deserialization
    @Serial
    protected Object readResolve() {
        return getInstance();
    }

    // For test environment resets
    public static void resetDefaultsForTesting() {
        AppConfigSingleton instance = getInstance();
        instance.properties.clear();
        instance.loadDefaultSettings();
    }
}
