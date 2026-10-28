package com.systemdesign.lld.patterns.singleton.example01.bad;

import java.util.HashMap;
import java.util.Map;

public class BadConfigurationManager {
    private final Map<String, String> properties = new HashMap<>();

    // Flawed: Public constructor allows arbitrary duplicate instances
    public BadConfigurationManager() {
        // Simulating reading from disk
        properties.put("db.host", "localhost");
        properties.put("db.port", "5432");
        properties.put("max.order.limit", "100");
    }

    public void set(String key, String value) {
        properties.put(key, value);
    }

    public String get(String key) {
        return properties.get(key);
    }

    public int getInt(String key, int defaultValue) {
        String val = properties.get(key);
        return val != null ? Integer.parseInt(val) : defaultValue;
    }
}
