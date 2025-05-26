package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.helpers.Constants;
import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

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

    @BeforeEach
    public void setUp() {
        personRepository.deleteAll();
        personDtoBuilder = PersonDto.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"));
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
                .voidReason("test")
                .build();

        mockMvc.perform(delete(Constants.PERSON_BASE_URL + "/{id}", savedPerson.getPersonId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recordVoidRequest)));

        ResultActions response = mockMvc.perform(
                get(Constants.PERSON_BASE_URL + "/{id}", savedPerson.getPersonId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.voided").value(true))
                .andExpect(jsonPath("$.voidedBy").value(1L));
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
}
