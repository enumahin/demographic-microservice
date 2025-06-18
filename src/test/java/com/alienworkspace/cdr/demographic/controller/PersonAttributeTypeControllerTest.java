package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.helpers.Constants;
import com.alienworkspace.cdr.demographic.service.PersonAttributeTypeService;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeTypeDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonAttributeTypeController.class)
class PersonAttributeTypeControllerTest {

    @MockitoBean
    private PersonAttributeTypeService personAttributeTypeService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private PersonAttributeTypeDto attributeTypeDto;

    @BeforeEach
    void setUp() {

        attributeTypeDto = PersonAttributeTypeDto.builder()
                .personAttributeTypeId(1)
                .name("Blood Type")
                .description("Person's blood type")
                .format("java.lang.String")
                .build();
        attributeTypeDto.setCreatedAt(LocalDateTime.now());
        attributeTypeDto.setCreatedBy(1L);
    }

    @Test
    @DisplayName("Test save person attribute type")
    void testSavePersonAttributeType() throws Exception {
        when(personAttributeTypeService.savePersonAttributeType(any(PersonAttributeTypeDto.class)))
                .thenReturn(attributeTypeDto);

        mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(attributeTypeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personAttributeTypeId", is(1)))
                .andExpect(jsonPath("$.name", is("Blood Type")))
                .andExpect(jsonPath("$.description", is("Person's blood type")))
                .andExpect(jsonPath("$.format", is("java.lang.String")));
    }

    @Test
    @DisplayName("Test update person attribute type")
    void testUpdatePersonAttributeType() throws Exception {
        PersonAttributeTypeDto updatedDto = PersonAttributeTypeDto.builder()
                .personAttributeTypeId(1)
                .build();

        when(personAttributeTypeService.updatePersonAttributeType(eq(1), any(PersonAttributeTypeDto.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personAttributeTypeId", is(1)));
    }

    @Test
    @DisplayName("Test get person attribute type by id")
    void testGetPersonAttributeTypeById() throws Exception {
        when(personAttributeTypeService.getPersonAttributeTypeById(1))
                .thenReturn(attributeTypeDto);

        mockMvc.perform(get(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personAttributeTypeId", is(1)))
                .andExpect(jsonPath("$.name", is("Blood Type")))
                .andExpect(jsonPath("$.description", is("Person's blood type")))
                .andExpect(jsonPath("$.format", is("java.lang.String")));
    }

    @Test
    @DisplayName("Test get all person attribute types")
    void testGetAllPersonAttributeTypes() throws Exception {
        PersonAttributeTypeDto type2 = PersonAttributeTypeDto.builder()
                .personAttributeTypeId(2)
                .name("Weight")
                .description("Person's weight")
                .build();

        when(personAttributeTypeService.getAllPersonAttributeTypes())
                .thenReturn(Arrays.asList(attributeTypeDto, type2));

        mockMvc.perform(get(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personAttributeTypeId", is(1)))
                .andExpect(jsonPath("$[0].name", is("Blood Type")))
                .andExpect(jsonPath("$[1].personAttributeTypeId", is(2)))
                .andExpect(jsonPath("$[1].name", is("Weight")));
    }

    @Test
    @DisplayName("Test delete person attribute type")
    void testDeletePersonAttributeType() throws Exception {
        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();

        doNothing().when(personAttributeTypeService)
                .deletePersonAttributeType(eq(1), any(RecordVoidRequest.class));

        mockMvc.perform(delete(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voidRequest)))
                .andExpect(status().isOk());
    }
} 