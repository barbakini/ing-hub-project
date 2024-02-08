package com.barbakini.inghubproject.dto;

import com.barbakini.inghubproject.jpa.model.StockExchange;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExchangeDTO {
    private StockExchange exchange;
    private StockDTO stocks;
}
