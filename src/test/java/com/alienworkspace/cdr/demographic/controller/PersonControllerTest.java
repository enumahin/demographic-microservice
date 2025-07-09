package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.helpers.Constants;
import com.alienworkspace.cdr.demographic.service.PersonService;
import com.alienworkspace.cdr.model.dto.metadata.*;
import com.alienworkspace.cdr.model.dto.person.*;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.doNothing;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @MockitoBean
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    private PersonDto.PersonDtoBuilder personDtoBuilder;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // For LocalDate serialization
        personDtoBuilder = PersonDto.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"));
    }

    @Test
    @DisplayName("Test get all persons")
    void testGetPersons() throws Exception {
        // given
        PersonDto person1 = personDtoBuilder.personId(1L).build();
        PersonDto person2 = personDtoBuilder.personId(2L).build();
        List<PersonDto> persons = Arrays.asList(person1, person2);

        when(personService.getPersons()).thenReturn(persons);

        // when & then
        mockMvc.perform(get(Constants.PERSON_BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].personId").value(1))
                .andExpect(jsonPath("$[1].personId").value(2));
    }

    @Test
    @DisplayName("Test get person by ID")
    void testGetPerson() throws Exception {
        // given
        PersonDto person = personDtoBuilder.personId(1L).build();
        when(personService.getPerson(1L, false)).thenReturn(person);

        // when & then
        mockMvc.perform(get(Constants.PERSON_BASE_URL + "/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @Test
    @DisplayName("Test add person")
    void testAddPerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder.build();
        PersonDto savedPerson = personDtoBuilder.personId(1L).build();
        
        when(personService.addPerson(any(PersonDto.class))).thenReturn(savedPerson);

        // when & then
        mockMvc.perform(post(Constants.PERSON_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personId").value(1))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @Test
    @DisplayName("Test update person")
    void testUpdatePerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder.gender('F').build();
        PersonDto updatedPerson = personDtoBuilder.personId(1L).gender('F').build();
        
        when(personService.updatePerson(eq(1L), any(PersonDto.class))).thenReturn(updatedPerson);

        // when & then
        mockMvc.perform(put(Constants.PERSON_BASE_URL + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1))
                .andExpect(jsonPath("$.gender").value("F"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @Test
    @DisplayName("Test delete person")
    void testDeletePerson() throws Exception {
        // given
        ResponseDto response = new ResponseDto(200, "Person deleted successfully");
        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();
        
        when(personService.deletePerson(eq(1L), any(RecordVoidRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voidRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.statusMessage").value("Person deleted successfully"));
    }

    @Test
    @DisplayName("Test add person name")
    void testAddPersonName() throws Exception {
        // given
        PersonNameDto nameDto = PersonNameDto.builder()
                .firstName("John")
                .lastName("Doe")
                .personId(1L)
                .preferred(true)
                .build();
        PersonDto updatedPerson = personDtoBuilder.personId(1L).build();
        
        when(personService.addPersonName(eq(1L), any(PersonNameDto.class))).thenReturn(nameDto);

        // when & then
        mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/names", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nameDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1));
    }

    @Test
    @DisplayName("Test update person name")
    void testUpdatePersonName() throws Exception {
        // given
        PersonNameDto nameDto = PersonNameDto.builder()
                .firstName("John")
                .lastName("Doe")
                .personId(1L)
                .preferred(true)
                .build();
        PersonDto updatedPerson = personDtoBuilder.personId(1L).build();
        
        when(personService.updatePersonName(eq(1L), eq(1L), any(Boolean.class))).thenReturn(nameDto);

        // when & then
        mockMvc.perform(put(Constants.PERSON_BASE_URL + "/{personId}/names/{nameId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(true)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1));
    }

    @Test
    @DisplayName("Test delete person name")
    void testDeletePersonName() throws Exception {
        // given
        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();
        
        doNothing().when(personService).deletePersonName(eq(1L), eq(1L), any(RecordVoidRequest.class));

        // when & then
        mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{personId}/names/{nameId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voidRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test add person address")
    void testAddPersonAddress() throws Exception {
        // given
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
        PersonDto updatedPerson = personDtoBuilder.personId(1L).build();
        
        when(personService.addAddress(eq(1L), any(PersonAddressDto.class))).thenReturn(addressDto);

        // when & then
        mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/addresses", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addressDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(notNullValue()));
    }

    @Test
    @DisplayName("Test update person address")
    void testUpdatePersonAddress() throws Exception {
        // given
        PersonAddressDto addressDto = PersonAddressDto.builder()
                .addressLine1("123 Main St")
                .postalCode("12345")
                .preferred(true)
                .build();
        
        when(personService.updateAddress(eq(1L), eq(1L), any(Boolean.class))).thenReturn(addressDto);

        // when & then
        mockMvc.perform(put(Constants.PERSON_BASE_URL + "/{personId}/addresses/{addressId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(true)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test delete person address")
    void testDeletePersonAddress() throws Exception {
        // given
        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();
        
        doNothing().when(personService).deleteAddress(eq(1L), eq(1L), any(RecordVoidRequest.class));

        // when & then
        mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{personId}/addresses/{addressId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voidRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test add person attribute")
    void testAddPersonAttribute() throws Exception {
        // given
        PersonAttributeDto attributeDto = PersonAttributeDto.builder()
                .value("Test Value")
                .personAttributeType(PersonAttributeTypeDto.builder()
                        .personAttributeTypeId(1)
                        .build())
                .preferred(true)
                .build();
        
        when(personService.addAttribute(eq(1L), any(PersonAttributeDto.class))).thenReturn(attributeDto);

        // when & then
        mockMvc.perform(post(Constants.PERSON_BASE_URL + "/{personId}/attributes", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(attributeDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(notNullValue()));
    }

    @Test
    @DisplayName("Test update person attribute")
    void testUpdatePersonAttribute() throws Exception {
        // given
        PersonAttributeDto attributeDto = PersonAttributeDto.builder()
                .value("Test Value")
                .personAttributeType(PersonAttributeTypeDto.builder()
                        .personAttributeTypeId(1)
                        .build())
                .preferred(true)
                .build();
        
        when(personService.updateAttribute(eq(1L), eq(1L), any(Boolean.class))).thenReturn(attributeDto);

        // when & then
        mockMvc.perform(put(Constants.PERSON_BASE_URL + "/{personId}/attributes/{attributeId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(true)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test delete person attribute")
    void testDeletePersonAttribute() throws Exception {
        // given
        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();
        
        doNothing().when(personService).deleteAttribute(eq(1L), eq(1L), any(RecordVoidRequest.class));

        // when & then
        mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{personId}/attributes/{attributeId}", 1L, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voidRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
