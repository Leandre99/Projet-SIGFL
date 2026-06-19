package bj.mef.sigfl_backend.invoice.application;

import java.util.List;

import bj.mef.sigfl_backend.invoice.application.dto.InvoiceRequest;
import bj.mef.sigfl_backend.invoice.application.dto.InvoiceResponse;
import bj.mef.sigfl_backend.invoice.application.mapper.InvoiceMapper;
import bj.mef.sigfl_backend.invoice.domain.InvoiceRepository;
import bj.mef.sigfl_backend.invoice.domain.model.Invoice;
import bj.mef.sigfl_backend.invoice.domain.model.InvoiceStatus;
import bj.mef.sigfl_backend.shared.domain.exception.DuplicateResourceException;
import bj.mef.sigfl_backend.shared.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service holding the invoice use cases. Orchestrates the repository
 * port and the mapper; callers (controllers) work only with DTOs.
 */
@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository repository;
    private final InvoiceMapper mapper;

    public InvoiceService(InvoiceRepository repository, InvoiceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> findAll() {
        return mapper.toList(repository.findAll());
    }

    @Transactional(readOnly = true)
    public InvoiceResponse findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

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

    public InvoiceResponse update(Long id, InvoiceRequest request) {
        Invoice invoice = getOrThrow(id);
        mapper.updateEntity(request, invoice);
        return mapper.toResponse(repository.save(invoice));
    }

    public void delete(Long id) {
        repository.delete(getOrThrow(id));
    }

    private Invoice getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Invoice", id));
    }
}
