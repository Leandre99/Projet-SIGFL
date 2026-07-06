package bj.mef.sigfl_backend.invoice.application.service;

import bj.mef.sigfl_backend.invoice.application.dto.InvoiceRequest;
import bj.mef.sigfl_backend.invoice.application.dto.InvoiceResponse;
import bj.mef.sigfl_backend.invoice.application.mapper.InvoiceMapper;
import bj.mef.sigfl_backend.invoice.domain.InvoiceRepository;
import bj.mef.sigfl_backend.invoice.domain.port.in.InvoiceUseCase;

import java.util.List;

public class InvoiceUseCaseImpl implements InvoiceUseCase {

    private final InvoiceRepository repository;
    private final InvoiceMapper mapper;

    @Override
    public InvoiceResponse create(InvoiceRequest request) {
        if (repository.existsByNumber(request.number())) {
            throw new DuplicateResourceException("Invoice", "number", request.number());
        }
        Invoice invoice = mapper.toEntity(request);
        if (invoice.getStatus() == null) {
            invoice.setStatus(InvoiceStatus.DRAFT);
        }
        return mapper.toResponse(repository.save(invoice));
    }

    @Override
    public InvoiceResponse update(Long id, InvoiceRequest request) {
        return null;
    }

    @Override
    public InvoiceResponse findById(Long id) {
        return null;
    }

    @Override
    public List<InvoiceResponse> findAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
}
