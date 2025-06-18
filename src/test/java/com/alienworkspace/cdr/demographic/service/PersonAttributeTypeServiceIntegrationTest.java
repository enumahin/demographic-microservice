package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.repository.PersonAttributeTypeRepository;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeTypeDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PersonAttributeTypeServiceIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private PersonAttributeTypeRepository personAttributeTypeRepository;

    @Autowired
    private PersonAttributeTypeService personAttributeTypeService;

    private PersonAttributeTypeDto.PersonAttributeTypeDtoBuilder attributeTypeDtoBuilder;

    @BeforeEach
     void setup() {
        personAttributeTypeRepository.deleteAll();
        attributeTypeDtoBuilder = PersonAttributeTypeDto.builder()
                .name("Blood Type")
                .description("Person's blood type")
                .format("java.lang.String");
    }

    @DisplayName("Test save person attribute type")
    @Test
     void testSavePersonAttributeType() {
        // given
        PersonAttributeTypeDto typeDto = attributeTypeDtoBuilder.build();

        // when
        PersonAttributeTypeDto savedType = personAttributeTypeService.savePersonAttributeType(typeDto);

        // then
        assertNotNull(savedType.getPersonAttributeTypeId());
        assertEquals(typeDto.getName(), savedType.getName());
        assertEquals(typeDto.getDescription(), savedType.getDescription());
        assertEquals(typeDto.getFormat(), savedType.getFormat());
        assertNotNull(savedType.getCreatedAt());
        assertEquals(1L, savedType.getCreatedBy());
        assertFalse(savedType.getVoided());
    }

    @DisplayName("Test update person attribute type")
    @Test
     void testUpdatePersonAttributeType() {
        // given
        PersonAttributeTypeDto typeDto = attributeTypeDtoBuilder.build();
        PersonAttributeTypeDto savedType = personAttributeTypeService.savePersonAttributeType(typeDto);

        // when
        savedType.setName("Updated Blood Type");
        savedType.setDescription("Updated description");
        PersonAttributeTypeDto updatedType = personAttributeTypeService.updatePersonAttributeType(
                savedType.getPersonAttributeTypeId(), savedType);

        // then
        assertEquals(savedType.getFormat(), updatedType.getFormat());
        assertNotNull(updatedType.getLastModifiedAt());
        assertEquals(1L, updatedType.getLastModifiedBy());
    }

    @DisplayName("Test get person attribute type by id")
    @Test
     void testGetPersonAttributeTypeById() {
        // given
        PersonAttributeTypeDto typeDto = attributeTypeDtoBuilder.build();
        PersonAttributeTypeDto savedType = personAttributeTypeService.savePersonAttributeType(typeDto);

        // when
        PersonAttributeTypeDto foundType = personAttributeTypeService.getPersonAttributeTypeById(
                savedType.getPersonAttributeTypeId());

        // then
        assertEquals(savedType.getPersonAttributeTypeId(), foundType.getPersonAttributeTypeId());
        assertEquals(savedType.getName(), foundType.getName());
        assertEquals(savedType.getDescription(), foundType.getDescription());
        assertEquals(savedType.getFormat(), foundType.getFormat());
    }

    @DisplayName("Test get all person attribute types")
    @Test
     void testGetAllPersonAttributeTypes() {
        // given
        PersonAttributeTypeDto type1 = attributeTypeDtoBuilder.build();
        PersonAttributeTypeDto type2 = attributeTypeDtoBuilder
                .name("Weight")
                .description("Person's weight")
                .build();

        personAttributeTypeService.savePersonAttributeType(type1);
        personAttributeTypeService.savePersonAttributeType(type2);

        // when
        List<PersonAttributeTypeDto> types = personAttributeTypeService.getAllPersonAttributeTypes();

        // then
        assertEquals(2, types.size());
        assertTrue(types.stream().anyMatch(type -> type.getName().equals("Blood Type")));
        assertTrue(types.stream().anyMatch(type -> type.getName().equals("Weight")));
    }

    @DisplayName("Test delete person attribute type")
    @Test
     void testDeletePersonAttributeType() {
        // given
        PersonAttributeTypeDto typeDto = attributeTypeDtoBuilder.build();

        // when
        PersonAttributeTypeDto savedType = personAttributeTypeService.savePersonAttributeType(typeDto);
        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();
        personAttributeTypeService.deletePersonAttributeType(savedType.getPersonAttributeTypeId(), voidRequest);
        PersonAttributeTypeDto voidedType = personAttributeTypeService.getPersonAttributeTypeById(
                savedType.getPersonAttributeTypeId());

        // then
        assertTrue(voidedType.getVoided());
        assertEquals(1L, voidedType.getVoidedBy());
        assertEquals("Test void reason", voidedType.getVoidReason());
        assertNotNull(voidedType.getVoidedAt());

        // Verify that voided types are not returned in the list
        List<PersonAttributeTypeDto> activeTypes = personAttributeTypeService.getAllPersonAttributeTypes();
        assertTrue(activeTypes.stream()
                .noneMatch(type -> type.getPersonAttributeTypeId().equals(savedType.getPersonAttributeTypeId())));
    }
} 