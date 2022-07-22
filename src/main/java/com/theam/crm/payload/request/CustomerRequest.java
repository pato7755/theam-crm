package com.theam.crm.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@Builder
public class CustomerRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    public CustomerRequest(@NotBlank String firstName, @NotBlank String lastName, @NotBlank @Email String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
