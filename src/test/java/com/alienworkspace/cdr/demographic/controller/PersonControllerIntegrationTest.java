package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.helpers.Constants;
import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.demographic.service.client.MetadataFeignClient;
import com.alienworkspace.cdr.model.dto.metadata.*;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.dto.person.PersonAddressDto;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeDto;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeTypeDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonControllerIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    private PersonDto.PersonDtoBuilder personDtoBuilder;

    @MockitoBean
    private MetadataFeignClient metadataFeignClient;

    @BeforeEach
    public void setUp() {
        personRepository.deleteAll();
        personDtoBuilder = PersonDto.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"));

        when(metadataFeignClient.getCountry(1)).thenReturn(
                ResponseEntity.ok(CountryDto.builder().countryId(1).countryName("test country").build()));

        when(metadataFeignClient.getPersonLocation(1, 1, 1, 1, 1,
                null)).thenReturn(
                ResponseEntity.ok(CountryDto.builder().countryId(1).countryName("test country").build()));
    }

    @DisplayName("Test add person")
    @Test
    public void testAddPerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions response = mockMvc.perform(post(Constants.PERSON_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personId").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @DisplayName("Test update person")
    @Test
    public void testUpdatePerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        PersonDto updatePerson = personDtoBuilder
                .personId(savedPerson.getPersonId())
                .gender('F')
                .build();
        ResultActions response = mockMvc.perform(put(Constants.PERSON_BASE_URL + "/{id}", savedPerson.getPersonId())
                .contentType("application/json").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePerson)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.gender").value("F"))
                .andExpect(jsonPath("$.lastModifiedBy").value(1L))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @DisplayName("Test get person")
    @Test
    public void testGetPerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        ResultActions response = mockMvc.perform(get(Constants.PERSON_BASE_URL + "/{id}", savedPerson.getPersonId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @DisplayName("Test get persons")
    @Test
    public void testGetPersons() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        PersonDto personDto2 = personDtoBuilder
                .gender('F')
                .build();

        // when
        mockMvc.perform(post(Constants.PERSON_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));
        mockMvc.perform(post(Constants.PERSON_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto2)));

        ResultActions response = mockMvc.perform(get(Constants.PERSON_BASE_URL));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @DisplayName("Test delete person")
    @Test
    public void testDeletePerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(
                post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();

        mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{id}", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recordVoidRequest)));

        ResultActions response = mockMvc.perform(
                get(Constants.PERSON_BASE_URL + "/{id}", savedPerson.getPersonId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.voided").value(true))
                .andExpect(jsonPath("$.voidedBy").value(1L))
                .andExpect(jsonPath("$.voidReason").value("Test void reason"))
                .andExpect(jsonPath("$.voidedAt").isNotEmpty());

        // Verify that the person is not returned in the list of active persons
        ResultActions listResponse = mockMvc.perform(get(Constants.PERSON_BASE_URL));
        listResponse.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.personId == " + savedPerson.getPersonId() + ")]").doesNotExist());
    }

    @DisplayName("Test unknown delete person")
    @Test
    public void testDeleteNonExistingPerson() throws Exception {
        // given

        // when
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();

        ResultActions response = mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recordVoidRequest)));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage")
                        .value(CoreMatchers.containsStringIgnoringCase("ResourceNotFoundException")));
    }

    @DisplayName("Test add person name")
    @Test
    public void testAddPersonName() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        PersonNameDto personNameDto = PersonNameDto.builder()
                .firstName("John")
                .lastName("Doe")
                .preferred(true)
                .build();

        ResultActions response = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/names", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @DisplayName("Test update person name")
    @Test
    public void testUpdatePersonName() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        PersonNameDto personNameDto = PersonNameDto.builder()
                .firstName("John")
                .lastName("Umar")
                .preferred(true)
                .build();

        PersonNameDto personNameDto2 = PersonNameDto.builder()
                .firstName("John")
                .lastName("Doe")
                .preferred(true)
                .build();

        ResultActions nameAddResponse = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/names", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto2)));


        mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/names", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto)));

        String nameResponseEntity = nameAddResponse.andReturn().getResponse().getContentAsString();
        PersonNameDto personWithName = objectMapper.readValue(nameResponseEntity, PersonNameDto.class);
        Long personNameId = personWithName.getPersonNameId();

        ResultActions response = mockMvc.perform(put(Constants.PERSON_BASE_URL + "/{personId}/names/{personNameId}", 
                savedPerson.getPersonId(), personNameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(true)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.preferred").value(true))
                .andExpect(jsonPath("$.lastModifiedBy").value(1));
    }

    @DisplayName("Test delete person name")
    @Test
    public void testDeletePersonName() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        PersonNameDto personNameDto = PersonNameDto.builder()
                .firstName("John")
                .lastName("Doe")
                .preferred(true)
                .build();

        ResultActions nameAddResponse = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/names", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto)));

        String nameResponseEntity = nameAddResponse.andReturn().getResponse().getContentAsString();
        PersonNameDto personWithName = objectMapper.readValue(nameResponseEntity, PersonNameDto.class);
        Long personNameId = personWithName.getPersonNameId();

        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();

        mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{personId}/names/{personNameId}", 
                savedPerson.getPersonId(), personNameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voidRequest)));

        ResultActions response = mockMvc.perform(get(Constants.PERSON_BASE_URL + "/{id}", savedPerson.getPersonId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.name[0].voided").value(true))
                .andExpect(jsonPath("$.name[0].voidedBy").value(1L))
                .andExpect(jsonPath("$.name[0].voidReason").value("Test void reason"))
                .andExpect(jsonPath("$.name[0].voidedAt").isNotEmpty());
    }

    @DisplayName("Test add person address")
    @Test
    public void testAddPersonAddress() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        PersonAddressDto addressDto = PersonAddressDto.builder()
                .country(CountryDto.builder()
                        .countryId(1).build())
                .state(StateDto.builder().stateId(1).build())
                .county(CountyDto.builder().countyId(1).build())
                .city(CityDto.builder().cityId(1).build())
                .community(CommunityDto.builder().communityId(1).build())
                .addressLine1("123 Main St")
                .addressLine2("Suite 123")
                .addressLine3("test address line 3")
                .startDate(LocalDate.of(2023, 1, 1))
                .postalCode("12345")
                .preferred(true)
                .build();

        ResultActions response = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/addresses", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDto)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.addressLine1").value("123 Main St"))
                .andExpect(jsonPath("$.postalCode").value("12345"));
    }

    @DisplayName("Test update preferred person address throws")
    @Test
    public void testUpdatePersonAddressThrowsException() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        PersonAddressDto addressDto = PersonAddressDto.builder()
                .country(CountryDto.builder()
                        .countryId(1).build())
                .state(StateDto.builder().stateId(1).build())
                .county(CountyDto.builder().countyId(1).build())
                .city(CityDto.builder().cityId(1).build())
                .community(CommunityDto.builder().communityId(1).build())
                .addressLine1("123 Main St")
                .addressLine3("test address line 3")
                .startDate(LocalDate.of(2023, 1, 1))
                .postalCode("12345")
                .preferred(true)
                .build();

        ResultActions addressAddResponse = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/addresses", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDto)));

        String addressResponseEntity = addressAddResponse.andReturn().getResponse().getContentAsString();
        PersonAddressDto personWithAddress = objectMapper.readValue(addressResponseEntity, PersonAddressDto.class);
        Long addressId = personWithAddress.getPersonAddressId();

        ResultActions response = mockMvc.perform(put(Constants.PERSON_BASE_URL + "/{personId}/addresses/{addressId}", 
                savedPerson.getPersonId(), addressId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(true)));

        // then
        response.andDo(print())
                .andExpect(status().is4xxClientError());
    }


    @DisplayName("Test add new preferred person address")
    @Test
    public void testAddNewPreferredPersonAddress() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        PersonAddressDto addressDto = PersonAddressDto.builder()
                .country(CountryDto.builder()
                        .countryId(1).build())
                .state(StateDto.builder().stateId(1).build())
                .county(CountyDto.builder().countyId(1).build())
                .city(CityDto.builder().cityId(1).build())
                .community(CommunityDto.builder().communityId(1).build())
                .addressLine1("123 Main St")
                .addressLine3("test address line 3")
                .startDate(LocalDate.of(2023, 1, 1))
                .postalCode("12345")
                .preferred(true)
                .build();

        mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/addresses", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDto)));

        PersonAddressDto updateAddressDto = PersonAddressDto.builder()
                .country(CountryDto.builder()
                        .countryId(1).build())
                .state(StateDto.builder().stateId(1).build())
                .county(CountyDto.builder().countyId(1).build())
                .city(CityDto.builder().cityId(1).build())
                .community(CommunityDto.builder().communityId(1).build())
                .addressLine1("456 Oak Ave")
                .addressLine3("test address line 3")
                .startDate(LocalDate.of(2023, 1, 12))
                .postalCode("67890")
                .preferred(true)
                .build();

        ResultActions response = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/addresses", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAddressDto)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.preferred").value(true))
                .andExpect(jsonPath("$.lastModifiedBy").value(1L));
    }

    @DisplayName("Test delete person address")
    @Test
    public void testDeletePersonAddress() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        PersonAddressDto addressDto = PersonAddressDto.builder()
                .country(CountryDto.builder()
                        .countryId(1).build())
                .state(StateDto.builder().stateId(1).build())
                .county(CountyDto.builder().countyId(1).build())
                .city(CityDto.builder().cityId(1).build())
                .community(CommunityDto.builder().communityId(1).build())
                .addressLine1("123 Main St")
                .addressLine3("test address line 3")
                .startDate(LocalDate.of(2023, 1, 1))
                .postalCode("12345")
                .preferred(true)
                .build();

        ResultActions addressAddResponse = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/addresses", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDto)));

        String addressResponseEntity = addressAddResponse.andReturn().getResponse().getContentAsString();
        PersonAddressDto personWithAddress = objectMapper.readValue(addressResponseEntity, PersonAddressDto.class);
        Long addressId = personWithAddress.getPersonAddressId();

        PersonAddressDto addressDto2 = PersonAddressDto.builder()
                .country(CountryDto.builder()
                        .countryId(1).build())
                .state(StateDto.builder().stateId(1).build())
                .county(CountyDto.builder().countyId(1).build())
                .city(CityDto.builder().cityId(1).build())
                .community(CommunityDto.builder().communityId(1).build())
                .addressLine1("Flat 2")
                .addressLine2("No. 213")
                .addressLine3("St. Johns Street")
                .startDate(LocalDate.of(2023, 1, 1))
                .postalCode("12345")
                .preferred(true)
                .build();

        ResultActions addressAddResponse2 = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/addresses", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDto2)));

        String addressResponseEntity2 = addressAddResponse2.andReturn().getResponse().getContentAsString();
        PersonAddressDto personWithAddress2 = objectMapper.readValue(addressResponseEntity2, PersonAddressDto.class);
        Long addressId2 = personWithAddress2.getPersonAddressId();

        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();

        mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{personId}/addresses/{addressId}", 
                savedPerson.getPersonId(), addressId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voidRequest)));

        ResultActions response = mockMvc.perform(get(Constants.PERSON_BASE_URL + "/{id}/addresses/{addressId}",
                savedPerson.getPersonId(), addressId).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(savedPerson.getPersonId())));


        ResultActions response2 = mockMvc.perform(get(Constants.PERSON_BASE_URL + "/{id}/addresses/{addressId}",
                savedPerson.getPersonId(), addressId2).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(savedPerson.getPersonId())));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.voided").value(true))
                .andExpect(jsonPath("$.preferred").value(false))
                .andExpect(jsonPath("$.voidedBy").value(1L))
                .andExpect(jsonPath("$.voidReason").value("Test void reason"))
                .andExpect(jsonPath("$.voidedAt").isNotEmpty());

        response2.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.voided").value(false))
                .andExpect(jsonPath("$.preferred").value(true));
    }

    @DisplayName("Test add person attribute")
    @Test
    public void testAddPersonAttribute() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        ResultActions resultActionAttributeType = mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PersonAttributeTypeDto.builder()
                        .name("Test Name")
                        .build())));

        String responseEntityAttributeType = resultActionAttributeType.andReturn().getResponse().getContentAsString();
        PersonAttributeTypeDto savedAttributeType = objectMapper.readValue(responseEntityAttributeType,
                PersonAttributeTypeDto.class);

        PersonAttributeDto attributeDto = PersonAttributeDto.builder()
                .value("Test Value")
                .personAttributeType(savedAttributeType)
                .preferred(true)
                .build();

        ResultActions response = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/attributes", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attributeDto)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.person_id").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.value").value("Test Value"));
    }

    @DisplayName("Test update person attribute")
    @Test
    public void testUpdatePersonAttribute() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        ResultActions resultActionAttributeType = mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PersonAttributeTypeDto.builder()
                        .name("Test Name")
                        .build())));

        String responseEntityAttributeType = resultActionAttributeType.andReturn().getResponse().getContentAsString();
        PersonAttributeTypeDto savedAttributeType = objectMapper.readValue(responseEntityAttributeType,
                PersonAttributeTypeDto.class);

        PersonAttributeDto attributeDto = PersonAttributeDto.builder()
                .value("Test Value")
                .personAttributeType(savedAttributeType)
                .preferred(true)
                .build();

        ResultActions attributeAddResponse = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/attributes", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attributeDto)));

        String attributeResponseEntity = attributeAddResponse.andReturn().getResponse().getContentAsString();
        PersonAttributeDto personWithAttribute = objectMapper.readValue(attributeResponseEntity, PersonAttributeDto.class);
        Long attributeId = personWithAttribute.getPersonAttributeId();

        PersonAttributeDto updateAttributeDto = PersonAttributeDto.builder()
                .value("Updated Value")
                .personAttributeType(savedAttributeType)
                .preferred(false)
                .build();

        ResultActions attributeAddResponse2 = mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/attributes", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateAttributeDto)));

        String attributeResponseEntity2 = attributeAddResponse2.andReturn().getResponse().getContentAsString();
        PersonAttributeDto personWithAttribute2 = objectMapper.readValue(attributeResponseEntity2, PersonAttributeDto.class);

        ResultActions response = mockMvc.perform(put(Constants.PERSON_BASE_URL + "/{personId}/attributes/{attributeId}", 
                savedPerson.getPersonId(), attributeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(true)));

        // then
        assertFalse(personWithAttribute2.isPreferred());
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.person_id").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.preferred").value(true))
                .andExpect(jsonPath("$.personAttributeType.personAttributeTypeId")
                        .value(savedAttributeType.getPersonAttributeTypeId()));

    }

    @DisplayName("Test delete person attribute")
    @Test
    public void testDeletePersonAttribute() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonDto savedPerson = objectMapper.readValue(responseEntity, PersonDto.class);

        ResultActions resultActionAttributeType = mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PersonAttributeTypeDto.builder()
                        .name("Test Name")
                        .build())));

        String responseEntityAttributeType = resultActionAttributeType.andReturn().getResponse().getContentAsString();
        PersonAttributeTypeDto savedAttributeType = objectMapper.readValue(responseEntityAttributeType,
                PersonAttributeTypeDto.class);


        PersonAttributeDto attributeDto = PersonAttributeDto.builder()
                .value("Test Value")
                .personAttributeType(savedAttributeType)
                .preferred(true)
                .build();

        ResultActions attributeAddResponse = mockMvc.perform(post(
                Constants.PERSON_BASE_URL + "/{personId}/attributes", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attributeDto)));

        String attributeResponseEntity = attributeAddResponse.andReturn().getResponse().getContentAsString();
        PersonAttributeDto personWithAttribute = objectMapper.readValue(attributeResponseEntity, PersonAttributeDto.class);
        Long attributeId = personWithAttribute.getPersonAttributeId();

        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();

        mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{personId}/attributes/{attributeId}", 
                savedPerson.getPersonId(), attributeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voidRequest)));

        ResultActions response = mockMvc.perform(get(Constants.PERSON_BASE_URL + "/{id}", savedPerson.getPersonId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(savedPerson.getPersonId()))
                .andExpect(jsonPath("$.attributes[0].voided").value(true))
                .andExpect(jsonPath("$.attributes[0].voidedBy").value(1L))
                .andExpect(jsonPath("$.attributes[0].voidReason").value("Test void reason"))
                .andExpect(jsonPath("$.attributes[0].voidedAt").isNotEmpty());
    }
}
