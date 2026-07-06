package bj.mef.sigfl_backend.invoice.application.mapper;

import bj.mef.sigfl_backend.invoice.application.dto.InvoiceRequest;
import bj.mef.sigfl_backend.invoice.application.dto.InvoiceResponse;
import bj.mef.sigfl_backend.invoice.domain.model.entities.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {

    Invoice toEntity(InvoiceRequest request);

    InvoiceResponse toResponse(Invoice invoice);

    List<InvoiceResponse> toList(List<Invoice> invoices);

    void updateEntity(InvoiceRequest request,
                      @MappingTarget Invoice invoice);
}