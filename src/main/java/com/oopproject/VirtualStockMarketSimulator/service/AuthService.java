package com.oopproject.VirtualStockMarketSimulator.service;

import com.oopproject.VirtualStockMarketSimulator.dto.LoginRequest;
import com.oopproject.VirtualStockMarketSimulator.dto.RegisterRequest;
import com.oopproject.VirtualStockMarketSimulator.model.User;
import com.oopproject.VirtualStockMarketSimulator.model.Wallet;
import com.oopproject.VirtualStockMarketSimulator.repository.UserRepository;
import com.oopproject.VirtualStockMarketSimulator.repository.WalletRepository;
import com.oopproject.VirtualStockMarketSimulator.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // --- 1. REGISTRATION LOGIC ---
    public String register(RegisterRequest request) {

        // 1. Create a new User object
        User user = new User();
        user.setUsername(request.getUsername());

        // 2. Hash the password before saving!
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        // 3. Save the new User to the database
        userRepository.save(user);

        // 4. Create the corresponding starting Wallet
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setCashBalance(new BigDecimal("10000.00")); // Starts with $10,000.00
        walletRepository.save(wallet);

        // 5. Generate a JWT token
        return jwtService.generateToken(user);
    }

    // --- 2. LOGIN LOGIC ---
    public String login(LoginRequest request) {

        // FIX IS HERE: We use 'User' (our class), not 'UserDetails' (the interface)
        // This allows us to access .getPasswordHash()
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Verify the password
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid Credentials");
        }

        // 3. If valid, generate the token
        return jwtService.generateToken(user);
    }
}