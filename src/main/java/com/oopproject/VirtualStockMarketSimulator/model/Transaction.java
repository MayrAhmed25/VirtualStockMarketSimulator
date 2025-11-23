// Entity class Transaction (SEE USER FOR ALL DEETS ON JAKARTA & HIBERNATE & WHY AND WHAT)
package com.oopproject.VirtualStockMarketSimulator.model; // Your package

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "transactions") // Maps to the 'transactions' table
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // This handles the ENUM('BUY', 'SELL') in MySQL
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 4) // length = 4 to fit 'SELL'
    private TransactionType type;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_at_execution", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtExecution;

    @Column(nullable = false, updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp timestamp;

    // --- Relationship 1: Link to the User ---
    // --- Relationship 1: Link to the User ---
    @ManyToOne(fetch = FetchType.EAGER) // <--- CHANGE THIS TO EAGER
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- Relationship 2: Link to the Stock ---
    // --- Relationship 2: Link to the Stock ---
    @ManyToOne(fetch = FetchType.EAGER) // <--- CHANGE THIS TO EAGER
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    // This is the Java Enum
    public enum TransactionType {
        BUY,
        SELL
    }

    // ... existing code ...

    @PrePersist // <--- ADD THIS ANNOTATION
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        }
    }

}

/*
 * Transaction Entity
 *
 * Maps to the 'transactions' table in the database.
 * Represents a single buy or sell trade performed by a user for a specific stock.
 *
 * Fields:
 *  - id: Primary key for the transaction. Auto-incremented.
 *  - type: The type of transaction, either BUY or SELL. Stored as a string in the database to match ENUM('BUY','SELL').
 *  - quantity: The number of shares traded in this transaction.
 *  - priceAtExecution: The stock price at the exact moment this transaction occurred. Stored as BigDecimal for precision.
 *  - timestamp: The exact time when the transaction was created. Automatically generated and cannot be updated.
 *
 * Relationships:
 *  - user: Many transactions belong to one User. Represents which user made this trade.
 *  - stock: Many transactions belong to one Stock. Represents which stock was traded.
 *
 * Annotations used:
 *  - @Entity: Marks this class as a JPA entity.
 *  - @Table(name = "transactions"): Maps this entity to the 'transactions' table.
 *  - @Id, @GeneratedValue: Specifies primary key and auto-increment strategy.
 *  - @Enumerated(EnumType.STRING): Maps the Java enum TransactionType to a database column as a readable string.
 *  - @Column: Defines column properties such as nullable, length, precision, and scale.
 *  - @ManyToOne(fetch = FetchType.LAZY): Establishes a many-to-one relationship with lazy loading.
 *  - @JoinColumn: Specifies the foreign key column in the transactions table.
 *
 * Lombok Annotations:
 *  - @Getter / @Setter: Automatically generates getters and setters for all fields.
 *
 *
*/