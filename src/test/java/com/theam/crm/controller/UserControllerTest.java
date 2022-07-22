package com.theam.crm.controller;

import com.theam.crm.exceptions.UserNotFoundException;
import com.theam.crm.model.User;
import com.theam.crm.payload.request.SignupRequest;
import com.theam.crm.payload.request.UpdateUserRequest;
import com.theam.crm.payload.response.UserResponse;
import com.theam.crm.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.theam.crm.TestUtilities.convertToJson;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @MockBean
    private UserService userService;

    User USER_1 = new User("user1", "pass1");
    User USER_2 = new User("user2", "pass2");
    User USER_3 = new User("user3", "pass3");


    @Test
    @WithMockUser(username = "user", password = "pass", roles = "USER")
    void testGetAllCustomersWithoutAdminRole() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void testGetAllCustomersWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllCustomersWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void testGetAllCustomers() throws Exception {
        List<User> records = new ArrayList<>(Arrays.asList(USER_1, USER_2, USER_3));

        when(userService.getAllUsers()).thenReturn(records.stream().map(UserResponse::toUserResponse).collect(Collectors.toList()));

        mockMvc.perform(get("/api/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[1].username", Matchers.is("user2")))
                .andExpect(jsonPath("$[2].username", Matchers.is("user3")));
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void testSizeForGetAllUsers() throws Exception {

        when(userService.getAllUsers()).thenReturn(List.of(
                UserResponse.builder().build(),
                UserResponse.builder().build(),
                UserResponse.builder().build()));

        mockMvc.perform(get("/api/user")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void createUser() throws Exception {

        SignupRequest user = new SignupRequest("userName", "passWord", "email@gmail.com");

        mockMvc.perform(post("/api/user")
                .content(convertToJson(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void createUserWithBlankUsername() throws Exception {

        SignupRequest user = new SignupRequest("", "password", "email@gmail.com");

        mockMvc.perform(post("/api/user")
                .content(convertToJson(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void createUserWithNullUsername() throws Exception {

        SignupRequest user = new SignupRequest(null, "password", "email@gmail.com");

        mockMvc.perform(post("/api/user")
                .content(convertToJson(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void createUserWithInvalidEmail() throws Exception {

        SignupRequest user = new SignupRequest("username", "password", "email");

        mockMvc.perform(post("/api/user")
                .content(convertToJson(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void updateUser() throws Exception {
        UpdateUserRequest request = UpdateUserRequest.builder()
                .username("roger")
                .build();
        when(userService.updateUser(1L, request))
                .thenReturn(UserResponse.builder().build());
        mockMvc.perform(put("/api/user/1")
                .content(convertToJson(request))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(userService).updateUser(1L, request);
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/api/user/{userId}", 1))
                .andExpect(status().isOk());
        verify(userService).deleteUser(1L);
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = "ADMIN")
    void deleteUserWhoDoesNotExist() throws Exception {
        doThrow(new UserNotFoundException()).when(userService).deleteUser(2L);
        mockMvc.perform(delete("/api/user/{userId}", 2))
                .andExpect(status().isNotFound());
    }

}