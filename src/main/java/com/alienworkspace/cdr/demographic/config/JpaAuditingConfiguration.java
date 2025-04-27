package com.alienworkspace.cdr.demographic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Moved @EnableJpaAuditing to this configuration class.
 *
 * <p>Instead of putting @EnableJpaAuditing on the main application class or test config.
 * This ensures it's properly picked up only when JPA is ready to avoid jpaAuditingHandler error during test.
 * </p>
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class JpaAuditingConfiguration {
}

