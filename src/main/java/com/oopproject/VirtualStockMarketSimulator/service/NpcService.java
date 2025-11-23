package com.oopproject.VirtualStockMarketSimulator.service;

import com.oopproject.VirtualStockMarketSimulator.dto.RegisterRequest;
import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import com.oopproject.VirtualStockMarketSimulator.repository.StockRepository;
import com.oopproject.VirtualStockMarketSimulator.repository.UserRepository;
import com.oopproject.VirtualStockMarketSimulator.service.npc.INpcTradingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class NpcService {

    private final UserRepository userRepository;
    private final AuthService authService;
    private final StockRepository stockRepository;
    private final TradingService tradingService;

    // 1. Inject a LIST of all strategies found in your project
    private final List<INpcTradingStrategy> allStrategies;

    // Map to store Bot Name -> Database ID
    private final Map<String, Long> botIds = new ConcurrentHashMap<>();

    @Scheduled(initialDelay = 1000, fixedRate = 10000000) // Run once on startup
    public void initializeNpcs() {
        System.out.println("🤖 Initializing " + allStrategies.size() + " NPC Strategies...");

        for (INpcTradingStrategy strategy : allStrategies) {
            // Generate a name based on the class name (e.g., "WhaleTraderStrategy" -> "Bot_WhaleTrader")
            String botName = "Bot_" + strategy.getClass().getSimpleName().replace("Strategy", "");

            // Check if this bot exists in DB, if not, create it
            if (userRepository.findByUsername(botName).isEmpty()) {
                RegisterRequest request = new RegisterRequest(botName, "npc_pass_123");
                authService.register(request);
                System.out.println("   -> Created new user: " + botName);
            }

            // Store the ID in memory so we don't have to look it up every time
            Long id = userRepository.findByUsername(botName).get().getId();
            botIds.put(strategy.getClass().getName(), id);
        }
    }

    @Scheduled(fixedRate = 5000) // Run every 5 seconds
    public void runAllNpcTrades() {
        if (botIds.isEmpty()) return;

        List<Stock> allStocks = stockRepository.findAll();

        // 2. Loop through EVERY strategy and let them take a turn
        for (INpcTradingStrategy strategy : allStrategies) {
            Long botId = botIds.get(strategy.getClass().getName());

            if (botId != null) {
                // Execute the trade logic for this specific bot
                strategy.executeTrade(botId, allStocks, tradingService);
            }
        }
    }
}