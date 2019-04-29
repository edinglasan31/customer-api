package com.mintpayments.api.customer.service;

import com.mintpayments.api.customer.model.Customer;

import java.util.List;


public interface CustomerService {
    Customer getCustomer(long id);

    List<Customer> getCustomers();

    Customer createCustomer(Customer customer);

    void deletesCustomer(long id);

    void updateCustomer(long id, Customer customer);
}
