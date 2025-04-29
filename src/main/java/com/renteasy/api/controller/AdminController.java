package com.renteasy.api.controller;

import com.renteasy.api.entity.Role;
import com.renteasy.api.entity.User;
import com.renteasy.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin Controller", description = "Admin APIs for managing users, including viewing all users and updating user roles.")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Operation(summary = "Get all users", description = "Fetches a list of all users in the system. Accessible only to admins.")
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Operation(summary = "Update user role", description = "Updates the role of a user by their ID. Accessible only to admins.")
    @PutMapping("/users/{id}")
    public User updateUserRole(
            @Parameter(description = "ID of the user whose role is to be updated")
            @PathVariable String id,

            @Parameter(description = "New role to be assigned to the user")
            @RequestParam String role
    ) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(Role.valueOf(role));
        return userRepository.save(user);
    }
}
