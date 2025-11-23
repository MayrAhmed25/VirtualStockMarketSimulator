package com.oopproject.VirtualStockMarketSimulator.service.npc;

import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import com.oopproject.VirtualStockMarketSimulator.service.TradingService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class TimidTraderStrategy implements INpcTradingStrategy {

    private final Random random = new Random();

    @Override
    public void executeTrade(Long npcId, List<Stock> stocks, TradingService tradingService) {
        for (Stock stock : stocks) {
            // 1. Only look at "Safe" stocks (Low Volatility)
            if (stock.getVolatility().doubleValue() < 0.015) {
                try {
                    // 2. Flip a coin: Buy or Sell?
                    if (random.nextBoolean()) {
                        // Buy small (1 share)
                        tradingService.buyStock(npcId, stock.getId(), 1);
                        System.out.println("🐢 Timid Bot BOUGHT 1 " + stock.getTicker());
                    } else {
                        // Sell small (1 share) - Takes profit
                        tradingService.sellStock(npcId, stock.getId(), 1);
                        System.out.println("🐢 Timid Bot SOLD 1 " + stock.getTicker());
                    }
                } catch (Exception e) {
                    // Ignore errors (e.g., trying to sell stock it doesn't own yet)
                }
            }
        }
    }
}