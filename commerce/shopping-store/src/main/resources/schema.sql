CREATE TABLE IF NOT EXISTS products
(
    id             UUID           PRIMARY KEY,
    name           VARCHAR(100)   NOT NULL,
    description    VARCHAR(1000)  NOT NULL,
    price          DECIMAL(10, 2) NOT NULL,
    category       VARCHAR(8)     NOT NULL,
    image_src      VARCHAR(500),
    quantity_state VARCHAR(6)     NOT NULL,
    product_state  VARCHAR(10)    NOT NULL,

    CONSTRAINT chk_product_state    CHECK (product_state  IN ('ACTIVE', 'DEACTIVATE')),
    CONSTRAINT chk_category         CHECK (category       IN ('CONTROL', 'SENSORS', 'LIGHTING')),
    CONSTRAINT chk_quantity_state   CHECK (quantity_state IN ('ENDED', 'FEW', 'ENOUGH', 'MANY'))
);