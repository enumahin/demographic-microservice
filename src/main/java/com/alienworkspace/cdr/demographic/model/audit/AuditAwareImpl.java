package com.alienworkspace.cdr.demographic.model.audit;

import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;


/**
 * This class is an implementation of Spring Data's {@link AuditorAware} which
 * provides the current auditor to the JPA Auditing framework.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<Long> {


    /**
     * Returns the current auditor. This is the value of the {@code createdBy},
     * {@code lastModifiedBy} fields in the {@link AuditTrail} class.
     *
     * @return the current auditor
     */
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(1L);
    }
}
