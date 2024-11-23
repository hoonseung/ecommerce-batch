create table products
(
    product_id       varchar(255) not null,
    seller_id        bigint       not null,
    category         varchar(255) not null,
    product_name     varchar(255) not null,
    sales_start_date date,
    sales_end_date   date,
    product_status   varchar(50),
    brand            varchar(255),
    manufacturer     varchar(255),
    sales_price      int          not null,
    stock_quantity   int       default 0,
    created_at       timestamp default current_timestamp,
    updated_at       timestamp default current_timestamp,
    primary key (product_id)
);
create index idx_products_product_status on products (product_status);
create index idx_products_category on products (category);
create index idx_products_brand on products (brand);
create index idx_products_manufacturer on products (manufacturer);
create index idx_products_seller_id on products (seller_id);

create table payments
(
    payment_id     bigserial,
    payment_method varchar(50) not null,
    payment_status varchar(50) not null,
    payment_date   timestamp   not null,
    amount         int         not null,
    order_id       bigint      not null unique,
    primary key (payment_id)
);
create index idx_payment_order_id on payments (order_id);

create table orders
(
    order_id     bigserial,
    order_date   timestamp   not null,
    order_status varchar(50) not null,
    customer_id  bigint
);
create index idx_orders_customer_id on orders (customer_id);

create table order_products
(
    order_product_id bigserial,
    order_id         bigint       not null,
    product_id       varchar(255) not null,
    item_price       int          not null,
    quantity         int          not null,
    primary key (order_product_id)
);
create index idx_order_products_product_id on order_products (product_id);
create unique index idx_order_products_order_id_product_id on order_products (order_id, product_id);



