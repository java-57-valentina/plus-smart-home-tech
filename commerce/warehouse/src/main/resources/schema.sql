DROP TABLE IF EXISTS goods;

CREATE TABLE IF NOT EXISTS goods (
    product_id  UUID            PRIMARY KEY,

    weight      DECIMAL(10, 2)  NOT NULL CHECK (weight > 0),
    width       DECIMAL(10, 2)  NOT NULL CHECK (width > 0),
    height      DECIMAL(10, 2)  NOT NULL CHECK (height > 0),
    depth       DECIMAL(10, 2)  NOT NULL CHECK (depth > 0),
    fragile     BOOLEAN         NOT NULL DEFAULT FALSE,

    quantity    INT             NOT NULL DEFAULT 0 CHECK (quantity >= 0),
    reserved    INT             NOT NULL DEFAULT 0 CHECK (reserved >= 0 AND reserved <= quantity)
);