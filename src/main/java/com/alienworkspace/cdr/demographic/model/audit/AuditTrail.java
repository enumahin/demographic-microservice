package com.alienworkspace.cdr.demographic.model.audit;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * Abstract base class for any entity that requires audit trail information.
 * Contains the following information:
 * <ul>
 *     <li>Created by</li>
 *     <li>Created at</li>
 *     <li>Last modified by</li>
 *     <li>Last modified at</li>
 *     <li>Voided</li>
 *     <li>Voided by</li>
 *     <li>Voided at</li>
 *     <li>Void reason</li>
 * </ul>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class AuditTrail {

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private long createdBy;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @Column(name = "last_modified_by", insertable = false)
    private Long lastModifiedBy;

    @LastModifiedDate
    @Column(name = "last_modified_at", insertable = false)
    private LocalDateTime lastModifiedAt;

    private boolean voided = false;

    @Column(name = "voided_by")
    private Long voidedBy;

    @Column(name = "voided_at")
    private LocalDateTime voidedAt;

    @Column(name = "void_reason")
    private String voidReason;

    private String uuid = UUID.randomUUID().toString();
}
