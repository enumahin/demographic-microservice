package com.alienworkspace.cdr.demographic.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PersonAddressTest {

    @Test
    void testPersonAddressBuilder() {
        // given
        Person person = Person.builder().personId(1L).build();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // when
        PersonAddress address = PersonAddress.builder()
                .personAddressId(1L)
                .person(person)
                .preferred(true)
                .country(1)
                .state(2)
                .county(3)
                .city(4)
                .community(5)
                .postalCode("12345")
                .addressLine1("123 Main St")
                .addressLine2("Apt 4B")
                .addressLine3("Building C")
                .landmark("Near Park")
                .longitude(123L)
                .latitude(456L)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        // then
        assertEquals(1L, address.getPersonAddressId());
        assertEquals(person, address.getPerson());
        assertTrue(address.isPreferred());
        assertEquals(1, address.getCountry());
        assertEquals(2, address.getState());
        assertEquals(3, address.getCounty());
        assertEquals(4, address.getCity());
        assertEquals(5, address.getCommunity());
        assertEquals("12345", address.getPostalCode());
        assertEquals("123 Main St", address.getAddressLine1());
        assertEquals("Apt 4B", address.getAddressLine2());
        assertEquals("Building C", address.getAddressLine3());
        assertEquals("Near Park", address.getLandmark());
        assertEquals(123L, address.getLongitude());
        assertEquals(456L, address.getLatitude());
        assertEquals(startDate, address.getStartDate());
        assertEquals(endDate, address.getEndDate());
    }

    @Test
    void testAuditTrailFields() {
        // given
        LocalDateTime now = LocalDateTime.now();
        PersonAddress address = PersonAddress.builder()
                .personAddressId(1L)
                .addressLine1("123 Main St")
                .build();

        // when
        address.setCreatedBy(1L);
        address.setCreatedAt(now);
        address.setLastModifiedBy(2L);
        address.setLastModifiedAt(now.plusHours(1));
        address.setVoided(true);
        address.setVoidedBy(3L);
        address.setVoidedAt(now.plusHours(2));
        address.setVoidReason("Test void reason");

        // then
        assertEquals(1L, address.getCreatedBy());
        assertEquals(now, address.getCreatedAt());
        assertEquals(2L, address.getLastModifiedBy());
        assertEquals(now.plusHours(1), address.getLastModifiedAt());
        assertTrue(address.isVoided());
        assertEquals(3L, address.getVoidedBy());
        assertEquals(now.plusHours(2), address.getVoidedAt());
        assertEquals("Test void reason", address.getVoidReason());
    }

    @Test
    void testSetPreferred() {
        // given
        PersonAddress address = PersonAddress.builder()
                .personAddressId(1L)
                .addressLine1("123 Main St")
                .preferred(false)
                .build();

        // when
        address.setPreferred(true);

        // then
        assertTrue(address.isPreferred());
    }

    @Test
    void testSetAddressLines() {
        // given
        PersonAddress address = PersonAddress.builder()
                .personAddressId(1L)
                .build();

        // when
        address.setAddressLine1("123 Main St");
        address.setAddressLine2("Apt 4B");
        address.setAddressLine3("Building C");

        // then
        assertEquals("123 Main St", address.getAddressLine1());
        assertEquals("Apt 4B", address.getAddressLine2());
        assertEquals("Building C", address.getAddressLine3());
    }

    @Test
    void testSetLocationFields() {
        // given
        PersonAddress address = PersonAddress.builder()
                .personAddressId(1L)
                .build();

        // when
        address.setCountry(1);
        address.setState(2);
        address.setCounty(3);
        address.setCity(4);
        address.setCommunity(5);
        address.setPostalCode("12345");
        address.setLongitude(123L);
        address.setLatitude(456L);

        // then
        assertEquals(1, address.getCountry());
        assertEquals(2, address.getState());
        assertEquals(3, address.getCounty());
        assertEquals(4, address.getCity());
        assertEquals(5, address.getCommunity());
        assertEquals("12345", address.getPostalCode());
        assertEquals(123L, address.getLongitude());
        assertEquals(456L, address.getLatitude());
    }

    @Test
    void testSetDates() {
        // given
        PersonAddress address = PersonAddress.builder()
                .personAddressId(1L)
                .build();
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        // when
        address.setStartDate(startDate);
        address.setEndDate(endDate);

        // then
        assertEquals(startDate, address.getStartDate());
        assertEquals(endDate, address.getEndDate());
    }
} 