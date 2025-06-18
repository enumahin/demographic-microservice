package com.alienworkspace.cdr.demographic.model.audit;

/**
 * Mapper for AuditTrail entity.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public abstract class AuditTrailMapper {

    /**
     * Maps a AuditTrail entity to a AuditTrailDto.
     *
     * @param from The AuditTrail entity to map.
     * @param to   The AuditTrailDto to map to.
     */
    public static <T extends com.alienworkspace.cdr.model.helper.AuditTrail> void mapFromDto(T from, AuditTrail to) {
        if (from == null || to == null) {
            return;
        }
        to.setUuid(from.getUuid());
        to.setCreatedAt(from.getCreatedAt());
        if (from.getCreatedBy() != null) {
            to.setCreatedBy(from.getCreatedBy());
        }
        to.setLastModifiedBy(from.getLastModifiedBy());
        to.setLastModifiedAt(from.getLastModifiedAt());
        if (from.getVoidedBy() != null) {
            to.setVoidedBy(from.getVoidedBy());
        }
        to.setVoidedBy(from.getVoidedBy());
        to.setVoidedAt(from.getVoidedAt());
        to.setVoidReason(from.getVoidReason());
    }

    /**
     * Maps a AuditTrail entity to a AuditTrailDto.
     *
     * @param from The AuditTrail entity to map.
     * @param to   The AuditTrailDto to map to.
     */
    public static <T extends AuditTrail> void mapToDto(T from, com.alienworkspace.cdr.model.helper.AuditTrail to) {
        if (from == null || to == null) {
            return;
        }
        to.setUuid(from.getUuid());
        to.setCreatedAt(from.getCreatedAt());
        to.setCreatedBy(from.getCreatedBy());
        to.setLastModifiedBy(from.getLastModifiedBy());
        to.setLastModifiedAt(from.getLastModifiedAt());
        to.setVoided(from.isVoided());
        to.setVoidedBy(from.getVoidedBy());
        to.setVoidedAt(from.getVoidedAt());
        to.setVoidReason(from.getVoidReason());
    }
}
