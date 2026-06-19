package org.example.sigfl_backend.invoice.application.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

import org.example.sigfl_backend.invoice.domain.model.InvoiceStatus;

/**
 * Outbound representation of an invoice. Never expose the entity directly.
 */
public record InvoiceResponse(
        Long id,
        String number,
        String customerName,
        BigDecimal amount,
        String currency,
        InvoiceStatus status,
        LocalDate dueDate,
        Instant createdAt,
        Instant updatedAt) {
}
