package com.mintpayments.api.customer.controller;

import com.mintpayments.api.customer.model.Customer;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class CustomerResponseUtility {

    /**
     * This will generate the Hateoas link for the Customer response
     */
    public static void buildCustomerHateoasLinks(Customer customer) {
        customer.getLinks().clear();
        customer.add(linkTo(methodOn(CustomerController.class).getCustomer(customer.getCustomerId())).withSelfRel());
        customer.add(linkTo(methodOn(CustomerController.class).getCustomers()).withRel("customerList"));
    }
}
