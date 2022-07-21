package com.theam.crm.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

    private String username;

    private String password;

    private Set<String> roles;

}
