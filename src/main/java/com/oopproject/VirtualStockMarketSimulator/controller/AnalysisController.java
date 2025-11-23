package com.oopproject.VirtualStockMarketSimulator.controller;

import com.oopproject.VirtualStockMarketSimulator.model.User;
import com.oopproject.VirtualStockMarketSimulator.model.UserPortfolio;
import com.oopproject.VirtualStockMarketSimulator.model.Wallet;
import com.oopproject.VirtualStockMarketSimulator.repository.UserPortfolioRepository;
import com.oopproject.VirtualStockMarketSimulator.repository.UserRepository;
import com.oopproject.VirtualStockMarketSimulator.repository.WalletRepository;
import lombok.Data;
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
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final UserPortfolioRepository portfolioRepository;

    // Helper class to structure the JSON response
    @Data
    static class GameSummary {
        private BigDecimal finalCash;
        private BigDecimal portfolioValue;
        private BigDecimal totalNetWorth;
        private BigDecimal profitLoss;
        private String performanceGrade;
    }

    @GetMapping("/summary")
    public ResponseEntity<GameSummary> getGameSummary() {
        // 1. Get Current User
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();

        // 2. Get Wallet & Stocks
        Wallet wallet = walletRepository.findByUserId(user.getId()).orElseThrow();
        List<UserPortfolio> holdings = portfolioRepository.findByUserId(user.getId());

        BigDecimal cash = wallet.getCashBalance();
        BigDecimal assets = BigDecimal.ZERO;

        // 3. Calculate Total Asset Value (Shares * Current Price)
        for (UserPortfolio p : holdings) {
            BigDecimal stockVal = p.getStock().getCurrentPrice().multiply(new BigDecimal(p.getQuantity()));
            assets = assets.add(stockVal);
        }

        // 4. Calculate Stats
        BigDecimal total = cash.add(assets);
        BigDecimal initial = new BigDecimal("10000.00"); // Everyone started with 10k
        BigDecimal pl = total.subtract(initial);

        // 5. Assign a Grade
        String grade = "Neutral";
        double profit = pl.doubleValue();

        if (profit > 2000) grade = "Market Legend 🚀";
        else if (profit > 500) grade = "Wall Street Wolf 🐺";
        else if (profit > 0) grade = "Profitable ✅";
        else if (profit > -500) grade = "Bad Day 📉";
        else grade = "Bankrupt 💀";

        // 6. Build Response
        GameSummary summary = new GameSummary();
        summary.setFinalCash(cash);
        summary.setPortfolioValue(assets);
        summary.setTotalNetWorth(total);
        summary.setProfitLoss(pl);
        summary.setPerformanceGrade(grade);

        return ResponseEntity.ok(summary);
    }
}