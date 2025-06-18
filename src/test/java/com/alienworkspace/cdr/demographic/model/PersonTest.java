package com.alienworkspace.cdr.demographic.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void testPersonBuilder() {
        // given
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        LocalDate deathDate = LocalDate.of(2023, 1, 1);

        // when
        Person person = Person.builder()
                .personId(1L)
                .gender('M')
                .birthDate(birthDate)
                .dead(true)
                .deathDate(deathDate)
                .causeOfDeath("Natural causes")
                .names(new HashSet<>())
                .addresses(new HashSet<>())
                .attributes(new HashSet<>())
                .build();

        // then
        assertEquals(1L, person.getPersonId());
        assertEquals('M', person.getGender());
        assertEquals(birthDate, person.getBirthDate());
        assertTrue(person.isDead());
        assertEquals(deathDate, person.getDeathDate());
        assertEquals("Natural causes", person.getCauseOfDeath());
        assertNotNull(person.getNames());
        assertNotNull(person.getAddresses());
        assertNotNull(person.getAttributes());
        assertTrue(person.getNames().isEmpty());
        assertTrue(person.getAddresses().isEmpty());
        assertTrue(person.getAttributes().isEmpty());
    }

    @Test
    void testGetPreferredName() {
        // given
        Person person = Person.builder().build();
        PersonName preferredName = PersonName.builder()
                .personNameId(1L)
                .firstName("John")
                .lastName("Doe")
                .preferred(true)
                .build();
        PersonName nonPreferredName = PersonName.builder()
                .personNameId(2L)
                .firstName("Jane")
                .lastName("Doe")
                .preferred(false)
                .build();

        // when
        person.addName(preferredName);
        person.addName(nonPreferredName);

        // then
        assertEquals(preferredName, person.getPreferredName());
        assertEquals(2, person.getNames().size());
    }

    @Test
    void testGetPreferredAddress() {
        // given
        Person person = Person.builder().build();
        PersonAddress preferredAddress = PersonAddress.builder()
                .personAddressId(1L)
                .addressLine1("123 Main St")
                .preferred(true)
                .build();
        PersonAddress nonPreferredAddress = PersonAddress.builder()
                .personAddressId(2L)
                .addressLine1("456 Oak Ave")
                .preferred(false)
                .build();

        // when
        person.addAddress(preferredAddress);
        person.addAddress(nonPreferredAddress);

        // then
        assertEquals(preferredAddress, person.getPreferredAddress());
        assertEquals(2, person.getAddresses().size());
    }

    @Test
    void testGetPreferredAttribute() {
        // given
        Person person = Person.builder().build();
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .personAttributeTypeId(1)
                .name("Blood Type")
                .build();

        PersonAttribute preferredAttribute = PersonAttribute.builder()
                .personAttributeId(1L)
                .personAttributeType(attributeType)
                .attributeValue("A+")
                .preferred(true)
                .build();
        PersonAttribute nonPreferredAttribute = PersonAttribute.builder()
                .personAttributeId(2L)
                .personAttributeType(attributeType)
                .attributeValue("B+")
                .preferred(false)
                .build();

        // when
        person.addAttribute(preferredAttribute);
        person.addAttribute(nonPreferredAttribute);

        // then
        assertEquals(preferredAttribute, person.getPreferredAttribute(attributeType.getPersonAttributeTypeId()));
        assertEquals(2, person.getAttributes().size());
    }

    @Test
    void testAddName_UpdatesPreferredFlag() {
        // given
        Person person = Person.builder().build();
        PersonName firstPreferredName = PersonName.builder()
                .personNameId(1L)
                .firstName("John")
                .lastName("Doe")
                .preferred(true)
                .build();
        PersonName secondPreferredName = PersonName.builder()
                .personNameId(2L)
                .firstName("Jane")
                .lastName("Doe")
                .preferred(true)
                .build();

        // when
        person.addName(firstPreferredName);
        person.addName(secondPreferredName);

        // then
        assertFalse(firstPreferredName.isPreferred());
        assertTrue(secondPreferredName.isPreferred());
        assertEquals(secondPreferredName, person.getPreferredName());
    }

    @Test
    void testAddAddress_UpdatesPreferredFlag() {
        // given
        Person person = Person.builder().build();
        PersonAddress firstPreferredAddress = PersonAddress.builder()
                .personAddressId(1L)
                .addressLine1("123 Main St")
                .preferred(true)
                .build();
        PersonAddress secondPreferredAddress = PersonAddress.builder()
                .personAddressId(2L)
                .addressLine1("456 Oak Ave")
                .preferred(true)
                .build();

        // when
        person.addAddress(firstPreferredAddress);
        person.addAddress(secondPreferredAddress);

        // then
        assertFalse(firstPreferredAddress.isPreferred());
        assertTrue(secondPreferredAddress.isPreferred());
        assertEquals(secondPreferredAddress, person.getPreferredAddress());
    }

    @Test
    void testAddAttribute_UpdatesPreferredFlag() {
        // given
        Person person = Person.builder().build();
        PersonAttributeType attributeType = PersonAttributeType.builder()
                .personAttributeTypeId(1)
                .name("Blood Type")
                .build();

        PersonAttribute firstPreferredAttribute = PersonAttribute.builder()
                .personAttributeId(1L)
                .personAttributeType(attributeType)
                .attributeValue("A+")
                .preferred(true)
                .build();
        PersonAttribute secondPreferredAttribute = PersonAttribute.builder()
                .personAttributeId(2L)
                .personAttributeType(attributeType)
                .attributeValue("B+")
                .preferred(true)
                .build();

        // when
        person.addAttribute(firstPreferredAttribute);
        person.addAttribute(secondPreferredAttribute);

        // then
        assertFalse(firstPreferredAttribute.isPreferred());
        assertTrue(secondPreferredAttribute.isPreferred());
        assertEquals(secondPreferredAttribute, person.getPreferredAttribute(attributeType.getPersonAttributeTypeId()));
    }
} 