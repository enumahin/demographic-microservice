package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonServiceIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    @BeforeEach
    public void setup() {
        personRepository.deleteAll();
    }

    @DisplayName("Test add person")
    @Test
    public void testAddPerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();

        // when
        PersonDto response = personService.addPerson(personDto);

        // then
        assertEquals(personDto.getGender(), response.getGender());
        assertEquals(personDto.getBirthDate(), response.getBirthDate());
        assertTrue(response.getPersonId() > 0);
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getCreatedBy());
    }

    @DisplayName("Test update person")
    @Test
    public void testUpdatePerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();


        // when
        PersonDto savedPerson = personService.addPerson(personDto);
        PersonDto update = PersonDto.builder()
                .personId(savedPerson.getPersonId())
                .gender('M')
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();
        PersonDto updatedPerson = personService.updatePerson(update.getPersonId(), update);

        // then
        assertNotEquals(savedPerson.getGender(), updatedPerson.getGender());
        assertTrue(savedPerson.getBirthDate().isBefore(updatedPerson.getBirthDate()));
        assertEquals(savedPerson.getPersonId(), updatedPerson.getPersonId());
        assertEquals(savedPerson.getCreatedAt(), updatedPerson.getCreatedAt());
        assertNotNull(updatedPerson.getLastModifiedAt());
        assertNotNull(updatedPerson.getLastModifiedBy());
    }

    @DisplayName("Test get person")
    @Test
    public void testGetPerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();

        // when
        PersonDto saved = personService.addPerson(personDto);
        PersonDto response = personService.getPerson(saved.getPersonId());

        // then
        assertEquals(saved.getGender(), response.getGender());
        assertEquals(saved.getBirthDate(), response.getBirthDate());
        assertEquals(saved.getPersonId(), response.getPersonId());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getCreatedBy());
    }

    @DisplayName("Test get all persons")
    @Test
    public void testGetAllPerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
        PersonDto personDto2 = PersonDto.builder()
                .gender('M')
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();

        // when
        personService.addPerson(personDto);
        personService.addPerson(personDto2);
        List<PersonDto> response = personService.getPersons();

        // then
        assertTrue(response.size() == 2);
    }

    @DisplayName("Test delete person")
    @Test
    public void testDeletePerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .gender('F')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();


        // when
        PersonDto savedPerson = personService.addPerson(personDto);
        RecordVoidRequest request = RecordVoidRequest.builder()
                .voidReason("Test void reason")
                .build();

        ResponseDto response = personService.deletePerson(savedPerson.getPersonId(), request);
        PersonDto deletedPerson = personService.getPerson(savedPerson.getPersonId());

        // then
        assertEquals("Person deleted successfully", response.getStatusMessage());
        assertNotNull(deletedPerson);
        assertTrue(deletedPerson.getVoided());
        assertNotNull(deletedPerson.getVoidedAt());
        assertNotNull(deletedPerson.getVoidedBy());
    }
}
