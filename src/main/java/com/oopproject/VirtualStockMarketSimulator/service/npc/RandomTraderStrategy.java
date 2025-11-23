package com.oopproject.VirtualStockMarketSimulator.service.npc;

import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import com.oopproject.VirtualStockMarketSimulator.service.TradingService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component // Makes this available for Spring to use
public class RandomTraderStrategy implements INpcTradingStrategy {

    private final Random random = new Random();

    @Override
    public void executeTrade(Long npcId, List<Stock> stocks, TradingService tradingService) {
        // 1. Pick a random stock from the list
        if (stocks.isEmpty()) return;
        Stock randomStock = stocks.get(random.nextInt(stocks.size()));

        // 2. Decide randomly: Buy (0) or Sell (1)
        boolean isBuying = random.nextBoolean();

        // 3. Decide a random quantity (1 to 5 shares)
        int quantity = random.nextInt(5) + 1;

        try {
            if (isBuying) {
                // Try to buy
                tradingService.buyStock(npcId, randomStock.getId(), quantity);
                System.out.println("NPC (Random) BOUGHT " + quantity + " of " + randomStock.getTicker());
            } else {
                // Try to sell (this might fail if they don't own it, which is fine!)
                tradingService.sellStock(npcId, randomStock.getId(), quantity);
                System.out.println("NPC (Random) SOLD " + quantity + " of " + randomStock.getTicker());
            }
        } catch (Exception e) {
            // NPCs will fail often (e.g., trying to sell stock they don't have).
            // We catch the error silently so the game loop doesn't crash.
            // System.out.println("NPC Trade Failed: " + e.getMessage());
        }
    }
}