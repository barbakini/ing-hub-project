package com.barbakini.inghubproject.jpa.repository;

import com.barbakini.inghubproject.jpa.model.Stock;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface StockRepository extends ReactiveCrudRepository<Stock, Long> {
    Mono<Void> deleteByName(String name);

    Mono<Stock> findByName(String name);

    @Modifying
    @Query("UPDATE stock SET current_price = :price, last_update = :updateTime  where name = :name")
    Mono<Void> updatePrice(BigDecimal price, LocalDateTime updateTime, String name);
}
