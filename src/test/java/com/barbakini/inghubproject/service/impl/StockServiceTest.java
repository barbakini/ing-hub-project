package com.barbakini.inghubproject.service.impl;


import com.barbakini.inghubproject.dto.StockCreateRequest;
import com.barbakini.inghubproject.dto.StockUpdateRequest;
import com.barbakini.inghubproject.jpa.model.Stock;
import com.barbakini.inghubproject.jpa.repository.StockExchangeRepository;
import com.barbakini.inghubproject.jpa.repository.StockRepository;
import com.barbakini.inghubproject.service.StockService;
import com.barbakini.inghubproject.util.exceptions.EntityAlreadyExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;
    @Mock
    private StockExchangeRepository stockExchangeRepository;

    @InjectMocks
    private StockService stockService;


    @Test
    void createStock_ShouldCreateStock() {
        Stock stock = Stock.builder().id(1l).name("testStock").description("test stock")
                .currentPrice(BigDecimal.TEN).lastUpdate(LocalDateTime.now()).build();
        when(stockRepository.save(any(Stock.class))).thenReturn(Mono.just(stock));

        StockCreateRequest request = new StockCreateRequest();
        request.setName("name");
        request.setDescription("description");
        request.setCurrentPrice(BigDecimal.TEN);
        StepVerifier.create(stockService.createStock(request)).assertNext(savedStock -> assertEquals(savedStock, stock)).verifyComplete();

        verify(stockRepository, times(1)).save(any());
    }

    @Test
    void createStock_ThrowError_WhenStockExists() {
        when(stockRepository.save(any(Stock.class))).thenReturn(Mono.error(new DuplicateKeyException("duplicate key")));

        StockCreateRequest request = new StockCreateRequest();
        request.setName("name");
        request.setDescription("description");
        request.setCurrentPrice(BigDecimal.TEN);
        StepVerifier.create(stockService.createStock(request)).expectErrorMatches(e -> e instanceof EntityAlreadyExistException).verify();

        verify(stockRepository, times(1)).save(any());
    }

    @Test
    void deleteStock_ShouldDeleteStock() {
        when(stockRepository.deleteByName(anyString())).thenReturn(Mono.empty());
        when(stockExchangeRepository.updateLiveInStatus()).thenReturn(Mono.empty());

        StepVerifier.create(stockService.deleteStock("delete")).verifyComplete();

        verify(stockRepository, times(1)).deleteByName(anyString());
        verify(stockExchangeRepository, times(1)).updateLiveInStatus();
    }

    @Test
    void updateStock_ShouldUpdateStock() {
        when(stockRepository.updatePrice(any(BigDecimal.class), any(LocalDateTime.class), anyString())).thenReturn(Mono.empty());

        StockUpdateRequest request = new StockUpdateRequest();
        request.setName("update");
        request.setPrice(BigDecimal.TEN);
        StepVerifier.create(stockService.updateStock(request)).verifyComplete();

        verify(stockRepository, times(1)).updatePrice(any(BigDecimal.class), any(LocalDateTime.class), anyString());
    }

    @Test
    void getStock_ShouldReturnStock() {
        Stock stock = Stock.builder().id(1l).name("testStock").description("test stock")
                .currentPrice(BigDecimal.TEN).lastUpdate(LocalDateTime.now()).build();
        when(stockRepository.findByName(anyString())).thenReturn(Mono.just(stock));

        StepVerifier.create(stockService.getStock("get")).assertNext(stock1 -> assertEquals(stock1, stock)).verifyComplete();

        verify(stockRepository, times(1)).findByName(anyString());
    }
}
