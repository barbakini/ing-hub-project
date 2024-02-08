package com.barbakini.inghubproject.jpa.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("stock")
@Builder
public class Stock {
    @Id
    @Column("id")
    private Long id;
    @Column("name")
    private String name;
    @Column("description")
    private String description;
    @Column("current_price")
    private BigDecimal currentPrice;
    @Column("last_update")
    private LocalDateTime lastUpdate;
}
