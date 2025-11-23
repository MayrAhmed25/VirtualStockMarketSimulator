//(SEE USER FOR ALL DEETS ON JAKARTA & HIBERNATE & WHY AND WHAT)
package com.oopproject.VirtualStockMarketSimulator.model; // Your package

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal; // Import this for money

@Entity
@Table(name = "wallets") // Maps to the 'wallets' table
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cash_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal cashBalance;

    // --- This is the new relationship part ---

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

}
/*
 * StockPriceHistory Entity
 *
 * Maps to the 'stock_price_history' table in the database.
 * Represents a historical record of stock prices over time.
 * Each row logs the price of a specific stock at a specific timestamp, allowing charting and analytics.
 *
 * Fields:
 *  - id: Primary key for the history entry. Auto-incremented.
 *  - price: The stock price at the moment this record was created. Stored as BigDecimal for precision.
 *  - timestamp: The exact time this price was recorded. Automatically generated and immutable.
 *
 * Relationships:
 *  - stock: Many price history entries belong to one Stock. Represents which stock the price belongs to.
 *
 * Annotations used:
 *  - @Entity: Marks this class as a JPA entity.
 *  - @Table(name = "stock_price_history"): Maps this entity to the 'stock_price_history' table.
 *  - @Id, @GeneratedValue: Specifies primary key and auto-increment strategy.
 *  - @Column: Defines column properties such as nullable, precision, scale, and default values.
 *  - @ManyToOne(fetch = FetchType.LAZY): Establishes a many-to-one relationship with lazy loading.
 *  - @JoinColumn: Specifies the foreign key column 'stock_id' linking to the Stock entity.
 *
 * Lombok Annotations:
 *  - @Getter / @Setter: Automatically generates getters and setters for all fields.
 *
 * Notes:
 *  - This table is critical for displaying stock charts and analyzing trends.
 *  - Lazy fetching ensures the Stock object is loaded only when explicitly requested.
 *  - Every time the PriceGeneratorService updates a stock's current price, a new StockPriceHistory record is created.
 */
