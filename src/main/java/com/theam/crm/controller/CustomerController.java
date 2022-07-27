package com.theam.crm.controller;

import com.theam.crm.payload.request.CustomerRequest;
import com.theam.crm.payload.request.UpdateCustomerRequest;
import com.theam.crm.payload.response.CustomerResponse;
import com.theam.crm.repository.CustomerRepository;
import com.theam.crm.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
@RequestMapping("/api/customer")
public class CustomerController {

    final CustomerService customerService;

    final CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> retrieveAllCustomers() {
        return new ResponseEntity<>(customerService.getAllCustomers(), HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> retrieveCustomerById(@PathVariable(name = "customerId") Long id) {
        return new ResponseEntity<>(customerService.getCustomerById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestParam("firstName") @NotEmpty String firstName,
                                                           @RequestParam("lastName") @NotEmpty String lastName,
                                                           @RequestParam("email") @NotEmpty String email,
                                                           @RequestPart(value = "file") MultipartFile file) {

        CustomerRequest customerRequest = CustomerRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
        CustomerResponse createdCustomer = customerService.createCustomer(customerRequest, file);

        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable(name = "customerId") Long customerId,
                                                           @RequestBody UpdateCustomerRequest updateCustomerRequest) {
        return new ResponseEntity<>(customerService.updateCustomer(customerId, updateCustomerRequest), HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}")
    public void deleteCustomer(@PathVariable(name = "customerId") Long customerId) {
        customerService.deleteCustomer(customerId);
    }

    @PutMapping("/photo/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomerPhoto(@PathVariable(name = "customerId") Long customerId,
                                                                @RequestPart(value = "file") MultipartFile file) {
        return new ResponseEntity<>(customerService.updateCustomerPhoto(customerId, file), HttpStatus.OK);
    }
}