package com.barbakini.inghubproject.service;

import com.barbakini.inghubproject.dto.ExchangeDTO;
import com.barbakini.inghubproject.dto.StockExchangeCreateRequest;
import com.barbakini.inghubproject.jpa.model.StockExchange;
import reactor.core.publisher.Mono;

public interface StockExchangeService {

    Mono<StockExchange> createStockExchange(StockExchangeCreateRequest request);

    Mono<ExchangeDTO> getStockExchange(String name);

    Mono<Void> addStockToStockExchange(String name, String stockName);

    Mono<Void> deleteStockToStockExchange(String name, String stockName);
}
