package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.demographic.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.demographic.model.PersonName;
import com.alienworkspace.cdr.demographic.model.mapper.PersonNameMapper;
import com.alienworkspace.cdr.demographic.repository.PersonNameRepository;
import com.alienworkspace.cdr.demographic.service.impl.PersonNameServiceImpl;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class PersonNameServiceTest {

    public PersonNameRepository personNameRepository;

    public PersonNameMapper personNameMapper;

    public PersonNameServiceImpl personNameService;

    private PersonName.PersonNameBuilder personNameBuilder;

    private PersonNameDto.PersonNameDtoBuilder personNameDtoBuilder;

    PersonNameDto mappedPersonNameDto;

    PersonName mappedPersonName;

    @BeforeEach
    public void setup() {
        personNameRepository = mock(PersonNameRepository.class);
        personNameMapper = mock(PersonNameMapper.class);
        personNameService = new PersonNameServiceImpl(personNameRepository, personNameMapper);

        personNameBuilder = PersonName.builder()
                .personNameId(1L)
                .personId(1L)
                .firstName("John")
                .lastName("Doe")
                .preferred(true);

        personNameDtoBuilder = PersonNameDto.builder()
                .personId(1L)
                .firstName("John")
                .lastName("Doe")
                .preferred(true);

        mappedPersonNameDto = personNameDtoBuilder
                .personNameId(1L)
                .personId(1L)
                .build();
        mappedPersonName = personNameBuilder
                .personId(1L)
                .build();
    }

    @DisplayName("Test save person name")
    @Test
    public void testSavePersonName() {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder.build();

        PersonName expected = personNameBuilder
                .personNameId(1L)
                .personId(1L)
                .build();
        expected.setLastModifiedBy(1L);
        expected.setLastModifiedAt(LocalDateTime.now());
        given(personNameMapper.personNameDtoToPersonName(personNameDto)).willReturn(mappedPersonName);
        given(personNameRepository.save(any(PersonName.class))).willReturn(expected);
        given(personNameMapper.personNameToPersonNameDto(any(PersonName.class))).willReturn(mappedPersonNameDto);

        // when
        PersonNameDto savedPersonName = personNameService.addPersonName(personNameDto);

        // then
        assertNotNull(savedPersonName);
        assertEquals(1L, savedPersonName.getPersonId());
        assertEquals(1L, savedPersonName.getPersonNameId());
        assertEquals(personNameDto.getFirstName(), savedPersonName.getFirstName());
        assertEquals(personNameDto.getLastName(), savedPersonName.getLastName());
        assertEquals(personNameDto.getPreferred(), savedPersonName.getPreferred());
    }

    @DisplayName("Test get person name")
    @Test
    public void testGetPersonName() {
        // given
        PersonName savedPersonName = personNameBuilder
                .lastName("Smith")
                .preferred(true)
                .personNameId(1L)
                .personId(1L)
                .build();

        savedPersonName.setCreatedAt(LocalDateTime.now());
        savedPersonName.setCreatedBy(1L);
        given(personNameRepository.findByPersonNameId(any(Long.class)))
                .willReturn(Optional.of(savedPersonName));
        PersonNameDto expectedPersonNameDto = personNameDtoBuilder.lastName("Smith").
                preferred(true)
                .personNameId(1L)
                .personId(1L)
                .build();
        expectedPersonNameDto.setCreatedAt(LocalDateTime.now());
        expectedPersonNameDto.setCreatedBy(1L);
        given(personNameMapper.personNameToPersonNameDto(any(PersonName.class))).willReturn(expectedPersonNameDto);

        // when
        PersonNameDto actualPersonName = personNameService.findPersonName(1L);

        // then
        assertNotNull(actualPersonName);
        assertEquals(savedPersonName.getPersonId(),actualPersonName.getPersonId());
        assertEquals(savedPersonName.getPersonNameId(),actualPersonName.getPersonNameId());
        assertEquals(savedPersonName.getFirstName(), actualPersonName.getFirstName());
        assertEquals(savedPersonName.getLastName(), actualPersonName.getLastName());
        assertEquals(savedPersonName.isPreferred(), actualPersonName.getPreferred());
        assertEquals(savedPersonName.getCreatedBy(), actualPersonName.getCreatedBy());
        assertNull(actualPersonName.getLastModifiedBy());
        verify(personNameRepository).findByPersonNameId(eq(savedPersonName.getPersonNameId()));
        verifyNoMoreInteractions(personNameRepository);
    }


    @DisplayName("Test get non existing person name")
    @Test
    public void testGetNonExistingPersonName() {
        // given

        given(personNameRepository.findByPersonNameId(any(Long.class)))
                .willReturn(Optional.empty());

        // when
        assertThrows(ResourceNotFoundException.class, () -> personNameService.findPersonName(1L));

        // then
        verify(personNameRepository).findByPersonNameId(eq(1L));
        verifyNoMoreInteractions(personNameRepository);
    }


    @DisplayName("Test get preferred person name")
    @Test
    public void testGetPreferredPersonNameByPersonId() {
        // given
        PersonName savedPersonName = personNameBuilder
                .lastName("Smith")
                .preferred(true)
                .personNameId(1L)
                .personId(1L)
                .build();

        savedPersonName.setCreatedAt(LocalDateTime.now());
        savedPersonName.setCreatedBy(1L);
        given(personNameRepository.findByPersonIdAndPreferred(eq(1L), eq(true)))
                .willReturn(Optional.of(savedPersonName));
        PersonNameDto expectedPersonNameDto = personNameDtoBuilder.lastName("Smith").
                preferred(true)
                .personNameId(1L)
                .personId(1L)
                .build();
        expectedPersonNameDto.setCreatedAt(LocalDateTime.now());
        expectedPersonNameDto.setCreatedBy(1L);
        given(personNameMapper.personNameToPersonNameDto(any(PersonName.class))).willReturn(expectedPersonNameDto);

        // when
        Optional<PersonNameDto> optionalPersonNameDto = personNameService.findPreferredNameByPersonId(1L);

        PersonNameDto actualPersonName = optionalPersonNameDto.get();
        // then
        assertNotNull(actualPersonName);
        assertEquals(savedPersonName.getPersonId(),actualPersonName.getPersonId());
        assertEquals(savedPersonName.getPersonNameId(),actualPersonName.getPersonNameId());
        assertEquals(savedPersonName.getFirstName(), actualPersonName.getFirstName());
        assertEquals(savedPersonName.getLastName(), actualPersonName.getLastName());
        assertEquals(savedPersonName.isPreferred(), actualPersonName.getPreferred());
        assertEquals(savedPersonName.getCreatedBy(), actualPersonName.getCreatedBy());
        assertNull(actualPersonName.getLastModifiedBy());
        verify(personNameRepository).findByPersonIdAndPreferred(eq(1L), eq(true));
        verifyNoMoreInteractions(personNameRepository);
    }

    @DisplayName("Test get non existing person name")
    @Test
    public void testGetNonExistingPreferredPersonName() {
        // given

        given(personNameRepository.findByPersonIdAndPreferred(eq(1L), eq(true)))
                .willReturn(Optional.empty());

        // when
        Optional<PersonNameDto> personNameDto = personNameService.findPreferredNameByPersonId(1L);

        // then
        assertFalse(personNameDto.isPresent());
        verify(personNameRepository).findByPersonIdAndPreferred(1L, true);
        verifyNoMoreInteractions(personNameRepository);
    }


    @DisplayName("Test get person names by person id")
    @Test
    public void testGetPersonNamesByPersonId() {
        // given
        PersonName savedPersonName = personNameBuilder
                .lastName("Smith")
                .preferred(true)
                .personNameId(1L)
                .personId(1L)
                .build();

        savedPersonName.setCreatedAt(LocalDateTime.now());
        savedPersonName.setCreatedBy(1L);
        given(personNameRepository.findAllByPersonIdAndVoided(any(Long.class), any(Boolean.class)))
                .willReturn(List.of(savedPersonName));

        // when
        List<PersonNameDto> personNameDtos = personNameService.findPersonNamesByPersonId(1L);

        // then
        assertEquals(1, personNameDtos.size());
        verify(personNameRepository).findAllByPersonIdAndVoided(eq(1L), eq(false));
        verifyNoMoreInteractions(personNameRepository);
    }

    @DisplayName("Test get person names by person id")
    @Test
    public void testGetPersonNamesByPersonIdAndVoided() {
        // given
        PersonName savedPersonName = personNameBuilder
                .lastName("Smith")
                .preferred(true)
                .personNameId(1L)
                .personId(1L)
                .build();

        savedPersonName.setCreatedAt(LocalDateTime.now());
        savedPersonName.setCreatedBy(1L);

        given(personNameRepository.findAllByPersonIdAndVoided(any(Long.class), any(Boolean.class)))
                .willReturn(List.of(savedPersonName));

        // when
        List<PersonNameDto> personNameDtos = personNameService.findPersonNamesByPersonId(  1L);

        // then
        assertEquals(1, personNameDtos.size());
        verify(personNameRepository).findAllByPersonIdAndVoided(eq(1L), eq(false));
        verifyNoMoreInteractions(personNameRepository);
    }

    @DisplayName("Test delete person name")
    @Test
    public void testDeletePersonName() {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder
                .personNameId(1L)
                .preferred(false)
                .personId(1L).build();

        personNameDto.setVoided(true);
        personNameDto.setVoidReason("test");
        personNameDto.setVoidedBy(1L);
        personNameDto.setVoidedAt(LocalDateTime.now());

        given(personNameRepository.findByPersonNameId(any(Long.class)))
                .willReturn(Optional.of(personNameBuilder.preferred(false).personNameId(1L).personId(1L).build()));

        PersonName deletedPersonName = personNameBuilder
                .lastName("Smith")
                .preferred(false)
                .personNameId(1L)
                .personId(1L)
                .build();
        deletedPersonName.setVoided(true);
        deletedPersonName.setVoidReason("test");
        deletedPersonName.setVoidedBy(1L);
        deletedPersonName.setVoidedAt(LocalDateTime.now());

        given(personNameRepository.save(any(PersonName.class)))
                .willReturn(deletedPersonName);

        given(personNameRepository.findByPersonNameId(any(Long.class)))
                .willReturn(Optional.of(deletedPersonName));
        given(personNameMapper.personNameToPersonNameDto(any(PersonName.class))).willReturn(personNameDto);
        RecordVoidRequest recordVoidRequest = new RecordVoidRequest();
        recordVoidRequest.setVoidReason("test");

        // when
        String response = personNameService.deletePersonName(1L, recordVoidRequest);
        String expectedResponse = "Person name deleted successfully";
        PersonNameDto voidedPerson = personNameService.findPersonName(1L);

        // then
        assertNotNull(voidedPerson);
        assertEquals(expectedResponse, response);
        assertEquals(1L,voidedPerson.getPersonId());
        assertEquals(1L,voidedPerson.getPersonNameId());
        assertEquals(personNameDto.getFirstName(), voidedPerson.getFirstName());
        assertEquals(personNameDto.getLastName(), voidedPerson.getLastName());
        assertEquals(personNameDto.getPreferred(), voidedPerson.getPreferred());
        assertEquals(1L, voidedPerson.getVoidedBy());
        assertNotNull(voidedPerson.getVoidedAt());
        verify(personNameRepository, times(2)).findByPersonNameId(eq(personNameDto.getPersonNameId()));
        verify(personNameRepository).save(any(PersonName.class));
    }


    @DisplayName("Test delete non existing person name")
    @Test
    public void testDeleteNonExistingPersonName() {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder.personNameId(1L).personId(1L).build();

        given(personNameRepository.findByPersonNameId(any(Long.class)))
                .willReturn(Optional.empty());

        // when
        RecordVoidRequest recordVoidRequest = new RecordVoidRequest();
        recordVoidRequest.setVoidReason("test");
        assertThrows(ResourceNotFoundException.class, () ->
                personNameService.deletePersonName(1L, recordVoidRequest));

        // then
        verify(personNameRepository).findByPersonNameId(eq(personNameDto.getPersonNameId()));
        verify(personNameRepository, never()).save(any(PersonName.class));
    }

    @DisplayName("Test preferred person name cannot be deleted")
    @Test
    public void testCanNotDeletePreferredPersonName() {
        // given
        PersonName personName = personNameBuilder
                .personNameId(1L)
                .preferred(true)
                .personId(1L).build();

        given(personNameRepository.findByPersonNameId(any(Long.class)))
                .willReturn(Optional.of(personName));

        RecordVoidRequest recordVoidRequest = new RecordVoidRequest();
        recordVoidRequest.setVoidReason("test");

        // when
        assertThrows(RuntimeException.class, () -> personNameService.deletePersonName(1L,recordVoidRequest));

        // then
        verify(personNameRepository).findByPersonNameId(eq(1L));
        verify(personNameRepository, never()).save(any(PersonName.class));
    }
}
