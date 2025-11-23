package com.oopproject.VirtualStockMarketSimulator.controller;

import com.oopproject.VirtualStockMarketSimulator.model.Transaction;
import com.oopproject.VirtualStockMarketSimulator.model.User;
import com.oopproject.VirtualStockMarketSimulator.model.UserPortfolio;
import com.oopproject.VirtualStockMarketSimulator.model.Wallet;
import com.oopproject.VirtualStockMarketSimulator.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserPortfolioRepository portfolioRepository;

    @PostMapping("/reset")
    @Transactional
    public ResponseEntity<String> resetGame() {
        // 1. Get User
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. MANUAL DELETE: Transactions
        // We fetch them first, then delete them. This avoids the "Method Not Found" issues.
        List<Transaction> userTrades = transactionRepository.findByUserId(user.getId());
        transactionRepository.deleteAll(userTrades);

        // 3. MANUAL DELETE: Portfolio
        List<UserPortfolio> userHoldings = portfolioRepository.findByUserId(user.getId());
        portfolioRepository.deleteAll(userHoldings);

        // 4. Reset Wallet to $10,000
        Wallet wallet = walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setCashBalance(new BigDecimal("10000.00"));
        walletRepository.save(wallet);

        return ResponseEntity.ok("Game Reset Successfully!");
    }
}