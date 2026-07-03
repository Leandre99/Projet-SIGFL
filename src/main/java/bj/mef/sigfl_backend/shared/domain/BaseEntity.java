package bj.mef.sigfl_backend.shared.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import lombok.Getter;

/**
 * Mapped superclass shared by every JPA entity in the application: a generated
 * identity, optimistic-locking version and audit timestamps (filled by
 * {@code @EnableJpaAuditing}). Module entities should extend this.
 *
 * <p>{@code @Audited} is declared here so every subclass is history-tracked by
 * Envers into the {@code audit} schema (table {@code <table>_aud}) with no
 * per-entity annotation needed. Each new entity needs a matching Flyway
 * migration creating its {@code audit.<table>_aud} table — see
 * {@code V3__audit_schema.sql} for the Invoice example.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Audited
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long version;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public boolean isNew() {
        return id == null;
    }
}
