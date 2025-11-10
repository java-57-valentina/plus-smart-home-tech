CREATE TABLE IF NOT EXISTS payments (
    id          UUID        NOT NULL PRIMARY KEY,
    order_id    UUID        NOT NULL
);