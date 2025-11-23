//(SEE USER FOR ALL DEETS ON JAKARTA & HIBERNATE & WHY AND WHAT)
package com.oopproject.VirtualStockMarketSimulator.model; // Your package

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_portfolio", uniqueConstraints = {
        // This matches the 'uk_user_stock' unique key in your database
        @UniqueConstraint(columnNames = {"user_id", "stock_id"})
})
@Getter
@Setter
public class UserPortfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity; // Use Integer (or int) for quantity

    // --- Relationship 1: Link to the User ---
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- Relationship 2: Link to the Stock ---
    // --- Relationship 2: Link to the Stock ---
    @ManyToOne(fetch = FetchType.EAGER) // <--- TO THIS
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

}

/*
 * UserPortfolio Entity
 *
 * Maps to the 'user_portfolio' table in the database.
 * Represents the ownership of stocks by a user in the virtual stock market simulator.
 * Each row tracks how many shares of a particular stock a specific user owns.
 *
 * Fields:
 *  - id: Primary key for the portfolio entry. Auto-incremented.
 *  - quantity: The number of shares the user owns of the linked stock.
 *
 * Relationships:
 *  - user: Many portfolio entries belong to one User. Represents which user owns the stock.
 *  - stock: Many portfolio entries belong to one Stock. Represents which stock is owned.
 *
 * Constraints:
 *  - UniqueConstraint(user_id, stock_id): Ensures that a user cannot have multiple rows for the same stock.
 *    Each user-stock combination is unique.
 *
 * Annotations used:
 *  - @Entity: Marks this class as a JPA entity.
 *  - @Table(name = "user_portfolio", uniqueConstraints = {...}): Maps this entity to the 'user_portfolio' table and enforces the unique constraint.
 *  - @Id, @GeneratedValue: Specifies primary key and auto-increment strategy.
 *  - @Column: Defines column properties such as nullable.
 *  - @ManyToOne(fetch = FetchType.LAZY): Establishes a many-to-one relationship with lazy loading.
 *  - @JoinColumn: Specifies the foreign key column in the user_portfolio table.
 *
 * Lombok Annotations:
 *  - @Getter / @Setter: Automatically generates getters and setters for all fields.
 *
 * Notes:
 *  - This entity, together with User and Stock, forms the bridge to track stock ownership.
 *  - quantity is an integer for exact count of shares.
 *  - Lazy fetching ensures that user and stock details are only loaded when explicitly needed.
 */
