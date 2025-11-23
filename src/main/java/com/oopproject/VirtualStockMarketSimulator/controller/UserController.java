package com.oopproject.VirtualStockMarketSimulator.controller;

import com.oopproject.VirtualStockMarketSimulator.model.User;
import com.oopproject.VirtualStockMarketSimulator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/user") // Base path for user data
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // GET request to /api/user/me
    // This uses the JWT token to find the currently authenticated user.
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        // 1. Get the username from the currently active JWT token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 2. Find the full User object in the database
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            // Should theoretically never happen if authentication passed
            return ResponseEntity.notFound().build();
        }
    }
}