package com.renteasy.api.service;

import com.renteasy.api.entity.Role;
import com.renteasy.api.entity.User;
import com.renteasy.api.payload.request.RegisterRequest;
import com.renteasy.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> registerUser(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        User user = new User(registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword()),
                Role.valueOf(registerRequest.getRole()));

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }
}