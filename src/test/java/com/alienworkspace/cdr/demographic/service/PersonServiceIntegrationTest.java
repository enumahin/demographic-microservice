package com.alienworkspace.cdr.demographic.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.demographic.service.client.MetadataFeignClient;
import com.alienworkspace.cdr.model.dto.metadata.*;
import com.alienworkspace.cdr.model.dto.person.*;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonServiceIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonAttributeTypeService personAttributeTypeService;

    @Autowired
    private PersonService personService;

    @MockitoBean
    private MetadataFeignClient metadataFeignClient;

    private final PersonNameDto.PersonNameDtoBuilder personNameDtoBuilder = PersonNameDto.builder();
    private final PersonAddressDto.PersonAddressDtoBuilder personAddressDtoBuilder = PersonAddressDto.builder();
    private final PersonAttributeTypeDto.PersonAttributeTypeDtoBuilder personAttributeTypeDtoBuilder = PersonAttributeTypeDto.builder();
    private final PersonAttributeDto.PersonAttributeDtoBuilder personAttributeDtoBuilder = PersonAttributeDto.builder();
    private final PersonDto.PersonDtoBuilder personDtoBuilder = PersonDto.builder();

    @BeforeEach
    public void setup() {
        personRepository.deleteAll();
        personNameDtoBuilder
                .firstName("John")
                .middleName("Umar");

        personDtoBuilder
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"));

        personAttributeTypeDtoBuilder
                .name("Phone Number")
                .description("Person Phone Number");

        PersonAttributeTypeDto personAttributeTypeDto = personAttributeTypeService.savePersonAttributeType(
                personAttributeTypeDtoBuilder.build());
        personAttributeDtoBuilder
                .personAttributeType(personAttributeTypeDto)
                .value("2345987667");

        personAddressDtoBuilder
                .country(CountryDto.builder().countryId(1).countryName("test country").build())
                .state(StateDto.builder().stateId(1).stateName("test state").build())
                .city(CityDto.builder().cityId(1).cityName("test city").build())
                .county(CountyDto.builder().countyId(1).countyName("test county").build())
                .community(CommunityDto.builder().communityId(1).communityName("test community").build())
                .addressLine1("test address line 1")
                .addressLine2("test address line 2")
                .addressLine3("test address line 3")
                .postalCode("test postal code");

        when(metadataFeignClient.getCountry(1)).thenReturn(
                        ResponseEntity.ok(CountryDto.builder().countryId(1).countryName("test country").build()));

        when(metadataFeignClient.getPersonLocation(1, 1, 1, 1, 1,
                null)).thenReturn(
                        ResponseEntity.ok(CountryDto.builder().countryId(1).countryName("test country").build()));
    }

    @DisplayName("Test add person")
    @Test
    public void testAddPerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();

        // when
        PersonDto response = personService.addPerson(personDto);

        // then
        assertEquals(personDto.getGender(), response.getGender());
        assertEquals(personDto.getBirthDate(), response.getBirthDate());
        assertTrue(response.getPersonId() > 0);
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getCreatedBy());
    }

    @DisplayName("Test update person")
    @Test
    public void testUpdatePerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();


        // when
        PersonDto savedPerson = personService.addPerson(personDto);
        PersonDto update = PersonDto.builder()
                .personId(savedPerson.getPersonId())
                .address(new HashSet<>())
                .gender('M')
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();
        PersonDto updatedPerson = personService.updatePerson(update.getPersonId(), update);

        // then
        assertNotEquals(savedPerson.getGender(), updatedPerson.getGender());
        assertTrue(savedPerson.getBirthDate().isBefore(updatedPerson.getBirthDate()));
        assertEquals(savedPerson.getPersonId(), updatedPerson.getPersonId());
        assertEquals(savedPerson.getCreatedAt(), updatedPerson.getCreatedAt());
        assertNotNull(updatedPerson.getLastModifiedAt());
        assertNotNull(updatedPerson.getLastModifiedBy());
    }

    @DisplayName("Test get person")
    @Test
    public void testGetPerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();

        // when
        PersonDto saved = personService.addPerson(personDto);
        PersonDto response = personService.getPerson(saved.getPersonId(), false);

        // then
        assertEquals(saved.getGender(), response.getGender());
        assertEquals(saved.getBirthDate(), response.getBirthDate());
        assertEquals(saved.getPersonId(), response.getPersonId());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getCreatedBy());
    }

    @DisplayName("Test get all persons")
    @Test
    public void testGetAllPerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
        PersonDto personDto2 = PersonDto.builder()
                .gender('M')
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();

        // when
        personService.addPerson(personDto);
        personService.addPerson(personDto2);
        List<PersonDto> response = personService.getPersons();

        // then
        assertTrue(response.size() == 2);
    }

    @DisplayName("Test delete person")
    @Test
    public void testDeletePerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();


        // when
        PersonDto savedPerson = personService.addPerson(personDto);
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();

        ResponseDto response = personService.deletePerson(savedPerson.getPersonId(), request);
        PersonDto deletedPerson = personService.getPerson(savedPerson.getPersonId(), true);

        // then
        assertEquals("Person deleted successfully", response.getStatusMessage());
        assertNotNull(deletedPerson);
        assertTrue(deletedPerson.getVoided());
        assertNotNull(deletedPerson.getVoidedAt());
        assertNotNull(deletedPerson.getVoidedBy());
    }

    @DisplayName("Test add a new person name")
    @Test
    public void testAddPersonName() {
        // given
        PersonDto personDto = personDtoBuilder.build();
        PersonNameDto personNameDto = personNameDtoBuilder.build();
        PersonDto savedPerson = personService.addPerson(personDto);

        // when
        PersonNameDto savedPersonName = personService.addPersonName(savedPerson.getPersonId(), personNameDto);

        // then
        assertNotNull(savedPersonName);
        assertEquals(personNameDto.getFirstName(), savedPersonName.getFirstName());
        assertEquals(personNameDto.getLastName(), savedPersonName.getLastName());
        assertTrue(savedPersonName.getPreferred());
        assertEquals(savedPerson.getPersonId(), savedPersonName.getPersonId());
    }

    @DisplayName("Test add a second not preferred person name")
    @Test
    void testAddSecondAndNotPreferredPersonName() {
        // given
        PersonDto personDto = personDtoBuilder.build();
        PersonNameDto personNameDto = personNameDtoBuilder.build();
        PersonDto savedPerson = personService.addPerson(personDto);

        // when
        PersonNameDto savedPersonName = personService.addPersonName(savedPerson.getPersonId(), personNameDto);
        PersonNameDto savedPersonName2 = personService.addPersonName(savedPerson.getPersonId(),
                personNameDtoBuilder.firstName("Queen").build());
        PersonDto updatedPerson = personService.getPerson(savedPerson.getPersonId(), false);

        // then
        assertNotNull(savedPersonName);
        assertNotNull(savedPersonName2);
        assertTrue(savedPersonName.getPreferred());
        assertFalse(savedPersonName2.getPreferred());
        assertEquals(2, updatedPerson.getName().size());
    }

    @DisplayName("Test add a new second preferred person name")
    @Test
    void testAddSecondPreferredPersonName() {
        // given
        PersonDto personDto = personDtoBuilder.build();
        PersonNameDto personNameDto = personNameDtoBuilder.build();
        PersonDto savedPerson = personService.addPerson(personDto);

        // when
        PersonNameDto savedPersonName = personService.addPersonName(savedPerson.getPersonId(), personNameDto);
        PersonNameDto savedPersonName2 = personService.addPersonName(savedPerson.getPersonId(),
                personNameDtoBuilder.firstName("Queen").preferred(true).build());
        PersonDto updatedPerson = personService.getPerson(savedPerson.getPersonId(), false);

        // then
        assertNotNull(savedPerson);
        assertFalse(updatedPerson.getName().stream().filter(name ->
                Objects.equals(name.getFirstName(), savedPersonName.getFirstName())
                && Objects.equals(name.getMiddleName(), savedPersonName.getMiddleName())
                        && Objects.equals(name.getLastName(), savedPersonName.getLastName())).findFirst()
                .map(PersonNameDto::getPreferred).orElse(true));
        assertTrue(updatedPerson.getName().stream().filter(name ->
                        Objects.equals(name.getFirstName(), savedPersonName2.getFirstName())
                                && Objects.equals(name.getMiddleName(), savedPersonName2.getMiddleName())
                                && Objects.equals(name.getLastName(), savedPersonName2.getLastName())).findFirst()
                .map(PersonNameDto::getPreferred).orElse(false));
        assertEquals(2, updatedPerson.getName().size());
    }

    @DisplayName("Test add a new person name")
    @Test
    void testAddPersonAddress() {
        // given
        PersonDto personDto = personDtoBuilder.build();
        PersonAddressDto personAddressDto = personAddressDtoBuilder.build();
        PersonDto savedPerson = personService.addPerson(personDto);

        // when
        PersonAddressDto savedPersonAddressDto = personService.addAddress(savedPerson.getPersonId(), personAddressDto);

        PersonDto updatedPerson = personService.getPerson(savedPerson.getPersonId(), false);
        // then
        assertNotNull(savedPerson);
        assertNotNull(savedPersonAddressDto);
        System.out.println(updatedPerson);
        assertEquals(1, updatedPerson.getAddress().size());
    }

    @DisplayName("Test add a second not preferred person address")
    @Test
    void testAddSecondAndNotPreferredPersonAddress() {
        // given
        PersonDto personDto = personDtoBuilder.build();
        PersonAddressDto personAddressDto = personAddressDtoBuilder.build();
        PersonDto savedPerson = personService.addPerson(personDto);

        // when
        PersonAddressDto savedPersonAddressDto = personService.addAddress(savedPerson.getPersonId(), personAddressDto);
        PersonAddressDto savedPersonAddressDto2 = personService.addAddress(savedPerson.getPersonId(), personAddressDtoBuilder
                .addressLine1("Another Address").preferred(false).build());
        PersonDto updatedPerson = personService.getPerson(savedPerson.getPersonId(), false);

        // then
        System.out.println(updatedPerson);
        assertNotNull(savedPersonAddressDto);
        assertNotNull(savedPersonAddressDto2);
        assertTrue(updatedPerson.getAddress().stream().filter(address ->
                address.getAddressLine1().equals(savedPersonAddressDto.getAddressLine1())
                && address.getAddressLine2().equals(savedPersonAddressDto.getAddressLine2())).findFirst()
                .map(PersonAddressDto::isPreferred).orElse(false));
        assertFalse(updatedPerson.getAddress().stream().filter(address ->
                        address.getAddressLine1().equals(savedPersonAddressDto2.getAddressLine1())
                                && address.getAddressLine2().equals(savedPersonAddressDto2.getAddressLine2())).findFirst()
                .map(PersonAddressDto::isPreferred).orElse(true));

        assertEquals(2, updatedPerson.getAddress().size());
    }

    @DisplayName("Test add a new second preferred person address")
    @Test
    void testAddSecondPreferredPersonAddress() {
        // given
        PersonDto personDto = personDtoBuilder.build();
        PersonAddressDto personAddressDto = personAddressDtoBuilder.build();
        PersonDto savedPerson = personService.addPerson(personDto);

        // when
        PersonAddressDto savedPersonAddressDto = personService.addAddress(savedPerson.getPersonId(), personAddressDto);
        PersonAddressDto savedPersonAddressDto2 = personService.addAddress(savedPerson.getPersonId(),
                personAddressDtoBuilder.addressLine1("Another Address").preferred(true).build());
        PersonDto updatedPerson = personService.getPerson(savedPerson.getPersonId(), false);

        // then
        assertNotNull(savedPersonAddressDto);
        assertNotNull(savedPersonAddressDto2);
        assertFalse(updatedPerson.getAddress().stream().filter(address ->
                address.getAddressLine1().equals(savedPersonAddressDto.getAddressLine1())
                && address.getAddressLine2().equals(savedPersonAddressDto.getAddressLine2())).findFirst()
                .map(PersonAddressDto::isPreferred).orElse(true));
        assertTrue(updatedPerson.getAddress().stream().filter(address ->
                        address.getAddressLine1().equals(savedPersonAddressDto2.getAddressLine1())
                                && address.getAddressLine2().equals(savedPersonAddressDto2.getAddressLine2())).findFirst()
                .map(PersonAddressDto::isPreferred).orElse(false));
        assertEquals(2, updatedPerson.getAddress().size());
    }

    @DisplayName("Test add a new person attribute")
    @Test
    void testAddPersonAttribute() {
        // given
        PersonDto personDto = personDtoBuilder.build();
        PersonAttributeDto personAttributeDto = personAttributeDtoBuilder.build();
        PersonDto savedPerson = personService.addPerson(personDto);

        // when
        PersonAttributeDto savedPersonAttribute = personService.addAttribute(savedPerson.getPersonId(),
                personAttributeDto);
        PersonDto updatedPerson = personService.getPerson(savedPerson.getPersonId(), false);

        // then
        assertNotNull(savedPerson);
        assertNotNull(savedPersonAttribute);
        assertEquals(1, updatedPerson.getAttributes().size());
    }
}
