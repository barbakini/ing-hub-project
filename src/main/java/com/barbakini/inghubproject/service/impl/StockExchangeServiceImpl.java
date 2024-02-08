package com.barbakini.inghubproject.service.impl;

import com.barbakini.inghubproject.dto.ExchangeDTO;
import com.barbakini.inghubproject.dto.StockDTO;
import com.barbakini.inghubproject.dto.StockExchangeCreateRequest;
import com.barbakini.inghubproject.jpa.model.ExchangeStocks;
import com.barbakini.inghubproject.jpa.model.Stock;
import com.barbakini.inghubproject.jpa.model.StockExchange;
import com.barbakini.inghubproject.jpa.repository.ExchangeStocksRepository;
import com.barbakini.inghubproject.jpa.repository.StockExchangeRepository;
import com.barbakini.inghubproject.jpa.repository.StockRepository;
import com.barbakini.inghubproject.service.StockExchangeService;
import com.barbakini.inghubproject.util.EntityTypeEnum;
import com.barbakini.inghubproject.util.exceptions.EntityAlreadyExistException;
import com.barbakini.inghubproject.util.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StockExchangeServiceImpl implements StockExchangeService {

    private final StockExchangeRepository stockExchangeRepository;
    private final ExchangeStocksRepository exchangeStocksRepository;
    private final StockRepository stockRepository;

    @Override
    public Mono<StockExchange> createStockExchange(StockExchangeCreateRequest request) {
        StockExchange newStockExchange = StockExchange.builder()
                .name(request.getName())
                .description(request.getDescription())
                .liveInMarket(false).build();
        return stockExchangeRepository.save(newStockExchange)
                .onErrorResume(DuplicateKeyException.class, (e) ->
                        Mono.error(new EntityAlreadyExistException(request.getName(), EntityTypeEnum.EXCHANGE)));
    }

    @Override
    public Mono<ExchangeDTO> getStockExchange(String name) {
        Mono<StockExchange> exchangeMono = stockExchangeRepository.findByName(name);
        Mono<List<Stock>> stockListMono = exchangeStocksRepository.getExchangeStocks(name).collectList();
        return Mono.zip(exchangeMono, stockListMono)
                .flatMap(tuple -> {
                    ExchangeDTO dto = ExchangeDTO.builder()
                            .exchange(tuple.getT1())
                            .stocks(StockDTO.builder().count(tuple.getT2().size()).stocks(tuple.getT2()).build()).build();

                    return Mono.just(dto);
                })
                .switchIfEmpty(Mono.error(new EntityNotFoundException(name, EntityTypeEnum.EXCHANGE)));
    }

    @Override
    public Mono<Void> addStockToStockExchange(String name, String stockName) {
        Mono<StockExchange> exchangeMono = stockExchangeRepository.findByName(name)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(name, EntityTypeEnum.EXCHANGE)));
        Mono<Stock> stockMono = stockRepository.findByName(stockName)
                .switchIfEmpty(Mono.error(new EntityNotFoundException(stockName, EntityTypeEnum.STOCK)));
        return Mono.zip(exchangeMono, stockMono).flatMap(tuple -> {
            ExchangeStocks exchangeStocks = ExchangeStocks.builder()
                    .exchangeId(tuple.getT1().getId())
                    .stockId(tuple.getT2().getId()).build();
            return exchangeStocksRepository.save(exchangeStocks)
                    .then(stockExchangeRepository.updateLiveInStatus(name));
        }).onErrorResume(DuplicateKeyException.class, (e) -> {
            log.info("error: ", e);
            return Mono.empty();
        }).onErrorResume(DataIntegrityViolationException.class, (e) -> {
            log.info("error: ", e);
            return Mono.error(new EntityNotFoundException(stockName, EntityTypeEnum.STOCK));
        });
    }

    @Override
    public Mono<Void> deleteStockToStockExchange(String name, String stockName) {
        Mono<StockExchange> exchangeMono = stockExchangeRepository.findByName(name);
        Mono<Stock> stockMono = stockRepository.findByName(stockName);
        return Mono.zip(exchangeMono, stockMono).flatMap(tuple ->
                        exchangeStocksRepository.deleteByExchangeIdAndStockId(tuple.getT1().getId(), tuple.getT2().getId())
                                .then(stockExchangeRepository.updateLiveInStatus(name)))
                .onErrorResume(e -> {
                    log.error("error: ", e);
                    return Mono.error(e);
                });
    }


}
