package com.oopproject.VirtualStockMarketSimulator.service.npc;

import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import com.oopproject.VirtualStockMarketSimulator.service.TradingService;

import java.util.List;

public interface INpcTradingStrategy {

    /**
     * The method that every NPC personality must implement.
     * * @param npcId The ID of the NPC user performing the trade
     * @param stocks The list of all available stocks in the market
     * @param tradingService The service used to execute the Buy/Sell actions
     */
    void executeTrade(Long npcId, List<Stock> stocks, TradingService tradingService);
}