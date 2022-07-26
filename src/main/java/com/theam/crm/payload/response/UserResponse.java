package com.theam.crm.payload.response;

import com.theam.crm.model.Role;
import com.theam.crm.model.User;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
public class UserResponse {

    Long id;

    String username;

    String email;

    Set<Role> role;

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRoles())
                .build();
    }
}
