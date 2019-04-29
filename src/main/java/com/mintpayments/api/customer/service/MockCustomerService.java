package com.mintpayments.api.customer.service;

import com.mintpayments.api.customer.model.Address;
import com.mintpayments.api.customer.model.Customer;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "customers")
public class MockCustomerService implements CustomerService {

    private static Map<Long, Customer> database;
    private static long counter;

    static {
        database = new HashMap<>();
        database.put(Long.parseLong("1"), new Customer()
                .customerId(1)
                .firstName("Tony")
                .lastName("Stark")
                .address(new Address()
                        .addressLine1("Address line 1")
                        .addressLine2("Address line 2")
                        .country("AU")));
        database.put(Long.parseLong("2"), new Customer()
                .customerId(2)
                .firstName("Steve")
                .lastName("Rogers")
                .address(new Address()
                        .addressLine1("Address line 1")
                        .addressLine2("Address line 2")
                        .country("US")));
        database.put(Long.parseLong("3"), new Customer()
                .customerId(3)
                .firstName("Peter")
                .lastName("Parker")
                .address(new Address()
                        .addressLine1("Address line 1")
                        .addressLine2("Address line 2")
                        .country("US")));
        counter = 3;
    }

    /**
     * Get a specific customer.
     * Caching added for consumers that frequently poll for new data
     */
    @Cacheable(key = "#id", unless = "#result == null")
    @Override
    public Customer getCustomer(long id) {
        Customer customer = database.get(id);
        return customer == null ? null : new Customer(customer);
    }

    @Override
    public List<Customer> getCustomers() {
        return database.values().stream().map(customer -> new Customer(customer)).collect(Collectors.toList());
    }

    @Override
    @CachePut(key = "#result.customerId")
    public Customer createCustomer(Customer c) {
        Customer newCustomer = new Customer(c).customerId(++counter);
        database.put(counter, newCustomer);
        return newCustomer;
    }

    @Override
    @CacheEvict(key = "#id")
    public void deletesCustomer(long id) {
        if (database.get(id) != null) {
            database.remove(id);
        }
    }

    @Override
    @CacheEvict(key = "#id")
    public void updateCustomer(long id, Customer customer) {
        database.put(id, customer.customerId(id));
    }
}
