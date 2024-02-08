package com.barbakini.inghubproject.service.impl;


import com.barbakini.inghubproject.dto.StockExchangeCreateRequest;
import com.barbakini.inghubproject.jpa.model.ExchangeStocks;
import com.barbakini.inghubproject.jpa.model.Stock;
import com.barbakini.inghubproject.jpa.model.StockExchange;
import com.barbakini.inghubproject.jpa.repository.ExchangeStocksRepository;
import com.barbakini.inghubproject.jpa.repository.StockExchangeRepository;
import com.barbakini.inghubproject.jpa.repository.StockRepository;
import com.barbakini.inghubproject.service.StockExchangeService;
import com.barbakini.inghubproject.util.exceptions.EntityAlreadyExistException;
import com.barbakini.inghubproject.util.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockExchangeServiceTest {

    @Mock
    private StockRepository stockRepository;
    @Mock
    private StockExchangeRepository stockExchangeRepository;
    @Mock
    private ExchangeStocksRepository exchangeStocksRepository;

    @InjectMocks
    private StockExchangeService stockExchangeService;


    @Test
    void createStockExchange_ShouldCreateStockExchange() {
        StockExchange stockExchange = StockExchange.builder().id(1l).name("testStock").description("test stock").build();
        when(stockExchangeRepository.save(any(StockExchange.class))).thenReturn(Mono.just(stockExchange));

        StockExchangeCreateRequest request = new StockExchangeCreateRequest();
        request.setName("name");
        request.setDescription("description");
        StepVerifier.create(stockExchangeService.createStockExchange(request))
                .assertNext(savedExchange -> assertEquals(savedExchange, stockExchange))
                .verifyComplete();

        verify(stockExchangeRepository, times(1)).save(any());
    }

    @Test
    void createStockExchange_ThrowError_WhenStockExchangeExists() {
        when(stockExchangeRepository.save(any(StockExchange.class)))
                .thenReturn(Mono.error(new DuplicateKeyException("duplicate key")));

        StockExchangeCreateRequest request = new StockExchangeCreateRequest();
        request.setName("name");
        request.setDescription("description");
        StepVerifier.create(stockExchangeService.createStockExchange(request))
                .expectErrorMatches(e -> e instanceof EntityAlreadyExistException).verify();

        verify(stockExchangeRepository, times(1)).save(any());
    }

    @Test
    void getStockExchange_ShouldReturnDTO() {
        StockExchange stockExchange = StockExchange.builder().id(1l).name("testStock").description("test stock").build();
        when(stockExchangeRepository.findByName(anyString())).thenReturn(Mono.just(stockExchange));
        when(exchangeStocksRepository.getExchangeStocks(anyString())).thenReturn(Flux.empty());

        StepVerifier.create(stockExchangeService.getStockExchange("exchange"))
                .assertNext(exchangeDTO -> assertEquals(exchangeDTO.getExchange(), stockExchange))
                .verifyComplete();

        verify(stockExchangeRepository, times(1)).findByName(anyString());
        verify(exchangeStocksRepository, times(1)).getExchangeStocks(anyString());
    }

    @Test
    void getStockExchange_ThrowError_WhenStockExchangeNotExists() {
        when(stockExchangeRepository.findByName(anyString())).thenReturn(Mono.empty());
        when(exchangeStocksRepository.getExchangeStocks(anyString())).thenReturn(Flux.empty());

        StepVerifier.create(stockExchangeService.getStockExchange("exchange"))
                .expectErrorMatches(e -> e instanceof EntityNotFoundException).verify();

        verify(stockExchangeRepository, times(1)).findByName(anyString());
        verify(exchangeStocksRepository, times(1)).getExchangeStocks(anyString());
    }

    @Test
    void addStockToStockExchange_ShouldAddStock() {
        StockExchange stockExchange = StockExchange.builder().id(1l).name("testStock").description("test stock").build();
        Stock stock = Stock.builder().id(1l).name("testStock").description("test stock")
                .currentPrice(BigDecimal.TEN).lastUpdate(LocalDateTime.now()).build();
        when(stockExchangeRepository.findByName(anyString())).thenReturn(Mono.just(stockExchange));
        when(stockExchangeRepository.updateLiveInStatus(anyString())).thenReturn(Mono.empty());
        when(stockRepository.findByName(anyString())).thenReturn(Mono.just(stock));
        when(exchangeStocksRepository.save(any(ExchangeStocks.class))).thenReturn(Mono.empty());

        StepVerifier.create(stockExchangeService.addStockToStockExchange("exchange", "stock"))
                .verifyComplete();

        verify(stockExchangeRepository, times(1)).findByName(anyString());
        verify(stockExchangeRepository, times(1)).updateLiveInStatus(anyString());
        verify(stockRepository, times(1)).findByName(anyString());
        verify(exchangeStocksRepository, times(1)).save(any(ExchangeStocks.class));
    }

    @Test
    void deleteStockToStockExchange_ShouldDeleteStock() {
        StockExchange stockExchange = StockExchange.builder().id(1l).name("testStock").description("test stock").build();
        Stock stock = Stock.builder().id(1l).name("testStock").description("test stock")
                .currentPrice(BigDecimal.TEN).lastUpdate(LocalDateTime.now()).build();
        when(stockExchangeRepository.findByName(anyString())).thenReturn(Mono.just(stockExchange));
        when(stockRepository.findByName(anyString())).thenReturn(Mono.just(stock));
        when(stockExchangeRepository.updateLiveInStatus(anyString())).thenReturn(Mono.empty());
        when(exchangeStocksRepository.deleteByExchangeIdAndStockId(anyLong(), anyLong())).thenReturn(Mono.empty());

        StepVerifier.create(stockExchangeService.deleteStockToStockExchange("exchange", "stock")).verifyComplete();

        verify(stockRepository, times(1)).findByName(anyString());
        verify(stockExchangeRepository, times(1)).updateLiveInStatus(anyString());
    }
}
