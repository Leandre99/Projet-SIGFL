-- Invoice module: reference table for the CRUD slice.
-- Column names follow Hibernate's default snake_case mapping of the entity.

CREATE TABLE invoice (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    version       BIGINT        NOT NULL DEFAULT 0,
    number        VARCHAR(64)   NOT NULL UNIQUE,
    customer_name VARCHAR(255)  NOT NULL,
    amount        NUMERIC(19, 2) NOT NULL,
    currency      VARCHAR(3)    NOT NULL,
    status        VARCHAR(32)   NOT NULL,
    due_date      DATE,
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ
);

CREATE INDEX idx_invoice_status ON invoice (status);
