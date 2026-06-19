package org.example.sigfl_backend.shared.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Enables JPA auditing so {@code @CreatedDate}/{@code @LastModifiedDate} on
 * {@link org.example.sigfl_backend.shared.domain.BaseEntity} are populated.
 */
@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
