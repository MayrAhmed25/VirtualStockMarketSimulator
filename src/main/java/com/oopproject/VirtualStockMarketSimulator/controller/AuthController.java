package com.oopproject.VirtualStockMarketSimulator.controller;

import com.oopproject.VirtualStockMarketSimulator.dto.LoginRequest;
import com.oopproject.VirtualStockMarketSimulator.dto.RegisterRequest;
import com.oopproject.VirtualStockMarketSimulator.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 1. Tells Spring this handles HTTP requests
@RequestMapping("/api/auth") // 2. All endpoints here start with /api/auth
@RequiredArgsConstructor // 3. Lombok auto-injects the AuthService
public class AuthController {

    private final AuthService authService; // The service with the logic

    // Handles POST requests to /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        String jwt = authService.register(request);
        return ResponseEntity.ok(jwt); // Returns the JWT token in the response body
    }

    // Handles POST requests to /api/auth/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String jwt = authService.login(request);
        return ResponseEntity.ok(jwt); // Returns the JWT token
    }
}