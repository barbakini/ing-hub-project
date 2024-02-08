package com.barbakini.inghubproject.service;

import com.barbakini.inghubproject.dto.StockCreateRequest;
import com.barbakini.inghubproject.dto.StockUpdateRequest;
import com.barbakini.inghubproject.jpa.model.Stock;
import reactor.core.publisher.Mono;

public interface StockService {

    Mono<Stock> createStock(StockCreateRequest request);

    Mono<Void> deleteStock(String name);

    Mono<Void> updateStock(StockUpdateRequest request);

    Mono<Stock> getStock(String name);
}
