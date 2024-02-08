package com.barbakini.inghubproject.controller;

import com.barbakini.inghubproject.dto.Response;
import com.barbakini.inghubproject.dto.StockCreateRequest;
import com.barbakini.inghubproject.dto.StockUpdateRequest;
import com.barbakini.inghubproject.jpa.model.Stock;
import com.barbakini.inghubproject.service.StockService;
import com.barbakini.inghubproject.util.Constants;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping(Constants.STOCK_V1)
@Slf4j
public class StockController {

    private final StockService stockService;

    @PostMapping()
    public Mono<Response<Stock>> createStock(@Valid @RequestBody StockCreateRequest request) {
        return stockService.createStock(request)
                .flatMap(stock -> Mono.just(Response.<Stock>builder()
                        .success(true)
                        .data(stock).build()));
    }

    @DeleteMapping()
    public Mono<Response<Object>> deleteStock(@RequestParam String name) {
        return stockService.deleteStock(name).thenReturn(Response.builder().success(true).build());
    }

    @PatchMapping()
    public Mono<Response<Object>> updateStock(@Valid @RequestBody StockUpdateRequest request) {
        return stockService.updateStock(request).thenReturn(Response.builder().success(true).build());
    }

}
