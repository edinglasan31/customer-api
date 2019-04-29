package com.mintpayments.api.customer.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class Customer extends ResourceSupport implements Serializable {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private Address address;
    private long customerId;

    public Customer() {

    }

    public Customer(Customer customer) {
        if (customer != null) {
            address(customer.getAddress());
            firstName(customer.getFirstName());
            lastName(customer.getLastName());
            customerId(customer.getCustomerId());
        }
    }

    public Customer firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Customer lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Customer address(Address address) {
        this.address = address;
        return this;
    }


    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long id) {
        this.customerId = id;
    }

    public Customer customerId(long customerId) {
        this.customerId = customerId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(firstName, customer.firstName) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(address, customer.address) &&
                Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, address, customerId);
    }

    @JsonProperty("_links")
    public void setLinks(final Map<String, Link> links) {
        links.forEach((label, link) -> add(link.withRel(label)));
    }
}

