package com.theam.crm.config;

import com.theam.crm.payload.request.SignupRequest;
import com.theam.crm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminConfig {

    private final UserService userService;

    @EventListener(ApplicationReadyEvent.class)
    public void setupAdminUser() {

        Set<String> str = new HashSet<>();
        str.add("admin");

        if (userService.getAdminUserCount() == 0) {
            userService.createUser(
                    SignupRequest.builder()
                            .username("admin")
                            .password("admin1234")
                            .email("email@admin.com")
                            .role(str)
                            .build()
            );

        }
    }

}
