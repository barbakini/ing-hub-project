package com.barbakini.inghubproject.controller;

import com.barbakini.inghubproject.dto.ExchangeDTO;
import com.barbakini.inghubproject.dto.Response;
import com.barbakini.inghubproject.dto.StockDTO;
import com.barbakini.inghubproject.dto.StockExchangeCreateRequest;
import com.barbakini.inghubproject.service.StockExchangeService;
import com.barbakini.inghubproject.util.Constants;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(Constants.STOCK_EXCHANGE_V1)
@Slf4j
public class StockExchangeController {

    private final StockExchangeService stockExchangeService;

    @PostMapping
    public Mono<Response<ExchangeDTO>> createStockExchange(@Valid @RequestBody StockExchangeCreateRequest request) {
        return stockExchangeService.createStockExchange(request)
                .flatMap(exchange -> Mono.just(Response.<ExchangeDTO>builder()
                        .success(true)
                        .data(ExchangeDTO.builder().exchange(exchange)
                                .stocks(StockDTO.builder().count(0).stocks(List.of()).build())
                                .build())
                        .build()));
    }

    @GetMapping("/{name}")
    public Mono<Response<ExchangeDTO>> getStockExchange(@PathVariable String name) {
        return stockExchangeService.getStockExchange(name)
                .flatMap(dto -> Mono.just(Response.<ExchangeDTO>builder()
                        .success(true)
                        .data(dto).build()));
    }

    @PatchMapping("/{name}")
    public Mono<Response<Object>> addStockToStockExchange(@PathVariable String name, @RequestParam String stockName) {
        return stockExchangeService.addStockToStockExchange(name, stockName)
                .thenReturn(Response.builder().success(true).build());
    }

    @DeleteMapping("/{name}")
    public Mono<Response<Object>> deleteStockToStockExchange(@PathVariable String name, @RequestParam String stockName) {
        return stockExchangeService.deleteStockToStockExchange(name, stockName)
                .thenReturn(Response.builder().success(true).build());
    }
}
