package com.oopproject.VirtualStockMarketSimulator.repository;

import com.oopproject.VirtualStockMarketSimulator.model.UserPortfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPortfolioRepository extends JpaRepository<UserPortfolio, Long> {

    List<UserPortfolio> findByUserId(Long userId);

    Optional<UserPortfolio> findByUserIdAndStockId(Long userId, Long stockId);

    // This custom method allows us to wipe the portfolio for a fresh start
    void deleteByUserId(Long userId);
}
/*
 * UserPortfolioRepository
 *
 * This is the **data access layer** for UserPortfolio entities.
 * It connects your Java code to the 'user_portfolio' table in the database.
 *
 * Key Points:
 * 1. Spring Data JPA automatically implements this interface at runtime, so no SQL is manually written.
 * 2. Extending JpaRepository<UserPortfolio, Long> provides built-in CRUD methods:
 *      - save(entity) → insert or update a portfolio entry
 *      - findById(id) → retrieve a portfolio entry by primary key
 *      - findAll() → retrieve all portfolio entries
 *      - delete(entity) → delete a portfolio entry
 * 3. You can define **custom query methods** just by their method names. Spring generates the SQL automatically.
 *
 * Example usage of this repository:
 *   - Fetching all stocks a user owns to display their portfolio
 *   - Retrieving a specific stock holding to update quantity when buying/selling
 *
 * Relationships:
 *  - UserPortfolio entries are linked to both User and Stock entities via foreign keys.
 *    This repository allows efficient querying of a user's holdings without raw SQL.
 *
 * Annotation:
 *  - @Repository marks this interface as a Spring bean for data access.
 */


    /**
     * Finds all portfolio entries for a specific user.
     * Useful to get the full list of a user's stock holdings.
     *
     * Generated SQL example:
     *      SELECT * FROM user_portfolio WHERE user_id = ?;
     *
     * @param userId The ID of the user whose portfolio is requested.
     * @return A list of UserPortfolio entries for the specified user.
     */


    /*
     * Finds a specific portfolio entry for a user and stock.
     * Useful to check if the user already owns a stock before buying/selling.
     *
     * Generated SQL example:
     *      SELECT * FROM user_portfolio WHERE user_id = ? AND stock_id = ?;
     *
     * @param userId  The ID of the user
     * @param stockId The ID of the stock
     * @return An Optional containing the UserPortfolio entry if it exists
     */
