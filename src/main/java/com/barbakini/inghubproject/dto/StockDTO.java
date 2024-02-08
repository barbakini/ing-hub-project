package com.barbakini.inghubproject.dto;

import com.barbakini.inghubproject.jpa.model.Stock;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StockDTO {
    private int count;
    private List<Stock> stocks;
}
