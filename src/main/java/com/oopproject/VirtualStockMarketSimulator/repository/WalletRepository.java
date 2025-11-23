package com.oopproject.VirtualStockMarketSimulator.repository;

import com.oopproject.VirtualStockMarketSimulator.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    // We can add findByUserId later, but JpaRepository is enough for now
    Optional<Wallet> findByUserId(Long userId);
}

/*
 * WalletRepository
 *
 * This is the **data access layer** for Wallet entities.
 * It connects your Java code to the 'wallets' table in the database.
 *
 * Key Points:
 * 1. Spring Data JPA automatically implements this interface at runtime, so no SQL is manually written.
 * 2. Extending JpaRepository<Wallet, Long> provides built-in CRUD operations:
 *      - save(wallet) → insert or update a wallet
 *      - findById(id) → retrieve a wallet by primary key
 *      - findAll() → retrieve all wallets
 *      - delete(wallet) → delete a wallet
 * 3. Custom query methods can be defined by method names. Spring generates the SQL automatically.
 *
 * Example usage:
 *  - Fetching a user's wallet to check their cash balance before buying stocks
 *  - Updating a wallet after a trade (buy/sell)
 *
 * Relationships:
 *  - Each Wallet is linked to a User via user_id (one-to-one relationship).
 *    This repository allows easy querying without writing raw SQL.
 *
 * Annotation:
 *  - @Repository marks this interface as a Spring-managed bean for data access.
 */

    /*
     * Finds a wallet by the associated user ID.
     * Useful for accessing a user's cash balance during trading or portfolio updates.
     *
     * Generated SQL example:
     *      SELECT * FROM wallets WHERE user_id = ?;
     *
     * @param userId The ID of the user whose wallet is requested.
     * @return An Optional containing the Wallet if found, empty if not.
     */

