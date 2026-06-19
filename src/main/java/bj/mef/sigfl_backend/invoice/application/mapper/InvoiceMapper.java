package bj.mef.sigfl_backend.invoice.application.mapper;

import java.util.List;

import bj.mef.sigfl_backend.invoice.application.dto.InvoiceRequest;
import bj.mef.sigfl_backend.invoice.application.dto.InvoiceResponse;
import bj.mef.sigfl_backend.invoice.domain.model.Invoice;
import org.springframework.stereotype.Component;

/**
 * Maps between the {@link Invoice} entity and its DTOs. Plain, explicit Java —
 * no code generation. Copy this class as the template for new modules' mappers.
 */
@Component
public class InvoiceMapper {

    /** Entity -> response DTO. */
    public InvoiceResponse toResponse(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        return new InvoiceResponse(
                invoice.getId(),
                invoice.getNumber(),
                invoice.getCustomerName(),
                invoice.getAmount(),
                invoice.getCurrency(),
                invoice.getStatus(),
                invoice.getDueDate(),
                invoice.getCreatedAt(),
                invoice.getUpdatedAt());
    }

    /** List of entities -> list of response DTOs. */
    public List<InvoiceResponse> toList(List<Invoice> invoices) {
        return invoices.stream().map(this::toResponse).toList();
    }

    /** Request DTO -> new entity (for creation). */
    public Invoice toEntity(InvoiceRequest request) {
        Invoice invoice = new Invoice();
        invoice.setNumber(request.number());
        invoice.setCustomerName(request.customerName());
        invoice.setAmount(request.amount());
        invoice.setCurrency(request.currency());
        invoice.setStatus(request.status());
        invoice.setDueDate(request.dueDate());
        return invoice;
    }

    /**
     * Copies the request onto an existing entity (for updates). Only non-null
     * fields are applied, so it also works for partial updates.
     */
    public void updateEntity(InvoiceRequest request, Invoice invoice) {
        if (request.number() != null) {
            invoice.setNumber(request.number());
        }
        if (request.customerName() != null) {
            invoice.setCustomerName(request.customerName());
        }
        if (request.amount() != null) {
            invoice.setAmount(request.amount());
        }
        if (request.currency() != null) {
            invoice.setCurrency(request.currency());
        }
        if (request.status() != null) {
            invoice.setStatus(request.status());
        }
        if (request.dueDate() != null) {
            invoice.setDueDate(request.dueDate());
        }
    }
}
