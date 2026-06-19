-- Flyway baseline migration.
-- Add new versioned scripts as V2__*.sql, V3__*.sql, ... — never edit an
-- already-applied migration; create a new one instead.

CREATE TABLE IF NOT EXISTS schema_smoke_test (
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);
