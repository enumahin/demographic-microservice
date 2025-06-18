package com.alienworkspace.cdr.demographic.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PersonNameTest {

    @Test
    void testPersonNameBuilder() {
        // given
        Person person = Person.builder().personId(1L).build();
        LocalDateTime now = LocalDateTime.now();

        // when
        PersonName personName = PersonName.builder()
                .personNameId(1L)
                .person(person)
                .firstName("John")
                .middleName("William")
                .lastName("Doe")
                .otherName("Johnny")
                .preferred(true)
                .build();

        // then
        assertEquals(1L, personName.getPersonNameId());
        assertEquals(person, personName.getPerson());
        assertEquals("John", personName.getFirstName());
        assertEquals("William", personName.getMiddleName());
        assertEquals("Doe", personName.getLastName());
        assertEquals("Johnny", personName.getOtherName());
        assertTrue(personName.isPreferred());
    }

    @Test
    void testEquals_SameObject() {
        // given
        PersonName personName = PersonName.builder()
                .personNameId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        // when & then
        assertTrue(personName.equals(personName));
    }

    @Test
    void testEquals_DifferentObject() {
        // given
        String uuid = "12345678-abcd-90ab-cdef-1234567890ab";
        PersonName personName1 = PersonName.builder()
                .personNameId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
        personName1.setUuid(uuid);

        PersonName personName2 = PersonName.builder()
                .personNameId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();
        personName2.setUuid(uuid);

        // when & then
        assertTrue(personName1.equals(personName2));
    }

    @Test
    void testEquals_DifferentValues() {
        // given
        PersonName personName1 = PersonName.builder()
                .personNameId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        PersonName personName2 = PersonName.builder()
                .personNameId(2L)
                .firstName("Jane")
                .lastName("Smith")
                .build();

        // when & then
        assertFalse(personName1.equals(personName2));
    }

    @Test
    void testEquals_NullObject() {
        // given
        PersonName personName = PersonName.builder()
                .personNameId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        // when & then
        assertFalse(personName.equals(null));
    }

    @Test
    void testEquals_DifferentClass() {
        // given
        PersonName personName = PersonName.builder()
                .personNameId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        // when & then
        assertFalse(personName.equals("Not a PersonName object"));
    }

    @Test
    void testAuditTrailFields() {
        // given
        LocalDateTime now = LocalDateTime.now();
        PersonName personName = PersonName.builder()
                .personNameId(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        // when
        personName.setCreatedBy(1L);
        personName.setCreatedAt(now);
        personName.setLastModifiedBy(2L);
        personName.setLastModifiedAt(now.plusHours(1));
        personName.setVoided(true);
        personName.setVoidedBy(3L);
        personName.setVoidedAt(now.plusHours(2));
        personName.setVoidReason("Test void reason");

        // then
        assertEquals(1L, personName.getCreatedBy());
        assertEquals(now, personName.getCreatedAt());
        assertEquals(2L, personName.getLastModifiedBy());
        assertEquals(now.plusHours(1), personName.getLastModifiedAt());
        assertTrue(personName.isVoided());
        assertEquals(3L, personName.getVoidedBy());
        assertEquals(now.plusHours(2), personName.getVoidedAt());
        assertEquals("Test void reason", personName.getVoidReason());
    }
} 