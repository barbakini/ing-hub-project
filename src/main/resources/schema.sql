create table if not exists STOCK
(
    ID            BIGINT auto_increment,
    NAME          CHARACTER VARYING(256)           not null
        constraint STOCK_UK_NAME
            unique,
    DESCRIPTION   CHARACTER VARYING(256)           not null,
    CURRENT_PRICE NUMERIC(20, 10)                  not null,
    LAST_UPDATE   TIMESTAMP default LOCALTIMESTAMP() not null,
    constraint STOCK_PK
        primary key (ID)
);

create table if not exists STOCK_EXCHANGE
(
    ID            BIGINT auto_increment,
    NAME          CHARACTER VARYING(256)           not null
        constraint STOCK_EXCHANGE_UK_NAME
            unique,
    DESCRIPTION   CHARACTER VARYING(256)           not null,
    LIVE_IN_MARKET boolean default false,
    constraint STOCK_EXCHANGE_PK
        primary key (ID)
);

create table if not exists EXCHANGE_STOCKS
(
    STOCK_ID    BIGINT not null,
    EXCHANGE_ID BIGINT not null,
    constraint EXCHANGE_STOCKS_PK
        primary key (STOCK_ID, EXCHANGE_ID),
    constraint EXCHANGE_STOCKS_EXCHANGE_FK
        foreign key (EXCHANGE_ID) references STOCK_EXCHANGE
            on delete cascade,
    constraint EXCHANGE_STOCKS_STOCK_FK
        foreign key (STOCK_ID) references STOCK
            on delete cascade
);

