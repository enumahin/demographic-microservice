package com.alienworkspace.cdr.demographic.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PersonAttributeTest {

    @Test
    void testPersonAttributeBuilder() {
        // given
        Person person = Person.builder().personId(1L).build();
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .personAttributeTypeId(1)
                .name("Blood Type")
                .description("Person's blood type")
                .format("text")
                .build();

        // when
        PersonAttribute attribute = PersonAttribute.builder()
                .personAttributeId(1L)
                .person(person)
                .personAttributeType(attributeType)
                .attributeValue("A+")
                .preferred(true)
                .build();

        // then
        assertEquals(1L, attribute.getPersonAttributeId());
        assertEquals(person, attribute.getPerson());
        assertEquals(attributeType, attribute.getPersonAttributeType());
        assertEquals("A+", attribute.getAttributeValue());
        assertTrue(attribute.isPreferred());
    }

    @Test
    void testAuditTrailFields() {
        // given
        LocalDateTime now = LocalDateTime.now();
        PersonAttribute attribute = PersonAttribute.builder()
                .personAttributeId(1L)
                .attributeValue("Test Value")
                .build();

        // when
        attribute.setCreatedBy(1L);
        attribute.setCreatedAt(now);
        attribute.setLastModifiedBy(2L);
        attribute.setLastModifiedAt(now.plusHours(1));
        attribute.setVoided(true);
        attribute.setVoidedBy(3L);
        attribute.setVoidedAt(now.plusHours(2));
        attribute.setVoidReason("Test void reason");

        // then
        assertEquals(1L, attribute.getCreatedBy());
        assertEquals(now, attribute.getCreatedAt());
        assertEquals(2L, attribute.getLastModifiedBy());
        assertEquals(now.plusHours(1), attribute.getLastModifiedAt());
        assertTrue(attribute.isVoided());
        assertEquals(3L, attribute.getVoidedBy());
        assertEquals(now.plusHours(2), attribute.getVoidedAt());
        assertEquals("Test void reason", attribute.getVoidReason());
    }

    @Test
    void testSetPreferred() {
        // given
        PersonAttribute attribute = PersonAttribute.builder()
                .personAttributeId(1L)
                .attributeValue("Test Value")
                .preferred(false)
                .build();

        // when
        attribute.setPreferred(true);

        // then
        assertTrue(attribute.isPreferred());
    }

    @Test
    void testSetAttributeValue() {
        // given
        PersonAttribute attribute = PersonAttribute.builder()
                .personAttributeId(1L)
                .build();

        // when
        attribute.setAttributeValue("New Value");

        // then
        assertEquals("New Value", attribute.getAttributeValue());
    }

    @Test
    void testSetPersonAttributeType() {
        // given
        PersonAttribute attribute = PersonAttribute.builder()
                .personAttributeId(1L)
                .build();

        PersonAttributeType attributeType = PersonAttributeType.builder()
                .personAttributeTypeId(1)
                .name("Blood Type")
                .description("Person's blood type")
                .format("text")
                .build();

        // when
        attribute.setPersonAttributeType(attributeType);

        // then
        assertEquals(attributeType, attribute.getPersonAttributeType());
        assertEquals("Blood Type", attribute.getPersonAttributeType().getName());
        assertEquals("Person's blood type", attribute.getPersonAttributeType().getDescription());
        assertEquals("text", attribute.getPersonAttributeType().getFormat());
    }

    @Test
    void testSetPerson() {
        // given
        PersonAttribute attribute = PersonAttribute.builder()
                .personAttributeId(1L)
                .build();

        Person person = Person.builder()
                .personId(1L)
                .gender('M')
                .build();

        // when
        attribute.setPerson(person);

        // then
        assertEquals(person, attribute.getPerson());
        assertEquals(1L, attribute.getPerson().getPersonId());
        assertEquals('M', attribute.getPerson().getGender());
    }
} 