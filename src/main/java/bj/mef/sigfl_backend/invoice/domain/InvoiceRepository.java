package bj.mef.sigfl_backend.invoice.domain;

import java.util.Optional;

import bj.mef.sigfl_backend.invoice.domain.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository port for the {@link Invoice} aggregate. Spring Data JPA provides
 * the implementation at runtime; the application layer depends only on this.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByNumber(String number);

    boolean existsByNumber(String number);
}
