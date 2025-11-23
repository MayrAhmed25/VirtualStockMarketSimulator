package com.oopproject.VirtualStockMarketSimulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling; // <-- Import this

@SpringBootApplication
@EnableScheduling // <-- ADD THIS LINE
public class VirtualStockMarketSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualStockMarketSimulatorApplication.class, args);
    }

}