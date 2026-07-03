-- Hibernate Envers history tables, kept in their own "audit" schema (separate
-- from the application's public schema). Hibernate ddl-auto stays "validate",
-- so — same as every other table — these are hand-written here, not
-- auto-generated. Every entity extending BaseEntity (@Audited, see
-- shared/domain/BaseEntity) needs one matching "audit.<table>_aud" row here.

CREATE SCHEMA IF NOT EXISTS audit;

-- One row per committed transaction that touched an audited entity.
CREATE TABLE audit.revinfo (
    rev      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    revtstmp BIGINT
);

-- History of the invoice table: one row per revision, plus revtype
-- (0 = ADD, 1 = MOD, 2 = DEL). "version" is not audited: Envers' own
-- revision tracking already supersedes optimistic-locking history.
CREATE TABLE audit.invoice_aud (
    id            BIGINT        NOT NULL,
    rev           INTEGER       NOT NULL REFERENCES audit.revinfo (rev),
    revtype       SMALLINT,
    number        VARCHAR(64),
    customer_name VARCHAR(255),
    amount        NUMERIC(19, 2),
    currency      VARCHAR(3),
    status        VARCHAR(32),
    due_date      DATE,
    created_at    TIMESTAMPTZ,
    updated_at    TIMESTAMPTZ,
    PRIMARY KEY (id, rev)
);
