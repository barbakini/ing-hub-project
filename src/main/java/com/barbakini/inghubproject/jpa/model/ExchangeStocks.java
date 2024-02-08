package com.barbakini.inghubproject.jpa.model;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("exchange_stocks")
@Builder
public class ExchangeStocks {
    @Column("stock_id")
    private Long stockId;
    @Column("exchange_id")
    private Long exchangeId;
}
