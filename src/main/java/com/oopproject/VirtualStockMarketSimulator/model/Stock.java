//Entity class Stock (SEE USER FOR ALL DEETS ON JAKARTA & HIBERNATE & WHY AND WHAT)
package com.oopproject.VirtualStockMarketSimulator.model; // Your package

import jakarta.persistence.*; //import the java persistence API
import lombok.Getter; //lombok a java lib; reduces boilerplate code; remove writing setters from code.
//auto generate getters for all attributes of the class
import lombok.Setter;
//auto generate setters for all attributes of the class
import java.math.BigDecimal; // For money and volatility
//big decimal is accurate as opposed to the normal d.p. which is more of an estimate

@Entity
@Table(name = "stocks") // Maps to the 'stocks' table
@Getter
@Setter
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String ticker;

    @Column(name = "company_name", nullable = false, length = 100)
    private String companyName;

    @Column(name = "current_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal currentPrice;
    // precision means the total number of digits in a number
    //scale is the digits after the decimal

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal volatility;

    // this allows for stock prices to be up to 2 d.p
    // volatility needs high accuracy and hence gets 4 dp
    // volatility = how much the stock price fluctuates (higher = more unpredictable)
    // currentPrice = the stock's latest market price stored with decimal precision

}