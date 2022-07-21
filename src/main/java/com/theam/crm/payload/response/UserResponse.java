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

    Set<Role> role;

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRoles())
                .build();
    }
}
