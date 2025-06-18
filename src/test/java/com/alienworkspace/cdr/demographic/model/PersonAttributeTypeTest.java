package com.alienworkspace.cdr.demographic.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PersonAttributeTypeTest {

    @Test
    void testPersonAttributeTypeBuilder() {
        // given & when
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .personAttributeTypeId(1)
                .name("Blood Type")
                .description("Person's blood type")
                .format("text")
                .build();

        // then
        assertEquals(1, attributeType.getPersonAttributeTypeId());
        assertEquals("Blood Type", attributeType.getName());
        assertEquals("Person's blood type", attributeType.getDescription());
        assertEquals("text", attributeType.getFormat());
    }

    @Test
    void testAuditTrailFields() {
        // given
        LocalDateTime now = LocalDateTime.now();
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .personAttributeTypeId(1)
                .name("Blood Type")
                .build();

        // when
        attributeType.setCreatedBy(1L);
        attributeType.setCreatedAt(now);
        attributeType.setLastModifiedBy(2L);
        attributeType.setLastModifiedAt(now.plusHours(1));
        attributeType.setVoided(true);
        attributeType.setVoidedBy(3L);
        attributeType.setVoidedAt(now.plusHours(2));
        attributeType.setVoidReason("Test void reason");

        // then
        assertEquals(1L, attributeType.getCreatedBy());
        assertEquals(now, attributeType.getCreatedAt());
        assertEquals(2L, attributeType.getLastModifiedBy());
        assertEquals(now.plusHours(1), attributeType.getLastModifiedAt());
        assertTrue(attributeType.isVoided());
        assertEquals(3L, attributeType.getVoidedBy());
        assertEquals(now.plusHours(2), attributeType.getVoidedAt());
        assertEquals("Test void reason", attributeType.getVoidReason());
    }

    @Test
    void testSetName() {
        // given
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .personAttributeTypeId(1)
                .build();

        // when
        attributeType.setName("Blood Type");

        // then
        assertEquals("Blood Type", attributeType.getName());
    }

    @Test
    void testSetDescription() {
        // given
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .personAttributeTypeId(1)
                .build();

        // when
        attributeType.setDescription("Person's blood type");

        // then
        assertEquals("Person's blood type", attributeType.getDescription());
    }

    @Test
    void testSetFormat() {
        // given
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .personAttributeTypeId(1)
                .build();

        // when
        attributeType.setFormat("text");

        // then
        assertEquals("text", attributeType.getFormat());
    }

    @Test
    void testSetPersonAttributeTypeId() {
        // given
        PersonAttributeType attributeType = PersonAttributeType.builder().build();

        // when
        attributeType.setPersonAttributeTypeId(1);

        // then
        assertEquals(1, attributeType.getPersonAttributeTypeId());
    }

    @Test
    void testNoArgsConstructor() {
        // when
        PersonAttributeType attributeType = new PersonAttributeType();

        // then
        assertNotNull(attributeType);
        assertEquals(0, attributeType.getPersonAttributeTypeId());
        assertNull(attributeType.getName());
        assertNull(attributeType.getDescription());
        assertNull(attributeType.getFormat());
    }
} 