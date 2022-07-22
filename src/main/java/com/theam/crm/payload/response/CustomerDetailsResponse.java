package com.theam.crm.payload.response;

import com.theam.crm.model.Customer;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CustomerDetailsResponse {

    Long id;

    String firstName;

    String lastName;

    String email;

    String photoUrl;

    public static CustomerDetailsResponse toCustomerDetailsResponse(Customer customer) {
        return CustomerDetailsResponse.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .photoUrl(customer.getPhotoUrl())
                .build();
    }
}
