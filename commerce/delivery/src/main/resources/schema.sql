--DROP TABLE IF EXISTS delivery;
--DROP TABLE IF EXISTS address;

CREATE TABLE IF NOT EXISTS address (
    id              UUID             PRIMARY KEY,
    country         VARCHAR,
    city            VARCHAR,
    street          VARCHAR,
    house           VARCHAR,
    flat            VARCHAR
);

CREATE TABLE IF NOT EXISTS delivery (
    id              UUID            PRIMARY KEY,
    order_id        UUID            NOT NULL,
    state           VARCHAR(11)     NOT NULL,

    volume          DECIMAL(10, 2)  NOT NULL CHECK (volume > 0),
    weight          DECIMAL(10, 2)  NOT NULL CHECK (weight > 0),
    fragile         BOOLEAN         NOT NULL DEFAULT FALSE,

    address_from    UUID            NOT NULL REFERENCES address(id),
    address_to      UUID            NOT NULL REFERENCES address(id),

    CONSTRAINT chk_delivery_state    CHECK (state  IN (
            'CREATED', 'IN_PROGRESS', 'DELIVERED', 'FAILED', 'CANCELLED'))
);