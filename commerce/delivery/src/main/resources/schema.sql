CREATE TABLE IF NOT EXISTS delivery (
    id              UUID            PRIMARY KEY,
    order_ID        UUID            NOT NULL,
    state           VARCHAR(11)     NOT NULL,

    CONSTRAINT chk_delivery_state    CHECK (state  IN (
            'CREATED', 'IN_PROGRESS', 'DELIVERED', 'FAILED', 'CANCELLED'))
);