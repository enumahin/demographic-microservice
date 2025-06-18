package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.helpers.Constants;
import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.repository.PersonAttributeTypeRepository;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeTypeDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 class PersonAttributeTypeControllerIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonAttributeTypeRepository personAttributeTypeRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonAttributeTypeDto.PersonAttributeTypeDtoBuilder attributeTypeDtoBuilder;

    @BeforeEach
     void setUp() {
       personRepository.deleteAll();
        personAttributeTypeRepository.deleteAll();
        attributeTypeDtoBuilder = PersonAttributeTypeDto.builder()
                .name("Test Attribute Type")
                .description("Test Description")
                .format("java.lang.String");
    }

    @DisplayName("Test save person attribute type")
    @Test
     void testSavePersonAttributeType() throws Exception {
        // given
        PersonAttributeTypeDto typeDto = attributeTypeDtoBuilder.build();

        // when
        ResultActions response = mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(typeDto)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personAttributeTypeId").exists())
                .andExpect(jsonPath("$.name").value("Test Attribute Type"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.format").value("java.lang.String"));
    }

    @DisplayName("Test update person attribute type")
    @Test
     void testUpdatePersonAttributeType() throws Exception {
        // given
        PersonAttributeTypeDto typeDto = attributeTypeDtoBuilder.build();

        // when
        ResultActions saveResponse = mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(typeDto)));

        String responseContent = saveResponse.andReturn().getResponse().getContentAsString();
        PersonAttributeTypeDto savedType = objectMapper.readValue(responseContent, PersonAttributeTypeDto.class);

        PersonAttributeTypeDto updateTypeDto = attributeTypeDtoBuilder
                .name("Updated Name")
                .description("Updated Description")
                .format("java.lang.Integer")
                .build();

        ResultActions response = mockMvc.perform(put(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL + "/{id}", 
                savedType.getPersonAttributeTypeId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTypeDto)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personAttributeTypeId").value(savedType.getPersonAttributeTypeId()))
                .andExpect(jsonPath("$.lastModifiedBy").value(1L))
                .andExpect(jsonPath("$.format").value("java.lang.Integer"));
    }

    @DisplayName("Test get person attribute type by ID")
    @Test
     void testGetPersonAttributeTypeById() throws Exception {
        // given
        PersonAttributeTypeDto typeDto = attributeTypeDtoBuilder.build();

        // when
        ResultActions saveResponse = mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(typeDto)));

        String responseContent = saveResponse.andReturn().getResponse().getContentAsString();
        PersonAttributeTypeDto savedType = objectMapper.readValue(responseContent, PersonAttributeTypeDto.class);

        ResultActions response = mockMvc.perform(get(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL + "/{id}", 
                savedType.getPersonAttributeTypeId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personAttributeTypeId").value(savedType.getPersonAttributeTypeId()))
                .andExpect(jsonPath("$.name").value("Test Attribute Type"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.format").value("java.lang.String"));
    }

    @DisplayName("Test get all person attribute types")
    @Test
     void testGetAllPersonAttributeTypes() throws Exception {
        // given
        PersonAttributeTypeDto type1 = attributeTypeDtoBuilder.build();
        PersonAttributeTypeDto type2 = attributeTypeDtoBuilder
                .name("Second Type")
                .description("Second Description")
                .build();

        // when
        mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(type1)));

        mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(type2)));

        ResultActions response = mockMvc.perform(get(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Test Attribute Type"))
                .andExpect(jsonPath("$[1].name").value("Second Type"));
    }

    @DisplayName("Test delete person attribute type")
    @Test
     void testDeletePersonAttributeType() throws Exception {
        // given
        PersonAttributeTypeDto typeDto = attributeTypeDtoBuilder.build();

        // when
        ResultActions saveResponse = mockMvc.perform(post(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(typeDto)));

        String responseContent = saveResponse.andReturn().getResponse().getContentAsString();
        PersonAttributeTypeDto savedType = objectMapper.readValue(responseContent, PersonAttributeTypeDto.class);

        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();

        mockMvc.perform(delete(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL + "/{id}", 
                savedType.getPersonAttributeTypeId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(voidRequest)));

        ResultActions response = mockMvc.perform(get(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL + "/{id}", 
                savedType.getPersonAttributeTypeId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personAttributeTypeId").value(savedType.getPersonAttributeTypeId()))
                .andExpect(jsonPath("$.voided").value(true))
                .andExpect(jsonPath("$.voidedBy").value(1L))
                .andExpect(jsonPath("$.voidReason").value("Test void reason"))
                .andExpect(jsonPath("$.voidedAt").isNotEmpty());

        // Verify that the type is not returned in the list of active types
        ResultActions listResponse = mockMvc.perform(get(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL));
        listResponse.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.personAttributeTypeId == " + savedType.getPersonAttributeTypeId() + ")]")
                        .doesNotExist());
    }
} 