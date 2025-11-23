package com.oopproject.VirtualStockMarketSimulator.controller;

import com.oopproject.VirtualStockMarketSimulator.model.User;
import com.oopproject.VirtualStockMarketSimulator.repository.UserRepository;
import com.oopproject.VirtualStockMarketSimulator.service.TradingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService;
    private final UserRepository userRepository;

    // Defines the JSON structure we expect from the frontend
    @Data
    public static class TradeRequest {
        private Long stockId;
        private int quantity;
    }

    @PostMapping("/buy")
    public ResponseEntity<String> buyStock(@RequestBody TradeRequest request) {
        Long userId = getCurrentUserId();
        tradingService.buyStock(userId, request.getStockId(), request.getQuantity());
        return ResponseEntity.ok("Stock bought successfully!");
    }

    @PostMapping("/sell")
    public ResponseEntity<String> sellStock(@RequestBody TradeRequest request) {
        Long userId = getCurrentUserId();
        tradingService.sellStock(userId, request.getStockId(), request.getQuantity());
        return ResponseEntity.ok("Stock sold successfully!");
    }

    // Helper method to get the logged-in user's ID from the JWT
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }
}