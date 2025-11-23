package com.oopproject.VirtualStockMarketSimulator.repository; // Package for all repository interfaces

import com.oopproject.VirtualStockMarketSimulator.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 * StockRepository
 *
 * This is the **data access layer** for Stock entities.
 * It connects your Java code to the 'stocks' table in the database.
 *
 * Key Points:
 * 1. Spring Data JPA automatically implements this interface at runtime, so you don't write SQL manually.
 * 2. Extending JpaRepository<Stock, Long> provides built-in CRUD operations:
 *      - save(stock) → insert or update a stock record
 *      - findById(id) → retrieve a stock by primary key
 *      - findAll() → retrieve all stocks
 *      - delete(stock) → delete a stock record
 * 3. No custom methods are needed yet, because all basic operations are already provided by JpaRepository.
 *
 * Example usage:
 *  - Fetching a list of all available stocks to display on the frontend dashboard
 *  - Updating a stock's current price in the PriceGeneratorService
 *  - Finding a stock by ID for trading or portfolio updates
 *
 * Annotation:
 *  - @Repository marks this interface as a Spring-managed bean for data access.
 *
 * Notes:
 *  - If you later need custom queries (e.g., find stocks by volatility > 0.02),
 *    you can add method signatures here and Spring Data JPA will automatically generate the SQL.
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // No custom methods yet — JpaRepository provides all basic CRUD operations

}
