package com.oopproject.VirtualStockMarketSimulator.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // <--- IMPORT THIS
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "stock_price_history")
@Getter
@Setter
public class StockPriceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, updatable = false)
    private Timestamp timestamp;

    @PrePersist
    protected void onCreate() {
        if (this.timestamp == null) {
            this.timestamp = new java.sql.Timestamp(System.currentTimeMillis());
        }
    }

    // --- THE FIX IS HERE ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    @JsonIgnore // <--- ADD THIS LINE. It stops the infinite loop/proxy error.
    private Stock stock;

}