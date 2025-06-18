package com.alienworkspace.cdr.demographic.model.audit;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuditTrailTest {

    @Test
    void testAuditTrailFields() {
        // given
        LocalDateTime now = LocalDateTime.now();
        AuditTrail auditTrail = new AuditTrail();
        String uuid = UUID.randomUUID().toString();

        // when
        auditTrail.setCreatedBy(1L);
        auditTrail.setCreatedAt(now);
        auditTrail.setLastModifiedBy(2L);
        auditTrail.setLastModifiedAt(now.plusHours(1));
        auditTrail.setVoided(true);
        auditTrail.setVoidedBy(3L);
        auditTrail.setVoidedAt(now.plusHours(2));
        auditTrail.setVoidReason("Test void reason");
        auditTrail.setUuid(uuid);

        // then
        assertEquals(1L, auditTrail.getCreatedBy());
        assertEquals(now, auditTrail.getCreatedAt());
        assertEquals(2L, auditTrail.getLastModifiedBy());
        assertEquals(now.plusHours(1), auditTrail.getLastModifiedAt());
        assertTrue(auditTrail.isVoided());
        assertEquals(3L, auditTrail.getVoidedBy());
        assertEquals(now.plusHours(2), auditTrail.getVoidedAt());
        assertEquals("Test void reason", auditTrail.getVoidReason());
        assertEquals(uuid, auditTrail.getUuid());
    }

    @Test
    void testDefaultValues() {
        // when
        AuditTrail auditTrail = new AuditTrail();

        // then
        assertFalse(auditTrail.isVoided());
        assertNotNull(auditTrail.getUuid());
        assertTrue(UUID.fromString(auditTrail.getUuid()).toString().equals(auditTrail.getUuid()));
    }

    @Test
    void testAllArgsConstructor() {
        // given
        LocalDateTime now = LocalDateTime.now();
        String uuid = UUID.randomUUID().toString();

        // when
        AuditTrail auditTrail = new AuditTrail(
                1L,
                now,
                2L,
                now.plusHours(1),
                true,
                3L,
                now.plusHours(2),
                "Test void reason",
                uuid
        );

        // then
        assertEquals(1L, auditTrail.getCreatedBy());
        assertEquals(now, auditTrail.getCreatedAt());
        assertEquals(2L, auditTrail.getLastModifiedBy());
        assertEquals(now.plusHours(1), auditTrail.getLastModifiedAt());
        assertTrue(auditTrail.isVoided());
        assertEquals(3L, auditTrail.getVoidedBy());
        assertEquals(now.plusHours(2), auditTrail.getVoidedAt());
        assertEquals("Test void reason", auditTrail.getVoidReason());
        assertEquals(uuid, auditTrail.getUuid());
    }

    @Test
    void testToString() {
        // given
        LocalDateTime now = LocalDateTime.now();
        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setCreatedBy(1L);
        auditTrail.setCreatedAt(now);
        auditTrail.setLastModifiedBy(2L);
        auditTrail.setLastModifiedAt(now.plusHours(1));
        auditTrail.setVoided(true);
        auditTrail.setVoidedBy(3L);
        auditTrail.setVoidedAt(now.plusHours(2));
        auditTrail.setVoidReason("Test void reason");

        // when
        String toString = auditTrail.toString();

        // then
        assertTrue(toString.contains("createdBy=1"));
        assertTrue(toString.contains("createdAt=" + now));
        assertTrue(toString.contains("lastModifiedBy=2"));
        assertTrue(toString.contains("lastModifiedAt=" + now.plusHours(1)));
        assertTrue(toString.contains("voided=true"));
        assertTrue(toString.contains("voidedBy=3"));
        assertTrue(toString.contains("voidedAt=" + now.plusHours(2)));
        assertTrue(toString.contains("voidReason=Test void reason"));
        assertTrue(toString.contains("uuid="));
    }
} 