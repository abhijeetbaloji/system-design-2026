package com.systemdesign.lld.patterns.singleton.example03.good;

import java.util.Optional;

public interface ServiceRegistry {
    <T> void register(Class<T> serviceType, T implementation);

    <T> Optional<T> resolve(Class<T> serviceType);

    void registerNamed(String name, Object implementation);

    Optional<Object> resolveNamed(String name);

    boolean unregister(Class<?> serviceType);

    void clear();

    int size();
}
