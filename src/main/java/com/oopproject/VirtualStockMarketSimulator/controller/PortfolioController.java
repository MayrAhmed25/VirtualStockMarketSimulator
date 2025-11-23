package com.oopproject.VirtualStockMarketSimulator.controller;

import com.oopproject.VirtualStockMarketSimulator.model.Transaction; // Import this
import com.oopproject.VirtualStockMarketSimulator.model.User;
import com.oopproject.VirtualStockMarketSimulator.model.UserPortfolio;
import com.oopproject.VirtualStockMarketSimulator.repository.TransactionRepository; // Import this
import com.oopproject.VirtualStockMarketSimulator.repository.UserPortfolioRepository;
import com.oopproject.VirtualStockMarketSimulator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final UserPortfolioRepository userPortfolioRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository; // <--- 1. Inject this

    @GetMapping
    public ResponseEntity<List<UserPortfolio>> getMyPortfolio() {
        User user = getAuthenticatedUser();
        return ResponseEntity.ok(userPortfolioRepository.findByUserId(user.getId()));
    }

    // --- 2. New Endpoint for History ---
    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getMyHistory() {
        User user = getAuthenticatedUser();
        // Returns latest trades first? You might want to sort by date in Repository later
        return ResponseEntity.ok(transactionRepository.findByUserId(user.getId()));
    }

    // Helper method to avoid repeating code
    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}