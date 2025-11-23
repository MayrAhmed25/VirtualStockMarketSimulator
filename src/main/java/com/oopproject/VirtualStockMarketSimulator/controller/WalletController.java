package com.oopproject.VirtualStockMarketSimulator.controller;

import com.oopproject.VirtualStockMarketSimulator.model.User;
import com.oopproject.VirtualStockMarketSimulator.model.UserPortfolio;
import com.oopproject.VirtualStockMarketSimulator.model.Wallet;
import com.oopproject.VirtualStockMarketSimulator.repository.UserPortfolioRepository; // New Import
import com.oopproject.VirtualStockMarketSimulator.repository.UserRepository;
import com.oopproject.VirtualStockMarketSimulator.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final UserPortfolioRepository portfolioRepository; // Inject this

    @GetMapping
    public ResponseEntity<Wallet> getMyWallet() {
        // 1. Get User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Get Wallet
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // --- 3. THE BAILOUT LOGIC ---

        // Check if cash is basically zero (less than $5)
        boolean isBroke = wallet.getCashBalance().compareTo(new BigDecimal("5.00")) < 0;

        // Check if they have NO stocks left to sell
        List<UserPortfolio> holdings = portfolioRepository.findByUserId(user.getId());
        boolean hasNoAssets = holdings.isEmpty() || holdings.stream().allMatch(p -> p.getQuantity() == 0);

        if (isBroke && hasNoAssets) {
            // BANKRUPTCY DETECTED! -> BAILOUT
            wallet.setCashBalance(new BigDecimal("10000.00"));
            walletRepository.save(wallet);
            System.out.println("🚑 BAILOUT triggered for user: " + username);
        }

        return ResponseEntity.ok(wallet);
    }
}