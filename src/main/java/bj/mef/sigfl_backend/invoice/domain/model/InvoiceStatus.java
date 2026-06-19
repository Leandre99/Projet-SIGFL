package bj.mef.sigfl_backend.invoice.domain.model;

/**
 * Lifecycle of an {@link Invoice}.
 */
public enum InvoiceStatus {
    DRAFT,
    ISSUED,
    PAID,
    CANCELLED
}
