create table credential
(
    id         int auto_increment
        primary key,
    imp_code   varchar(20)          null,
    api_key    varchar(64)          null,
    api_secret varchar(128)         null,
    created    datetime             not null,
    modified   datetime             not null,
    active     tinyint(1) default 1 not null
)
    collate = utf8mb3_unicode_ci;

create table orders
(
    id          int auto_increment
        primary key,
    user_id     int default 0 not null,
    product_id  int           not null,
    status      varchar(16)   null,
    fail_reason varchar(256)  null,
    created     datetime      null,
    modified    datetime      null,
    amount      decimal       null
)
    collate = utf8mb3_unicode_ci;

create table payments
(
    id             int auto_increment
        primary key,
    user_id        int                                           not null,
    order_id       int                                           not null,
    currency       varchar(8)                                    null,
    amount         decimal(13, 2)                                null,
    vat            decimal(13, 2)                                null,
    cancel_amount  decimal(13, 2)                                null,
    imp_uid        varchar(32)                                   null,
    merchant_uid   varchar(40)                                   null,
    pg_code        varchar(16)                                   not null,
    pay_method     varchar(20)                                   null,
    apply_num      varchar(20)                                   null,
    card_code      varchar(4)                                    null,
    card_quota     int                                           not null,
    vbank_num      varchar(32)                                   null,
    vbank_date     datetime                                      null,
    vbank_holder   varchar(16)                                   null,
    vbank_code     varchar(4)                                    null,
    is_escrow      tinyint(1) default 0                          not null,
    buyer_name     varchar(16)                                   null,
    buyer_email    varchar(64)                                   null,
    buyer_tel      varchar(16)                                   null,
    buyer_addr     varchar(128)                                  null,
    buyer_postcode varchar(8)                                    null,
    custom_data    text                                          null,
    status         enum ('ready', 'paid', 'cancelled', 'failed') null,
    paid_at        datetime                                      null,
    failed_at      datetime                                      null,
    cancelled_at   datetime                                      null,
    fail_reason    varchar(256)                                  null,
    cancel_reason  varchar(256)                                  null,
    created        datetime                                      not null,
    modified       datetime                                      not null,
    pg_tid         varchar(50)                                   null,
    receipt_url    varchar(100)                                  null
)
    collate = utf8mb3_unicode_ci;

create table products
(
    id     int auto_increment
        primary key,
    stock  int default 0 not null,
    color  varchar(10)   null,
    size   varchar(5)    null,
    amount decimal(13)   null,
    name   varchar(50)   null
)
    collate = utf8mb3_unicode_ci;

create table users
(
    id       int auto_increment
        primary key,
    email    varchar(128)         null,
    phone    varchar(20)          null,
    address  varchar(128)         null,
    postcode varchar(8)           null,
    name     varchar(50)          null,
    created  datetime             not null,
    modified datetime             not null,
    active   tinyint(1) default 1 not null
)
    collate = utf8mb3_unicode_ci;

