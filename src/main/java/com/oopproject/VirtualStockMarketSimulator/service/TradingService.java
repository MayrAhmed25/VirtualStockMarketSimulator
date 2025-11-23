package com.oopproject.VirtualStockMarketSimulator.service;

import com.oopproject.VirtualStockMarketSimulator.model.*;
import com.oopproject.VirtualStockMarketSimulator.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TradingService {

    private final WalletRepository walletRepository;
    private final StockRepository stockRepository;
    private final UserPortfolioRepository userPortfolioRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    /**
     * Handles the logic for Buying a stock
     */
    @Transactional
    public void buyStock(Long userId, Long stockId, int quantity) {
        // 1. Validate Inputs
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        // 2. Calculate Total Cost
        BigDecimal totalCost = stock.getCurrentPrice().multiply(new BigDecimal(quantity));

        // 3. Check Balance
        if (wallet.getCashBalance().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient funds!");
        }

        // 4. Deduct Money
        wallet.setCashBalance(wallet.getCashBalance().subtract(totalCost));
        walletRepository.save(wallet);

        // 5. Update Portfolio (Add Shares)
        UserPortfolio portfolio = userPortfolioRepository.findByUserIdAndStockId(userId, stockId)
                .orElse(new UserPortfolio());

        if (portfolio.getUser() == null) {
            portfolio.setUser(user);
            portfolio.setStock(stock);
            portfolio.setQuantity(0);
        }
        portfolio.setQuantity(portfolio.getQuantity() + quantity);
        userPortfolioRepository.save(portfolio);

        // 6. APPLY PRICE IMPACT (Buying drives price UP)
        applyPriceImpact(stock, quantity, true);

        // 7. Log Transaction
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setStock(stock);
        transaction.setType(Transaction.TransactionType.BUY);
        transaction.setQuantity(quantity);
        transaction.setPriceAtExecution(stock.getCurrentPrice()); // Log the price at moment of trade
        transaction.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        transactionRepository.save(transaction);
    }

    /**
     * Handles the logic for Selling a stock
     */
    @Transactional
    public void sellStock(Long userId, Long stockId, int quantity) {
        // 1. Validate Inputs
        Stock stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
        UserPortfolio portfolio = userPortfolioRepository.findByUserIdAndStockId(userId, stockId)
                .orElseThrow(() -> new RuntimeException("You don't own this stock!"));

        // 2. Check Share Quantity
        if (portfolio.getQuantity() < quantity) {
            throw new RuntimeException("Not enough shares to sell!");
        }

        // 3. Calculate Total Value
        BigDecimal totalValue = stock.getCurrentPrice().multiply(new BigDecimal(quantity));

        // 4. Add Money
        wallet.setCashBalance(wallet.getCashBalance().add(totalValue));
        walletRepository.save(wallet);

        // 5. Update Portfolio (Remove Shares)
        portfolio.setQuantity(portfolio.getQuantity() - quantity);
        if (portfolio.getQuantity() == 0) {
            userPortfolioRepository.delete(portfolio);
        } else {
            userPortfolioRepository.save(portfolio);
        }

        // 6. APPLY PRICE IMPACT (Selling drives price DOWN)
        applyPriceImpact(stock, quantity, false);

        // 7. Log Transaction
        // We need to fetch the user again because we only looked up the wallet/portfolio
        User user = userRepository.findById(userId).orElseThrow();

        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setStock(stock);
        transaction.setType(Transaction.TransactionType.SELL);
        transaction.setQuantity(quantity);
        transaction.setPriceAtExecution(stock.getCurrentPrice());
        transaction.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        transactionRepository.save(transaction);
    }

    /**
     * Helper method: Adjusts stock price based on Supply and Demand.
     * @param isBuy If true, price goes UP. If false, price goes DOWN.
     */
    private void applyPriceImpact(Stock stock, int quantity, boolean isBuy) {
        // Impact Factor: How much 1 share moves the needle.
        // 0.0005 means 1 share moves price by 0.05%.
        // 100 shares (Whale) moves it by 5%.
        double impactFactor = 0.0005;

        double percentChange = quantity * impactFactor;

        BigDecimal currentPrice = stock.getCurrentPrice();
        BigDecimal changeAmount = currentPrice.multiply(new BigDecimal(percentChange));

        BigDecimal newPrice;
        if (isBuy) {
            newPrice = currentPrice.add(changeAmount);
        } else {
            newPrice = currentPrice.subtract(changeAmount);
        }

        // Safety check: Price cannot be negative or zero
        if (newPrice.compareTo(new BigDecimal("0.01")) < 0) {
            newPrice = new BigDecimal("0.01");
        }

        // Round to 2 decimals
        newPrice = newPrice.setScale(2, RoundingMode.HALF_UP);

        stock.setCurrentPrice(newPrice);
        stockRepository.save(stock);
    }
}