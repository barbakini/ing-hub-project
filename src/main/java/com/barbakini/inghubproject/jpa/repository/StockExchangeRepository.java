package com.barbakini.inghubproject.jpa.repository;

import com.barbakini.inghubproject.jpa.model.StockExchange;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StockExchangeRepository extends ReactiveCrudRepository<StockExchange, Long> {

    Mono<StockExchange> findByName(String name);

    @Modifying
    @Query("""
                update STOCK_EXCHANGE set LIVE_IN_MARKET = (SELECT case when Count(STOCK_ID) > 4 then true                                         else false end
                FROM stock_exchange LEFT JOIN exchange_stocks ON stock_exchange.ID = exchange_stocks.EXCHANGE_ID
                where name = :name
                GROUP BY EXCHANGE_ID)
                where name = :name
            """)
    Mono<Void> updateLiveInStatus(@Param("name") String name);

    @Modifying
    @Query("""
                update STOCK_EXCHANGE s
                set LIVE_IN_MARKET = (SELECT case when Count(STOCK_ID) > 4 then true else false end
                                      FROM stock_exchange
                                               LEFT JOIN exchange_stocks ON stock_exchange.ID = exchange_stocks.EXCHANGE_ID
                                      where name = s.name
                                      GROUP BY EXCHANGE_ID);
            """)
    Mono<Void> updateLiveInStatus();

}
