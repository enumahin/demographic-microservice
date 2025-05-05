package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.repository.PersonNameRepository;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonNameControllerIntegrationTest extends AbstractionContainerBaseTest {

    private static final String BASE_URL = "/demographic/person-name";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonNameRepository personNameRepository;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    private PersonNameDto.PersonNameDtoBuilder personNameDtoBuilder;

    @BeforeEach
    public void setUp() {
        personNameRepository.deleteAll();
        personNameDtoBuilder = PersonNameDto.builder()
                .firstName("John")
                .middleName("Umar")
                .lastName("Doe");
    }

    @DisplayName("Test add person name")
    @Test
    public void testAddPersonName() throws Exception {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder
                .build();

        // when
        ResultActions response = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personNameId").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.middleName").value("Umar"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @DisplayName("Test get person name")
    @Test
    public void testGetPersonName() throws Exception {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder
                .build();

        // when
        ResultActions resultActions = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonNameDto savedPersonName = objectMapper.readValue(responseEntity, PersonNameDto.class);

        ResultActions response = mockMvc.perform(get(BASE_URL + "/" + savedPersonName.getPersonNameId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personNameId").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.middleName").value("Umar"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @DisplayName("Test get person names")
    @Test
    public void testGetPersonNames() throws Exception {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder
                .build();

        PersonNameDto personNameDto2 = personNameDtoBuilder
                .otherName("Jnr.")
                .build();

        // when
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto)));
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto2)));

        ResultActions response = mockMvc.perform(get(BASE_URL + "/" + personNameDto.getPersonId() + "/names"));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @DisplayName("Test delete person name")
    @Test
    public void testDeletePersonName() throws Exception {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder
                .build();

        // when
        mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto)));
        ResultActions resultActions = mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personNameDto)));

        String responseEntity = resultActions.andReturn().getResponse().getContentAsString();
        PersonNameDto savedPersonName = objectMapper.readValue(responseEntity, PersonNameDto.class);
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .resourceId(String.valueOf(savedPersonName.getPersonNameId()))
                .build();

        mockMvc.perform(delete(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recordVoidRequest)));

        ResultActions response = mockMvc.perform(get(BASE_URL + "/" + savedPersonName.getPersonNameId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personNameId").value(CoreMatchers.notNullValue()))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.middleName").value("Umar"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.voided").value(true))
                .andExpect(jsonPath("$.voidedBy").value(1L));
    }

    @DisplayName("Test unknown delete person name")
    @Test
    public void testDeleteNonExistingPersonName() throws Exception {
        // given

        // when
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .resourceId(String.valueOf(1L))
                .build();

        ResultActions response = mockMvc.perform(delete(BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(recordVoidRequest)));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.errorMessage")
                        .value(CoreMatchers.containsStringIgnoringCase("ResourceNotFoundException")));
    }

}
