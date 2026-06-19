package org.example.sigfl_backend.invoice.application.mapper;

import org.example.sigfl_backend.invoice.application.dto.InvoiceRequest;
import org.example.sigfl_backend.invoice.application.dto.InvoiceResponse;
import org.example.sigfl_backend.invoice.domain.model.Invoice;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * MapStruct mapper between the {@link Invoice} entity and its DTOs. The
 * implementation is generated at compile time and registered as a Spring bean
 * ({@code componentModel = "spring"}).
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper {

    InvoiceResponse toResponse(Invoice invoice);

    Invoice toEntity(InvoiceRequest request);

    /** Copies non-null request fields onto an existing entity (for updates). */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(InvoiceRequest request, @MappingTarget Invoice invoice);
}
