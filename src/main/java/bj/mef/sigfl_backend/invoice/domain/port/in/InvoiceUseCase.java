package bj.mef.sigfl_backend.invoice.domain.port.in;

import bj.mef.sigfl_backend.invoice.application.dto.InvoiceRequest;
import bj.mef.sigfl_backend.invoice.application.dto.InvoiceResponse;

import java.util.List;

public interface InvoiceUseCase {
    List<InvoiceResponse> findAll();

    InvoiceResponse findById(Long id);

    InvoiceResponse create(InvoiceRequest request);

    InvoiceResponse update(Long id, InvoiceRequest request);

    void delete(Long id);
}
