package com.oopproject.VirtualStockMarketSimulator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameClockService {

    private final PriceGeneratorService priceGeneratorService;

    // NOTE: This will only work once we enable scheduling in the main application file!

    /**
     * Runs the price generation logic every 5 seconds (5000 milliseconds)
     */
    @Scheduled(fixedRate = 5000)
    public void runMarketTick() {
        // This is the market "tick"
        priceGeneratorService.generateNextPrice();

        // We will add NPC trading logic here later (in Step 3)
    }
}