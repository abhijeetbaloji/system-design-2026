package com.systemdesign.lld.patterns.singleton.example03.bad;

import java.util.HashMap;
import java.util.Map;

public class BadServiceRegistry {
    private final Map<String, Object> services = new HashMap<>();

    // Flawed: Allows arbitrary instances to be created
    public BadServiceRegistry() {
    }

    public void register(String key, Object service) {
        services.put(key, service);
    }

    public Object resolve(String key) {
        return services.get(key);
    }
}
