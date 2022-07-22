package com.theam.crm.controller;

import com.theam.crm.exceptions.CustomerNotFoundException;
import com.theam.crm.model.Customer;
import com.theam.crm.payload.request.CustomerRequest;
import com.theam.crm.payload.request.UpdateCustomerRequest;
import com.theam.crm.payload.response.CustomerResponse;
import com.theam.crm.service.CustomerService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.theam.crm.TestUtilities.convertToJson;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CustomerControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @MockBean
    private CustomerService customerService;

    Customer CUSTOMER_1 = new Customer("Peter", "Morgan", "peter@gmail.com");
    Customer CUSTOMER_2 = new Customer("Mary", "Jefferson", "mary@gmail.com");
    Customer CUSTOMER_3 = new Customer("John", "Myers", "john@gmail.com");

    MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "image.txt", MediaType.TEXT_PLAIN_VALUE,
            "image".getBytes());

    @Test
    @WithMockUser()
    void testSizeForGetAllCustomers() throws Exception {

        when(customerService.getAllCustomers()).thenReturn(List.of(CustomerResponse.builder().build(), CustomerResponse.builder().build()));

        mockMvc.perform(get("/api/customer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser()
    void testGetSingleCustomer() throws Exception {

        when(customerService.getCustomerById(1L)).thenReturn((CustomerResponse.builder().build()));

        mockMvc.perform(get("/api/customer/{customerId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetAllCustomers() throws Exception {
        List<Customer> records = new ArrayList<>(Arrays.asList(CUSTOMER_1, CUSTOMER_2, CUSTOMER_3));

        when(customerService.getAllCustomers()).thenReturn(records.stream().map(CustomerResponse::toCustomerResponse).collect(Collectors.toList()));

        mockMvc.perform(get("/api/customer")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].lastName", Matchers.is("Jefferson")))
                .andExpect(jsonPath("$[2].firstName", Matchers.is("John")));
    }

    @Test
    @WithMockUser
    void createCustomer() throws Exception {

        CustomerRequest customer = new CustomerRequest("Mark", "Levine", "mark@gmail.com");

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.multipart("/api/customer")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("firstName", customer.getFirstName())
                .param("lastName", customer.getLastName())
                .param("email", customer.getEmail());

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void createCustomerWithNullFirstName() throws Exception {

        CustomerRequest customer = new CustomerRequest(null, "Levine", "mark@gmail.com");

        mockMvc.perform(multipart("/api/customer")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("firstName", customer.getFirstName())
                .param("lastName", customer.getLastName())
                .param("email", customer.getEmail()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void updateCustomer() throws Exception {
        UpdateCustomerRequest request = UpdateCustomerRequest.builder()
                .firstName("Roger")
                .build();
        when(customerService.updateCustomer(1L, request))
                .thenReturn(CustomerResponse.builder().build());
        mockMvc.perform(put("/api/customer/1")
                .content(convertToJson(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(customerService).updateCustomer(1L, request);
    }

    @Test
    @WithMockUser
    void deleteCustomer() throws Exception {
        mockMvc.perform(delete("/api/customer/{customerId}", 1))
                .andExpect(status().isOk());
        verify(customerService).deleteCustomer(1L);
    }

    @Test
    @WithMockUser
    void deleteCustomerWhoDoesNotExist() throws Exception {
        doThrow(new CustomerNotFoundException()).when(customerService).deleteCustomer(2L);
        mockMvc.perform(delete("/api/customer/{customerId}", 2))
                .andExpect(status().isNotFound());
    }
}