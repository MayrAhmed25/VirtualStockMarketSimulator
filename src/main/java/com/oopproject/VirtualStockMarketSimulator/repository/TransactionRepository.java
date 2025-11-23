package com.oopproject.VirtualStockMarketSimulator.repository;

import com.oopproject.VirtualStockMarketSimulator.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserId(Long userId);

    // This custom method allows us to delete all history for a specific user
    void deleteByUserId(Long userId);
}

/*
 * TransactionRepository
 *
 * This is the **data access layer** for Transaction entities.
 * It connects your Java code to the 'transactions' table in the database.
 *
 * Key Points:
 * 1. Spring Data JPA automatically implements this interface at runtime, so no SQL is manually written.
 * 2. Extending JpaRepository<Transaction, Long> provides built-in CRUD operations:
 *      - save(transaction) → insert or update a transaction record
 *      - findById(id) → retrieve a transaction by primary key
 *      - findAll() → retrieve all transactions
 *      - delete(transaction) → delete a transaction record
 * 3. Custom query methods can be defined by method names. Spring generates the SQL automatically.
 *
 * Example usage:
 *  - Fetching all transactions of a user to display their trade history
 *  - Retrieving transactions for analytics or end-of-day portfolio summaries
 *
 * Relationships:
 *  - Each Transaction is linked to a User and a Stock via foreign keys.
 *    This repository allows easy querying without writing raw SQL.
 *
 * Annotation:
 *  - @Repository marks this interface as a Spring-managed bean for data access.
 */


    /*
     * Finds all transactions for a specific user.
     * Useful to show a user's complete trade history.
     *
     * Generated SQL example:
     *      SELECT * FROM transactions WHERE user_id = ?;
     *
     * @param userId The ID of the user whose transactions are requested.
     * @return A list of Transaction entries for the specified user.
     */
