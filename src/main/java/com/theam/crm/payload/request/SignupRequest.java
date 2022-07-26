package com.theam.crm.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    public SignupRequest(@NotBlank @Size(min = 3, max = 20) String username, @NotBlank @Size(min = 6, max = 40) String password, @NotBlank @Size(max = 50) @Email String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

//    public SignupRequest(@NotBlank @Size(min = 3, max = 20) String username, @NotBlank @Size(min = 6, max = 40) String password, @NotBlank @Size(max = 50) @Email String email, Set<String> role) {
//        this.username = username;
//        this.password = password;
//        this.email = email;
//        this.role = role;
//    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }
}