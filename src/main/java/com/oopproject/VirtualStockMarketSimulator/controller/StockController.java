package com.oopproject.VirtualStockMarketSimulator.controller;

import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import com.oopproject.VirtualStockMarketSimulator.model.StockPriceHistory;
import com.oopproject.VirtualStockMarketSimulator.repository.StockPriceHistoryRepository;
import com.oopproject.VirtualStockMarketSimulator.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // Added Safety Net for CORS
public class StockController {

    private final StockRepository stockRepository;
    private final StockPriceHistoryRepository stockPriceHistoryRepository;

    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        return ResponseEntity.ok(stockRepository.findAll());
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<StockPriceHistory>> getStockHistory(@PathVariable Long id) {
        System.out.println("Request received for History of Stock ID: " + id); // LOGGING ADDED
        List<StockPriceHistory> history = stockPriceHistoryRepository.findByStockId(id);
        return ResponseEntity.ok(history);
    }
}