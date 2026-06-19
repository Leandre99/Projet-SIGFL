package org.example.sigfl_backend.invoice.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.example.sigfl_backend.invoice.domain.model.InvoiceStatus;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Inbound payload to create or update an invoice. Validated with Jakarta Bean
 * Validation ({@code @Valid} in the controller). {@code status} is optional and
 * defaults to {@code DRAFT} on creation.
 */
public record InvoiceRequest(
        @NotBlank @Size(max = 64) String number,
        @NotBlank @Size(max = 255) String customerName,
        @NotNull @Positive BigDecimal amount,
        @NotBlank @Size(min = 3, max = 3) String currency,
        InvoiceStatus status,
        @FutureOrPresent LocalDate dueDate) {
}
