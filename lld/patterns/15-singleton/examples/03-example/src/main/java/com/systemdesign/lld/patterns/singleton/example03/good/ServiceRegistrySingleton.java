package com.systemdesign.lld.patterns.singleton.example03.good;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service Registry implemented as an Enum Singleton (Joshua Bloch's Effective Java approach).
 * Provides JVM-enforced uniqueness, thread safety, and reflection immunity.
 */
public enum ServiceRegistrySingleton implements ServiceRegistry {
    INSTANCE;

    private final Map<Class<?>, Object> typedServices = new ConcurrentHashMap<>();
    private final Map<String, Object> namedServices = new ConcurrentHashMap<>();

    @Override
    public <T> void register(Class<T> serviceType, T implementation) {
        Objects.requireNonNull(serviceType, "Service type cannot be null");
        Objects.requireNonNull(implementation, "Implementation cannot be null");
        typedServices.put(serviceType, implementation);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> resolve(Class<T> serviceType) {
        Objects.requireNonNull(serviceType, "Service type cannot be null");
        Object impl = typedServices.get(serviceType);
        return Optional.ofNullable((T) impl);
    }

    @Override
    public void registerNamed(String name, Object implementation) {
        Objects.requireNonNull(name, "Service name cannot be null");
        Objects.requireNonNull(implementation, "Implementation cannot be null");
        namedServices.put(name.toLowerCase(), implementation);
    }

    @Override
    public Optional<Object> resolveNamed(String name) {
        Objects.requireNonNull(name, "Service name cannot be null");
        return Optional.ofNullable(namedServices.get(name.toLowerCase()));
    }

    @Override
    public boolean unregister(Class<?> serviceType) {
        return typedServices.remove(serviceType) != null;
    }

    @Override
    public void clear() {
        typedServices.clear();
        namedServices.clear();
    }

    @Override
    public int size() {
        return typedServices.size() + namedServices.size();
    }
}
