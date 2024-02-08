package com.barbakini.inghubproject.jpa.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("stock_exchange")
@Builder
public class StockExchange {
    @Id
    @Column("id")
    private Long id;
    @Column("name")
    private String name;
    @Column("description")
    private String description;
    @Column("live_in_market")
    private boolean liveInMarket;
}
