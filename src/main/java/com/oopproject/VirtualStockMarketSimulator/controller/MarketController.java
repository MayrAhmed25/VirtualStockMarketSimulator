package com.oopproject.VirtualStockMarketSimulator.controller;

import com.oopproject.VirtualStockMarketSimulator.model.Transaction;
import com.oopproject.VirtualStockMarketSimulator.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final TransactionRepository transactionRepository;

    @GetMapping("/activity")
    public ResponseEntity<List<Transaction>> getMarketActivity() {
        // Fetches ALL transactions from the database (User + NPCs)
        // Sorts them by newest first so the feed looks live
        List<Transaction> allTrades = transactionRepository.findAll(
                Sort.by(Sort.Direction.DESC, "timestamp")
        );

        // In a real app, we'd limit this to 50, but for now, this is perfect
        return ResponseEntity.ok(allTrades);
    }
}