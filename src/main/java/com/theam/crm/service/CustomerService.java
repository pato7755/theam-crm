package com.theam.crm.service;

import com.theam.crm.config.AmazonClient;
import com.theam.crm.config.service.MinioService;
import com.theam.crm.exceptions.CustomerNotFoundException;
import com.theam.crm.model.Customer;
import com.theam.crm.payload.request.CustomerRequest;
import com.theam.crm.payload.request.UpdateCustomerRequest;
import com.theam.crm.payload.response.CustomerResponse;
import com.theam.crm.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final AmazonClient amazonClient;

    private final MinioService minioService;

    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream().map(CustomerResponse::toCustomerResponse).collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isPresent()) return CustomerResponse.toCustomerResponse(customer.get());
        else throw new CustomerNotFoundException();
    }

    public CustomerResponse createCustomer(CustomerRequest customerRequest, MultipartFile multipartFile) {

//        String photoId = amazonClient.uploadFile(multipartFile);
        String photoId = minioService.uploadFile(multipartFile);

        Customer customer = new Customer(customerRequest.getFirstName(),
                customerRequest.getLastName(),
                customerRequest.getEmail(),
                photoId);

        return CustomerResponse.toCustomerResponse(customerRepository.save(customer));
    }

    public CustomerResponse updateCustomer(long customerId, UpdateCustomerRequest request) {
        Optional<Customer> currentCustomer = customerRepository.findById(customerId);
        if (currentCustomer.isEmpty()) {
            throw new CustomerNotFoundException();
        }

        Customer customerInfo = currentCustomer.get();
        customerInfo.setFirstName(request.getFirstName());
        customerInfo.setLastName(request.getLastName());
        customerInfo.setEmail(request.getEmail());

        return CustomerResponse.toCustomerResponse(customerRepository.save(customerInfo));

    }

    public CustomerResponse updateCustomerPhoto(long customerId, MultipartFile multipartFile) {
        Customer currentCustomer = CustomerResponse.fromCustomerResponse(getCustomerById(customerId));
        String photoId = amazonClient.uploadFile(multipartFile);
        currentCustomer.setPhotoUrl(photoId);
        return CustomerResponse.toCustomerResponse(customerRepository.save(currentCustomer));
    }

    public void deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) customerRepository.deleteById(id);
        else throw new CustomerNotFoundException();
    }

}
