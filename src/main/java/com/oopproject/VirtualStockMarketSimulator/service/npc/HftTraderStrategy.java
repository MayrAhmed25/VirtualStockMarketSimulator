package com.oopproject.VirtualStockMarketSimulator.service.npc;

import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import com.oopproject.VirtualStockMarketSimulator.service.TradingService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class HftTraderStrategy implements INpcTradingStrategy {

    private final Random random = new Random();

    @Override
    public void executeTrade(Long npcId, List<Stock> stocks, TradingService tradingService) {
        // Iterate through EVERY stock in the market
        for (Stock stock : stocks) {
            try {
                // HFTs trade fast and random directions
                if (random.nextBoolean()) {
                    tradingService.buyStock(npcId, stock.getId(), 1);
                } else {
                    tradingService.sellStock(npcId, stock.getId(), 1);
                }
            } catch (Exception e) {
                // HFTs fail constantly (trying to sell what they don't have),
                // but when they succeed, they fill the logs.
            }
        }
        // We remove the print statement inside the loop to avoid 1000 lines of logs
        System.out.println("⚡ HFT Bot executed high-speed sweep.");
    }
}