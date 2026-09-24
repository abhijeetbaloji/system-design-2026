package com.systemdesign.lld.fooddelivery.interview.repository;

import com.systemdesign.lld.fooddelivery.interview.domain.Customer;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Design Intent:
 * CustomerRepository manages customer profiles.
 */
public class CustomerRepository {
    private final Map<String, Customer> store = new ConcurrentHashMap<>();

    public void save(Customer customer) {
        if (customer != null) {
            store.put(customer.id(), customer);
        }
    }

    public Optional<Customer> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Collection<Customer> findAll() {
        return store.values();
    }
}
