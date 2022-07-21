package com.theam.crm.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class UpdateCustomerRequest {

    private String firstName;

    private String lastName;

    private String email;

}
