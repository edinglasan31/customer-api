package com.mintpayments.api.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintpayments.api.customer.model.Customer;
import com.mintpayments.api.customer.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    private final static String CUSTOMER_PAYLOD = "{\"firstName\":\"John\",\"lastName\":\"Doe\",\"address\":{\"addressLine1\":\"Address line 1\",\"addressLine2\":\"Address line 2\",\"country\":\"AU\"}}";
    private final static String CUSTOMER_PAYLOD_INVALID ="{\"lastName\":\"Doe\",\"address\":{\"addressLine1\":\"Address line 1\",\"addressLine2\":\"Address line 2\",\"country\":\"AU\"}}";


    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    @Before
    public void setup(){
        //Mock single customer
        when(customerService.getCustomer(1))
                .thenReturn(new Customer()
                        .customerId(1)
                        .firstName("John")
                        .lastName("Doe"));
        //Mock customer list
        when(customerService.getCustomers())
                .thenReturn(Arrays.asList(new Customer().customerId(1), new Customer().customerId(2)));
        //Mock delete
        doNothing().when(customerService).deletesCustomer(anyLong());
        //Mock add
        Customer newCustomer = new Customer().firstName("John").lastName("doe");
        when(customerService.createCustomer(any(Customer.class))).thenReturn(newCustomer);
        //Mock update
        doNothing().when(customerService).updateCustomer(1, new Customer());

    }

    /**
     * Test unauthorized request
     * */
    @Test
    public void notAuthorized() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers/1").accept(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());

    }

    /**
     * Test getting a specific customer
     * */
    @Test
    @WithMockUser("user1")
    public void getCustomer() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers/1").accept(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        //Test response
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        //Test returned customer
        String content = response.getContentAsString();
        Customer customer = new ObjectMapper().readValue(content, Customer.class);
        assertThat(customer.getCustomerId()).isEqualTo(1);
        assertThat(customer.getFirstName()).isEqualTo("John");
        assertThat(customer.getLastName()).isEqualTo("Doe");
        //Test Hateoas links
        assertThat(customer.getLink("self")).isNotNull();
        assertThat(customer.getLink("customerList")).isNotNull();



    }

    /**
     * Test a request which is trying to get an invalid customer
     * */
    @Test
    @WithMockUser("user1")
    public void customerNotFound() throws Exception{
        //Customer is not existing
        when(customerService.getCustomer(anyLong())).thenReturn(null);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers/5").accept(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }

    /**
     * Test getting a list of customers
     * */
    @Test
    @WithMockUser("user1")
    public void getCustomers() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/customers/").accept(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        //Test response status
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        //Test customer list
        Map embedded = (Map)new ObjectMapper().readValue(response.getContentAsString(), Map.class).get("_embedded");
        assertThat(((List)embedded.get("customerList")).size()).isEqualTo(2);
    }

    /**
     * Test creation of new customer
     * */
    @Test
    @WithMockUser("user1")
    public void addCustomer() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/")
                .content(CUSTOMER_PAYLOD)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        //Request is successful
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Test creation of a new customer having a payload with missing field
     * */
    @Test
    @WithMockUser("user1")
    public void addInvalidCustomer() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/customers/")
                .content(CUSTOMER_PAYLOD_INVALID)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        //Request fails
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test update of customer
     * */
    @Test
    @WithMockUser("user1")
    public void updateCustomer() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/1")
                .content(CUSTOMER_PAYLOD)
                .contentType(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        //Request is successful
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Test updating a non existing customer
     * */
    @Test
    @WithMockUser("user1")
    public void updateInvalidCustomer() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/customers/1")
                .content(CUSTOMER_PAYLOD)
                .contentType(MediaType.APPLICATION_JSON);

        //When: Update request contains an invalid customer id
        when(customerService.getCustomer(1)).thenReturn(null);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        //Request fails
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Test deletition of customer
     * */
    @Test
    @WithMockUser("user1")
    public void deleteCustomer() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/customers/1").accept(MediaType.APPLICATION_JSON);

        MockHttpServletResponse response = mvc.perform(requestBuilder).andReturn().getResponse();

        //Rrequest is successful
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}