package com.mintpayments.api.customer.controller;

import com.mintpayments.api.customer.model.Customer;
import com.mintpayments.api.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private MessageSource messageSource;

    /**
     * Gets a customer using the customer id
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomer(@PathVariable long id) {
        Customer customer = customerService.getCustomer(id);

        if (customer == null) {
            //Customer is not existing
            return ResponseEntity.notFound().build();
        }

        CustomerResponseUtility.buildCustomerHateoasLinks(customer);

        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    /**
     * Gets all available customers
     */
    @GetMapping("/customers")
    public ResponseEntity<Resources<Customer>> getCustomers() {
        List<Customer> customers = new ArrayList<>();

        customerService.getCustomers().forEach(customer -> {
            //Create links to every customer object before adding it to the response
            CustomerResponseUtility.buildCustomerHateoasLinks(customer);
            customers.add(customer);
        });
        //Create self link
        Resources<Customer> allCustomers = new Resources<>(customers,
                linkTo(methodOn(CustomerController.class).getCustomers()).withSelfRel());

        return new ResponseEntity<Resources<Customer>>(allCustomers, HttpStatus.OK);
    }

    /**
     * Create a new customer
     */
    @PostMapping(value = "/customers")
    public ResponseEntity<Customer> addCustomer(@Valid @RequestBody Customer customer) throws URISyntaxException {
        //Save new customer
        Customer newCustomer = customerService.createCustomer(customer);
        //Add response links
        CustomerResponseUtility.buildCustomerHateoasLinks(newCustomer);
        //Return response
        return ResponseEntity
                .created(new URI(newCustomer.getLink("self").getHref()))
                .build();
    }

    /**
     * Update a customer
     */
    @PutMapping("/customers/{id}")
    public ResponseEntity updateCustomer(@Valid @RequestBody Customer customer, @PathVariable long id) {
        if (customerService.getCustomer(id) == null) {
            return ResponseEntity.notFound().build();
        }

        customerService.updateCustomer(id, new Customer(customer));

        return ResponseEntity.noContent().build();
    }


    /**
     * Delete a customer
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable long id) {
        customerService.deletesCustomer(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Handler for bad requests.
     * <p>
     * This will add the invalid fields and error messages in the response
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });
        return validationErrors;
    }
}
