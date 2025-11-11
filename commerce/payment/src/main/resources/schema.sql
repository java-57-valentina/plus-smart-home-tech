DROP TABLE IF EXISTS payments;

CREATE TABLE IF NOT EXISTS payments (
    id              UUID            NOT NULL PRIMARY KEY,
    order_id        UUID            NOT NULL,

    total_price     DECIMAL(10, 2),
    delivery_price  DECIMAL(10, 2),
    product_price   DECIMAL(10, 2),

    state           varchar(7)      NOT NULL,

    CONSTRAINT chk_payment_state    CHECK (state  IN ('PENDING', 'FAILED', 'SUCCESS' ))
);