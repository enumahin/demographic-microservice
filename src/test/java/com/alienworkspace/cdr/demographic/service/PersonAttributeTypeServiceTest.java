package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.demographic.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.demographic.model.PersonAttributeType;
import com.alienworkspace.cdr.demographic.model.mapper.PersonAttributeTypeMapper;
import com.alienworkspace.cdr.demographic.repository.PersonAttributeTypeRepository;
import com.alienworkspace.cdr.demographic.service.impl.PersonAttributeTypeServiceImpl;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeTypeDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PersonAttributeTypeServiceTest {

    private PersonAttributeTypeRepository personAttributeTypeRepository;
    private PersonAttributeTypeMapper personAttributeTypeMapper;
    private PersonAttributeTypeServiceImpl personAttributeTypeService;
    private PersonAttributeTypeDto.PersonAttributeTypeDtoBuilder attributeTypeDtoBuilder;
    private PersonAttributeType.PersonAttributeTypeBuilder attributeTypeBuilder;
    private PersonAttributeTypeDto attributeTypeDto;
    private PersonAttributeType savedAttributeType;

    @BeforeEach
     void setup() {
        personAttributeTypeRepository = mock(PersonAttributeTypeRepository.class);
        personAttributeTypeMapper = mock(PersonAttributeTypeMapper.class);
        personAttributeTypeService = new PersonAttributeTypeServiceImpl(personAttributeTypeRepository, personAttributeTypeMapper);

        attributeTypeDtoBuilder = PersonAttributeTypeDto.builder()
                .name("Test Type")
                .description("Test Description")
                .format("java.lang.String");

        attributeTypeBuilder = PersonAttributeType.builder()
                .name("Test Type")
                .description("Test Description")
                .format("java.lang.String");

        attributeTypeDto = attributeTypeDtoBuilder.personAttributeTypeId(1).build();
        savedAttributeType = attributeTypeBuilder.personAttributeTypeId(1).build();
    }

    @Test
    @DisplayName("Test save person attribute type")
     void testSavePersonAttributeType() {
        // given
        when(personAttributeTypeMapper.toEntity(attributeTypeDtoBuilder.build())).thenReturn(attributeTypeBuilder.build());
        when(personAttributeTypeRepository.save(any(PersonAttributeType.class))).thenReturn(savedAttributeType);
        when(personAttributeTypeMapper.toDto(savedAttributeType)).thenReturn(attributeTypeDto);

        // when
        PersonAttributeTypeDto response = personAttributeTypeService.savePersonAttributeType(attributeTypeDtoBuilder.build());

        // then
        assertEquals(attributeTypeDto.getPersonAttributeTypeId(), response.getPersonAttributeTypeId());
        assertEquals(attributeTypeDto.getName(), response.getName());
        assertEquals(attributeTypeDto.getDescription(), response.getDescription());
        assertEquals(attributeTypeDto.getFormat(), response.getFormat());
    }

    @Test
    @DisplayName("Test update person attribute type")
     void testUpdatePersonAttributeType() {
        // given
        PersonAttributeTypeDto updateDto = attributeTypeDtoBuilder
                .name("Updated Name")
                .description("Updated Description")
                .format("java.lang.Integer")
                .build();

        PersonAttributeType updatedType = attributeTypeBuilder
                .name("Updated Name")
                .description("Updated Description")
                .format("java.lang.Integer")
                .build();
        updatedType.setLastModifiedAt(LocalDateTime.now());
        updatedType.setLastModifiedBy(1L);

        PersonAttributeTypeDto updatedTypeDto = attributeTypeDtoBuilder
                .name("Updated Name")
                .description("Updated Description")
                .format("java.lang.Integer")
                .build();

        when(personAttributeTypeRepository.findById(1)).thenReturn(Optional.of(savedAttributeType));
        when(personAttributeTypeRepository.save(any(PersonAttributeType.class))).thenReturn(updatedType);
        when(personAttributeTypeMapper.toDto(updatedType)).thenReturn(updatedTypeDto);

        // when
        PersonAttributeTypeDto response = personAttributeTypeService.updatePersonAttributeType(1, updateDto);

        // then
        assertEquals(updatedTypeDto.getName(), response.getName());
        assertEquals(updatedTypeDto.getDescription(), response.getDescription());
        assertEquals(updatedTypeDto.getFormat(), response.getFormat());
    }

    @Test
    @DisplayName("Test update non-existing person attribute type")
     void testUpdateNonExistingPersonAttributeType() {
        // given
        when(personAttributeTypeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // when & then
        PersonAttributeTypeDto attributeType = attributeTypeDtoBuilder.build();
        assertThrows(ResourceNotFoundException.class, () -> 
            personAttributeTypeService.updatePersonAttributeType(1, attributeType));
        verify(personAttributeTypeRepository, never()).save(any(PersonAttributeType.class));
    }

    @Test
    @DisplayName("Test get person attribute type by ID")
     void testGetPersonAttributeTypeById() {
        // given
        when(personAttributeTypeRepository.findById(1)).thenReturn(Optional.of(savedAttributeType));
        when(personAttributeTypeMapper.toDto(savedAttributeType)).thenReturn(attributeTypeDto);

        // when
        PersonAttributeTypeDto response = personAttributeTypeService.getPersonAttributeTypeById(1);

        // then
        assertEquals(attributeTypeDto.getPersonAttributeTypeId(), response.getPersonAttributeTypeId());
        assertEquals(attributeTypeDto.getName(), response.getName());
        assertEquals(attributeTypeDto.getDescription(), response.getDescription());
        assertEquals(attributeTypeDto.getFormat(), response.getFormat());
    }

    @Test
    @DisplayName("Test get non-existing person attribute type")
     void testGetNonExistingPersonAttributeType() {
        // given
        when(personAttributeTypeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> 
            personAttributeTypeService.getPersonAttributeTypeById(1));
    }

    @Test
    @DisplayName("Test get all person attribute types")
     void testGetAllPersonAttributeTypes() {
        // given
        when(personAttributeTypeRepository.findAll()).thenReturn(List.of(savedAttributeType));
        when(personAttributeTypeMapper.toDto(savedAttributeType)).thenReturn(attributeTypeDto);

        // when
        List<PersonAttributeTypeDto> response = personAttributeTypeService.getAllPersonAttributeTypes();

        // then
        assertEquals(1, response.size());
        assertEquals(attributeTypeDto, response.get(0));
    }

    @Test
    @DisplayName("Test delete person attribute type")
     void testDeletePersonAttributeType() {
        // given
        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test reason")
                .build();

        PersonAttributeType voidedType = attributeTypeBuilder
                .build();
        voidedType.setVoided(true);
        voidedType.setVoidedBy(1L);
        voidedType.setVoidReason("Test reason");
        voidedType.setVoidedAt(LocalDateTime.now());

        when(personAttributeTypeRepository.findById(1)).thenReturn(Optional.of(savedAttributeType));
        when(personAttributeTypeRepository.save(any(PersonAttributeType.class))).thenReturn(voidedType);

        // when
        personAttributeTypeService.deletePersonAttributeType(1, voidRequest);

        // then
        verify(personAttributeTypeRepository).save(any(PersonAttributeType.class));
    }

    @Test
    @DisplayName("Test delete non-existing person attribute type")
     void testDeleteNonExistingPersonAttributeType() {
        // given
        RecordVoidRequest voidRequest = RecordVoidRequest.builder()
                .voidReason("Test reason")
                .build();
        when(personAttributeTypeRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // when & then
        assertThrows(ResourceNotFoundException.class, () -> 
            personAttributeTypeService.deletePersonAttributeType(1, voidRequest));
        verify(personAttributeTypeRepository, never()).save(any(PersonAttributeType.class));
    }
} 