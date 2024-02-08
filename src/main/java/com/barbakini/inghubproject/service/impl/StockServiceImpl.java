package com.barbakini.inghubproject.service.impl;

import com.barbakini.inghubproject.dto.StockCreateRequest;
import com.barbakini.inghubproject.dto.StockUpdateRequest;
import com.barbakini.inghubproject.jpa.model.Stock;
import com.barbakini.inghubproject.jpa.repository.StockExchangeRepository;
import com.barbakini.inghubproject.jpa.repository.StockRepository;
import com.barbakini.inghubproject.service.StockService;
import com.barbakini.inghubproject.util.EntityTypeEnum;
import com.barbakini.inghubproject.util.exceptions.EntityAlreadyExistException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockExchangeRepository stockExchangeRepository;

    @Override
    public Mono<Stock> createStock(StockCreateRequest request) {
        Stock newStock = Stock.builder()
                .name(request.getName())
                .description(request.getDescription())
                .currentPrice(request.getCurrentPrice())
                .lastUpdate(LocalDateTime.now()).build();
        return stockRepository.save(newStock)
                .onErrorResume(DuplicateKeyException.class, (e) ->
                        Mono.error(new EntityAlreadyExistException(request.getName(), EntityTypeEnum.STOCK)));
    }

    @Override
    public Mono<Void> deleteStock(String name) {
        return stockRepository.deleteByName(name)
                .then(stockExchangeRepository.updateLiveInStatus());
    }

    @Override
    public Mono<Void> updateStock(StockUpdateRequest request) {
        return stockRepository.updatePrice(request.getPrice(), LocalDateTime.now(), request.getName()).then();
    }

    @Override
    public Mono<Stock> getStock(String name) {
        return stockRepository.findByName(name);
    }
}
