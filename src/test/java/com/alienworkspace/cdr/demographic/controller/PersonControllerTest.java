package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.demographic.service.impl.PersonServiceImpl;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    public PersonServiceImpl personService;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonDto.PersonDtoBuilder personDtoBuilder;

    @MockitoBean
    private PersonRepository personRepository;

    @BeforeEach
    void setup() {
        personDtoBuilder = PersonDto.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .dead(false)
                .deathDate(null)
                .causeOfDeath(null);
    }

    @DisplayName("Test create person")
    @Test
    public void testCreatePerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .personId(1L)
                .build();
        given(personService.addPerson(any(PersonDto.class))).willReturn(personDto);

        // when
        ResultActions response = mockMvc.perform(post("/demographic/person").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        // then
        verify(personService).addPerson(personDto);
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personId").value(1L))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @DisplayName("Test update person")
    @Test
    public void testUpdatePerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .personId(1L)
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
        given(personService.updatePerson(any(PersonDto.class))).willReturn(personDto);

        // when
        ResultActions response = mockMvc.perform(put("/demographic/person").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        // then
        verify(personService).updatePerson(personDto);
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1L))
                .andExpect(jsonPath("$.gender").value("F"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @DisplayName("Test update person")
    @Test
    public void testUpdateNonExistingPerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .personId(2L)
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
        given(personService.updatePerson(personDto)).willThrow(
                new ResourceNotFoundException("ResourceNotFoundException: Person not found"));

        // when
        ResultActions response = mockMvc.perform(put("/demographic/person").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personDto)));

        // then
        verify(personService).updatePerson(personDto);
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.apiPath", CoreMatchers.containsStringIgnoringCase("demographic/person")))
                .andExpect(jsonPath("$.errorMessage", CoreMatchers.containsStringIgnoringCase ("ResourceNotFoundException")))
                .andExpect(jsonPath("$.errorCode").value(404));
    }

    @DisplayName("Test get person")
    @Test
    public void testGetPerson() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .personId(1L)
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .dead(false)
                .build();
        given(personService.getPerson(any(Long.class))).willReturn(personDto);

        // when
        ResultActions response = mockMvc.perform(get("/demographic/person/{personId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        verify(personService).getPerson(1L);
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1L))
                .andExpect(jsonPath("$.gender").value("F"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));
    }

    @DisplayName("Test get persons")
    @Test
    public void testGetPersons() throws Exception {
        // given
        PersonDto personDto = personDtoBuilder
                .personId(1L)
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .dead(false)
                .build();
        given(personService.getPersons()).willReturn(List.of(personDto));

        // when
        ResultActions response = mockMvc.perform(get("/demographic/person", 1L)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        verify(personService).getPersons();
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].personId").value(1L))
                .andExpect(jsonPath("$.[0].gender").value("F"))
                .andExpect(jsonPath("$.[0].birthDate").value("1990-01-01"));
    }

    @DisplayName("Test delete person")
    @Test
    public void testDeletePerson() throws Exception {
        // given
        given(personService.deletePerson(any(RecordVoidRequest.class))).willReturn("Person deleted successfully");
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .resourceId(String.valueOf(1L))
                .resourceUUID(null)
                .voidReason("test")
                .build();

        // when
        ResultActions response = mockMvc.perform(delete("/demographic/person").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recordVoidRequest)));

        // then
        verify(personService).deletePerson(recordVoidRequest);
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
