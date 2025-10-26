
CREATE TABLE IF NOT EXISTS carts
(
    id             UUID             PRIMARY KEY,
    username       varchar(64)      NOT NULL UNIQUE,
    state          varchar(11)      NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT chk_cart_state       CHECK (state  IN ('ACTIVE', 'DEACTIVATED'))
);

CREATE TABLE IF NOT EXISTS cart_products
(
    id             UUID     PRIMARY KEY,
    cart_id        UUID     NOT NULL REFERENCES carts(id),
    product_id     UUID     NOT NULL,
    quantity       INTEGER  NOT NULL CHECK(quantity > 0)
);

