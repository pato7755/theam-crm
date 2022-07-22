package com.theam.crm.payload.response;


import com.theam.crm.model.Customer;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CustomerResponse {

    Long id;

    String firstName;

    String lastName;

    String email;

    String photoUrl;

    public static CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .photoUrl(customer.getPhotoUrl())
                .build();
    }

    public static Customer fromCustomerResponse(CustomerResponse customerResponse) {
        return new Customer(customerResponse.getId(),
                customerResponse.getFirstName(),
                customerResponse.getLastName(),
                customerResponse.getEmail(),
                customerResponse.getPhotoUrl());
    }

}
