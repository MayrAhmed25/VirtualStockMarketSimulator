package com.oopproject.VirtualStockMarketSimulator.service.npc;

import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import com.oopproject.VirtualStockMarketSimulator.service.TradingService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class AggressiveTraderStrategy implements INpcTradingStrategy {

    private final Random random = new Random();

    @Override
    public void executeTrade(Long npcId, List<Stock> stocks, TradingService tradingService) {
        for (Stock stock : stocks) {
            // 1. Check if it's a volatile "Momentum" stock
            if (stock.getVolatility().doubleValue() > 0.015) {
                try {
                    // 50% chance to Buy, 50% chance to Sell (to take profits)
                    boolean shouldBuy = random.nextBoolean();

                    if (shouldBuy) {
                        // Aggressive BUY: 5 shares
                        tradingService.buyStock(npcId, stock.getId(), 5);
                        System.out.println("😈 Aggressive NPC BOUGHT 5 " + stock.getTicker());
                    } else {
                        // Aggressive DUMP: Sell 5 shares (if owned)
                        // Note: This will fail silently if they don't own any, which is fine.
                        tradingService.sellStock(npcId, stock.getId(), 5);
                        System.out.println("😈 Aggressive NPC SOLD 5 " + stock.getTicker());
                    }
                } catch (Exception e) {
                    // Ignore "Insufficient Funds" or "Not Enough Shares" errors
                }
            }
        }
    }
}