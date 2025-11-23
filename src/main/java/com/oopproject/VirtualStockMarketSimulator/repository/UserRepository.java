package com.oopproject.VirtualStockMarketSimulator.repository; // Package for all repository interfaces

import com.oopproject.VirtualStockMarketSimulator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*
 * UserRepository
 *
 * This is the **data access layer** for User entities.
 * It connects your Java code to the 'users' table in the database.
 *
 * Key Points:
 * 1. Spring Data JPA automatically implements this interface at runtime, so no SQL is manually written.
 * 2. Extending JpaRepository<User, Long> provides built-in CRUD operations:
 *      - save(user) → insert or update a user
 *      - findById(id) → retrieve a user by primary key
 *      - findAll() → retrieve all users
 *      - delete(user) → delete a user
 * 3. Custom query methods can be defined by method names. Spring automatically generates the SQL.
 *
 * Example usage:
 *  - Checking if a username exists during signup
 *  - Retrieving a user for authentication/login
 *
 * Annotation:
 *  - @Repository marks this interface as a Spring-managed bean for data access.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /*
     * Finds a user by their username.
     * Useful for login or checking uniqueness during registration.
     *
     * Generated SQL example:
     *      SELECT * FROM users WHERE username = ?;
     *
     * @param username The username to search for.
     * @return An Optional containing the User if found, empty if not.
     */
    Optional<User> findByUsername(String username);

}
