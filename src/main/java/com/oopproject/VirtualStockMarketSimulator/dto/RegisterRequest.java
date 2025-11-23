//dto stands for data transfer objects
package com.oopproject.VirtualStockMarketSimulator.dto; // Your package

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Lombok annotations for clean code
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String username;
    private String password;

    // We don't need 'passwordHash' here because the frontend sends the plain password
    // Our backend service will use the PasswordEncoder to hash it later.
}