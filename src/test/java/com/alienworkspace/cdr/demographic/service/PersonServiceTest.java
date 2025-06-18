package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.demographic.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.demographic.model.Person;
import com.alienworkspace.cdr.demographic.model.mapper.PersonAddressMapper;
import com.alienworkspace.cdr.demographic.model.mapper.PersonAttributeMapper;
import com.alienworkspace.cdr.demographic.model.mapper.PersonMapper;
import com.alienworkspace.cdr.demographic.model.mapper.PersonNameMapper;
import com.alienworkspace.cdr.demographic.repository.PersonAttributeTypeRepository;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.demographic.service.impl.PersonServiceImpl;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    private PersonRepository personRepository;

    private PersonAttributeTypeRepository  personAttributeTypeRepository;

    private PersonMapper personMapper;

    private PersonNameMapper personNameMapper;

    private PersonAddressMapper personAddressMapper;

    private PersonAttributeMapper personAttributeMapper;

    private PersonServiceImpl personService;

    private PersonDto.PersonDtoBuilder personDtoBuilder;

    private Person.PersonBuilder personBuilder;

    private PersonDto personDto;

    private Person savedPerson;

    @BeforeEach
    public void setup() {
        personRepository = mock(PersonRepository.class);
        personAttributeTypeRepository = mock(PersonAttributeTypeRepository.class);
        personMapper = mock(PersonMapper.class);
        personNameMapper = mock(PersonNameMapper.class);
        personAddressMapper = mock(PersonAddressMapper.class);
        personAttributeMapper = mock(PersonAttributeMapper.class);
        personService = new PersonServiceImpl(personRepository, personAttributeTypeRepository, personMapper,
                personNameMapper, personAddressMapper, personAttributeMapper);

        personDtoBuilder = PersonDto.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .dead(false)
                .deathDate(null)
                .causeOfDeath(null);

        personBuilder = Person.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .dead(false)
                .deathDate(null)
                .causeOfDeath(null);

        personDto = personDtoBuilder.personId(1L).build();

        savedPerson = personBuilder
                .personId(1L)
                .build();
    }

    @Test
    public void testAddPerson() {
        // given
        when(personMapper.personDtoToPerson(personDtoBuilder.build())).thenReturn(personBuilder.build());
        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);
        when(personMapper.personToPersonDto(savedPerson)).thenReturn(personDto);

        // when
        PersonDto response = personService.addPerson(personDtoBuilder.build());

        // then
        assertEquals(personDto.getPersonId(), response.getPersonId());
        assertEquals(personDto.getGender(), response.getGender());
        assertEquals(personDto.getBirthDate(), response.getBirthDate());
        assertEquals(personDto.getDead(), response.getDead());
        assertEquals(personDto.getDeathDate(), response.getDeathDate());
        assertEquals(personDto.getCauseOfDeath(), response.getCauseOfDeath());
    }

    @DisplayName("Test add person with null birthdate")
    @Test
    public void testAddPersonWithNullBirthdate() {
        // given
        PersonDto personDto = personDtoBuilder.birthDate(null).build();
        when(personMapper.personDtoToPerson(personDto)).thenReturn(personBuilder.birthDate(null).build());
        when(personRepository.save(any(Person.class))).thenThrow(NullPointerException.class);

        // when
        assertThrows(RuntimeException.class, () -> personService.addPerson(personDto));

        // then - throw exception
    }

    @Test
    public void testGetPerson() {
        // given
        when(personRepository.findCompleteById(any(Long.class))).thenReturn(Optional.of(savedPerson));
        when(personMapper.personToPersonDto(savedPerson)).thenReturn(personDto);

        // when
        PersonDto response = personService.getPerson(1L);

        // then
        assertEquals(personDto.getPersonId(), response.getPersonId());
        assertEquals(personDto.getGender(), response.getGender());
        assertEquals(personDto.getBirthDate(), response.getBirthDate());
        assertEquals(personDto.getDead(), response.getDead());
        assertEquals(personDto.getDeathDate(), response.getDeathDate());
        assertEquals(personDto.getCauseOfDeath(), response.getCauseOfDeath());
    }

    @DisplayName("Test get non existing person.")
    @Test
    public void testGetNonExistingPerson() {
        // given
        when(personRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when
        assertThrows(ResourceNotFoundException.class, () -> personService.getPerson(1L));

        // then - throw exception
    }

    @Test
    public void testGetPersons() {
        // given
        when(personRepository.findCompleteAll()).thenReturn(List.of(savedPerson));
        when(personMapper.personToPersonDto(savedPerson)).thenReturn(personDto);

        // when
        List<PersonDto> response = personService.getPersons();

        // then
        assertEquals(List.of(personDto), response);
    }

    @Test
    public void testUpdatePerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .personId(1L)
                .gender('F')
                .build();
        Person updatedPerson = personBuilder
                .personId(1L)
                .gender('F')
                .build();
        PersonDto updatedPersonDto = personDtoBuilder
                .personId(1L)
                .gender('F')
                .build();
        when(personRepository.findByPersonId(any(Long.class))).thenReturn(Optional.of(savedPerson));
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);
        when(personMapper.personToPersonDto(any(Person.class))).thenReturn(updatedPersonDto);

        // when
        PersonDto response = personService.updatePerson(personDto.getPersonId(), personDto);

        // then
        assertEquals(updatedPersonDto, response);
        assertEquals(updatedPersonDto.getPersonId(), response.getPersonId());
        assertEquals(updatedPersonDto.getGender(), response.getGender());
        assertEquals(updatedPersonDto.getBirthDate(), response.getBirthDate());
        assertEquals(updatedPersonDto.getDead(), response.getDead());
        assertEquals(updatedPersonDto.getDeathDate(), response.getDeathDate());
        assertEquals(updatedPersonDto.getCauseOfDeath(), response.getCauseOfDeath());
    }

    @DisplayName("Test update non existing person.")
    @Test
    public void testUpdateNonExistingPerson() {
        // given
        when(personRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when
        assertThrows(ResourceNotFoundException.class, () -> personService.updatePerson(1L, personDtoBuilder.personId(1L)
                .build()));

        // then - throw exception
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    public void testDeletePerson() {
        // given
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test reason")
                .build();

        Person updatedPerson = personBuilder
                .personId(1L)
                .build();

        updatedPerson.setVoided(true);
        updatedPerson.setVoidReason("test");
        updatedPerson.setVoidedAt(LocalDateTime.now());
        updatedPerson.setVoidedBy(1L);

        when(personRepository.findById(any(Long.class))).thenReturn(Optional.of(savedPerson));
        when(personRepository.save(updatedPerson)).thenReturn(updatedPerson);

        // when
        ResponseDto response = personService.deletePerson(1, recordVoidRequest);

        // then
        assertEquals("Person deleted successfully", response.getStatusMessage());
    }

    @DisplayName("Test delete non existing person.")
    @Test
    void testDeleteNonExistingPerson() {
        // given
        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("Test reason")
                .build();
        when(personRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when
        assertThrows(ResourceNotFoundException.class, () -> personService.deletePerson(1, recordVoidRequest));

        // then - throw exception
        verify(personRepository, never()).save(any(Person.class));
    }
}
