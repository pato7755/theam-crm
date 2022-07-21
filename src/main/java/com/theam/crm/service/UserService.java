package com.theam.crm.service;

import com.theam.crm.exceptions.UserNotFoundException;
import com.theam.crm.exceptions.UsernameAlreadyExistsException;
import com.theam.crm.model.Role;
import com.theam.crm.model.RoleDescription;
import com.theam.crm.model.User;
import com.theam.crm.payload.request.SignupRequest;
import com.theam.crm.payload.request.UpdateUserRequest;
import com.theam.crm.payload.response.UserResponse;
import com.theam.crm.repository.RoleRepository;
import com.theam.crm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private static final String ROLE_NOT_FOUND_ERROR = "Error: Role is not found.";

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(UserResponse::toUserResponse).collect(Collectors.toList());
    }

    public UserResponse createUser(SignupRequest signUpRequest) {

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                passwordEncoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();

        user.setRoles(getRoleFromRequest(strRoles));

        return UserResponse.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(long userId, UpdateUserRequest request) {
        Optional<User> currentUser = userRepository.findById(userId);
        if (currentUser.isEmpty()) throw new UserNotFoundException();

        Optional<User> user = userRepository.findByUsername(request.getUsername());
        if (user.isPresent() && (user.get().getId() != userId)) {
            throw new UsernameAlreadyExistsException();
        }

        User userDetails = currentUser.get();
        userDetails.setUsername(request.getUsername());
        userDetails.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<String> strRoles = request.getRoles();

        userDetails.setRoles(getRoleFromRequest(strRoles));

        return UserResponse.toUserResponse(userRepository.save(userDetails));
    }

    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) userRepository.deleteById(id);
        else throw new UserNotFoundException();
    }

    private Set<Role> getRoleFromRequest(Set<String> strRoles) {

        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleDescription.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                if ("admin".equals(role)) {
                    Role adminRole = roleRepository.findByName(RoleDescription.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
                    roles.add(adminRole);
                } else {
                    Role userRole = roleRepository.findByName(RoleDescription.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
                    roles.add(userRole);
                }
            });
        }
        return roles;
    }

}
