-- DROP TABLE IF EXISTS order_products;
-- DROP TABLE IF EXISTS orders;

CREATE TABLE IF NOT EXISTS orders
(
    id              UUID            PRIMARY KEY,
    username        VARCHAR(64)     NOT NULL,
    state           VARCHAR(16)     NOT NULL,
    cart_id         UUID            NOT NULL,

    delivery_id     UUID,
    payment_id      UUID,

    fragile         BOOLEAN         NOT NULL DEFAULT FALSE,
    weight          DECIMAL(10, 2)  NOT NULL CHECK (weight > 0),
    volume          DECIMAL(10, 2)  NOT NULL CHECK (volume > 0),

    products_price  DECIMAL(10, 2)  NULL,
    delivery_price  DECIMAL(10, 2)  NULL,
    total_price     DECIMAL(10, 2)  NULL,

    created_at      TIMESTAMP       NOT NULL,

    CONSTRAINT chk_order_state    CHECK (state  IN (
        'NEW',
        'ON_PAYMENT', 'PAID', 'PAYMENT_FAILED',
        'ASSEMBLED', 'ASSEMBLY_FAILED',
        'ON_DELIVERY', 'DELIVERED', 'DELIVERY_FAILED',
        'COMPLETED', 'DONE', 'CANCELED' ))
);

CREATE TABLE IF NOT EXISTS order_products (
    order_id        UUID            REFERENCES orders(id) ON DELETE CASCADE,
    product_id      UUID            NOT NULL,
    quantity        INTEGER         NOT NULL CHECK (quantity > 0),
    PRIMARY KEY (order_id, product_id)
);