package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.helpers.Constants;
import com.alienworkspace.cdr.demographic.service.impl.PersonNameServiceImpl;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonNameController.class)
public class PersonNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public ObjectMapper objectMapper;

    @MockitoBean
    public PersonNameServiceImpl personNameService;

    private PersonNameDto.PersonNameDtoBuilder personNameDtoBuilder;

    @BeforeEach
    public void setup() {
        personNameDtoBuilder = PersonNameDto.builder()
                .personId(1L)
                .firstName("John")
                .lastName("Doe")
                .preferred(true);
    }

    @DisplayName("Test add person name")
    @Test
    public void testAddPersonName() throws Exception {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder.build();

        PersonNameDto mappedResponse = personNameDtoBuilder.personNameId(1L).build();

        given(personNameService.addPersonName(any(PersonNameDto.class))).willReturn(mappedResponse);

        // when
        ResultActions response = mockMvc.perform(post(Constants.PERSON_NAME_BASE_URL).contentType("application/json")
                .content(objectMapper.writeValueAsString(personNameDto)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personId").value(1L))
                .andExpect(jsonPath("$.personNameId").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.preferred").value(true));
        verify(personNameService).addPersonName(eq(personNameDto));
        verifyNoMoreInteractions(personNameService);
    }

    @DisplayName("Test get person name")
    @Test
    public void testGetPersonName() throws Exception {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder.build();

        PersonNameDto mappedResponse = personNameDtoBuilder.personNameId(1L).build();

        given(personNameService.addPersonName(any(PersonNameDto.class))).willReturn(mappedResponse);
        given(personNameService.findPersonName(any(Long.class))).willReturn(mappedResponse);

        // when
        mockMvc.perform(post(Constants.PERSON_NAME_BASE_URL).contentType("application/json")
                .content(objectMapper.writeValueAsString(personNameDto)));
        ResultActions response = mockMvc.perform(get(Constants.PERSON_NAME_BASE_URL +"/"+1L));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1L))
                .andExpect(jsonPath("$.personNameId").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.preferred").value(true));
        verify(personNameService).addPersonName(eq(personNameDto));
        verify(personNameService).findPersonName(eq(1L));
        verifyNoMoreInteractions(personNameService);
    }

    @DisplayName("Test get person names by person id")
    @Test
    public void testGetPersonNamesByPersonId() throws Exception {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder.build();
        PersonNameDto personNameDto2 = personNameDtoBuilder
                .otherName("Jr.")
                .build();

        PersonNameDto mappedResponse = personNameDtoBuilder.personNameId(1L).build();
        PersonNameDto mappedResponse2 = personNameDtoBuilder.personNameId(2L).otherName("Jr.").build();

        given(personNameService.addPersonName(eq(personNameDto))).willReturn(mappedResponse);
        given(personNameService.addPersonName(eq(personNameDto2))).willReturn(mappedResponse2);
        given(personNameService.findPersonNamesByPersonId(any(Long.class)))
                .willReturn(List.of(mappedResponse, mappedResponse2));

        // when
        mockMvc.perform(post(Constants.PERSON_NAME_BASE_URL).contentType("application/json")
                .content(objectMapper.writeValueAsString(personNameDto)));
        ResultActions response = mockMvc.perform(get(Constants.PERSON_NAME_BASE_URL +"/"+1L+"/names"));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
        verify(personNameService).addPersonName(eq(personNameDto));
        verify(personNameService).addPersonName(eq(personNameDto2));
        verify(personNameService).findPersonNamesByPersonId(eq(1L));
        verifyNoMoreInteractions(personNameService);
    }

    @DisplayName("Test delete person name")
    @Test
    public void testDeletePersonName() throws Exception {
        // given
        PersonNameDto mappedResponse = personNameDtoBuilder
                .personNameId(1L)
                .otherName("Smith")
                .preferred(false)
                .lastName("Umar")
                .build();
        mappedResponse.setVoided(true);
        mappedResponse.setVoidedAt(LocalDateTime.now());
        mappedResponse.setVoidedBy(1L);
        mappedResponse.setVoidReason("test");

        given(personNameService.deletePersonName(any(Long.class), any(RecordVoidRequest.class)))
                .willReturn("Person name successful deleted.");
        given(personNameService.findPersonName(any(Long.class))).willReturn(mappedResponse);

        // when
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder().voidReason("test").build();

        ResultActions deleteResponse = mockMvc.perform(delete(Constants.PERSON_NAME_BASE_URL + "/{id}", 1L).contentType("application/json")
                .content(objectMapper.writeValueAsString(recordVoidRequest)));
        ResultActions response = mockMvc.perform(get(Constants.PERSON_NAME_BASE_URL+ "/{id}", 1L)
                .contentType("application/json"));

        // then
        deleteResponse
                .andDo(print())
                .andExpect(jsonPath("$.statusMessage")
                        .value(CoreMatchers.containsStringIgnoringCase("Person name successful deleted.")));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personId").value(1L))
                .andExpect(jsonPath("$.personNameId").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Umar"))
                .andExpect(jsonPath("$.otherName").value("Smith"))
                .andExpect(jsonPath("$.voidedBy").value(1))
                .andExpect(jsonPath("$.voidedAt").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.voidReason").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.preferred").value(false));
        verify(personNameService).deletePersonName(eq(1L), eq(recordVoidRequest));
        verify(personNameService).findPersonName(1L);
        verifyNoMoreInteractions(personNameService);
    }
}
