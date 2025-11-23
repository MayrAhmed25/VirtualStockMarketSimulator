package com.oopproject.VirtualStockMarketSimulator.service;

import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import com.oopproject.VirtualStockMarketSimulator.model.StockPriceHistory;
import com.oopproject.VirtualStockMarketSimulator.repository.StockPriceHistoryRepository;
import com.oopproject.VirtualStockMarketSimulator.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PriceGeneratorService {

    private final StockRepository stockRepository;
    private final StockPriceHistoryRepository historyRepository;
    private final Random random = new Random();

    /**
     * The simple random walk formula: newPrice = oldPrice + (randomValue * volatility)
     */
    public void generateNextPrice() {
        // 1. Fetch all stocks currently in the market
        List<Stock> stocks = stockRepository.findAll();

        for (Stock stock : stocks) {
            // Get the stock's attributes
            BigDecimal oldPrice = stock.getCurrentPrice();
            BigDecimal volatility = stock.getVolatility();

            // Calculate a random change based on volatility
            // Random value between -0.5 and 0.5
            double randomFactor = random.nextDouble() - 0.5;

            // Calculate the change: change = oldPrice * randomFactor * volatility
            BigDecimal priceChange = oldPrice
                    .multiply(new BigDecimal(randomFactor))
                    .multiply(volatility)
                    .setScale(2, RoundingMode.HALF_UP);

            // Calculate the new price
            BigDecimal newPrice = oldPrice.add(priceChange);

            // Ensure price doesn't go below zero (simple safety check)
            if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
                newPrice = new BigDecimal("0.01");
            }

            // 2. Update the Stock table with the new price
            stock.setCurrentPrice(newPrice);
            stockRepository.save(stock);

            // 3. Log the price change in the history table
            StockPriceHistory history = new StockPriceHistory();
            history.setStock(stock);
            history.setPrice(newPrice);
            historyRepository.save(history);
        }
    }
}