package com.barbakini.inghubproject.jpa.repository;

import com.barbakini.inghubproject.jpa.model.ExchangeStocks;
import com.barbakini.inghubproject.jpa.model.Stock;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ExchangeStocksRepository extends ReactiveCrudRepository<ExchangeStocks, Long> {


    @Query("""
            select s.* from stock s, stock_exchange e, exchange_stocks es where es.stock_id = s.id and es.exchange_id = e.id
            and e.name = :name
            """)
    Flux<Stock> getExchangeStocks(String name);

    Mono<Void> deleteByExchangeIdAndStockId(Long exchangeId, Long stockId);
}
