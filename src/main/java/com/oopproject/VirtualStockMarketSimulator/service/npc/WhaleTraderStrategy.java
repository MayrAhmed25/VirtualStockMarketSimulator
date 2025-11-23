package com.oopproject.VirtualStockMarketSimulator.service.npc;

import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import com.oopproject.VirtualStockMarketSimulator.service.TradingService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class WhaleTraderStrategy implements INpcTradingStrategy {

    private final Random random = new Random();

    @Override
    public void executeTrade(Long npcId, List<Stock> stocks, TradingService tradingService) {
        // Whales are lazy. 80% of the time, they do nothing.
        if (random.nextInt(100) < 80) return;

        // Pick a random stock
        Stock stock = stocks.get(random.nextInt(stocks.size()));

        // BIG TRADE: 20 to 50 shares (Impacts price!)
        int quantity = random.nextInt(31) + 20;
        boolean buying = random.nextBoolean();

        try {
            if (buying) {
                tradingService.buyStock(npcId, stock.getId(), quantity);
                System.out.println("🐋 WHALE SPLASH: Bought " + quantity + " " + stock.getTicker());
            } else {
                tradingService.sellStock(npcId, stock.getId(), quantity);
                System.out.println("🐋 WHALE DUMP: Sold " + quantity + " " + stock.getTicker());
            }
        } catch (Exception e) {
            // Whales ignore errors
        }
    }
}