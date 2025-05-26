package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.model.mapper.PersonNameMapper;
import com.alienworkspace.cdr.demographic.repository.PersonNameRepository;
import com.alienworkspace.cdr.demographic.service.impl.PersonNameServiceImpl;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonNameServiceIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private PersonNameRepository personNameRepository;

    @Autowired
    private PersonNameService personNameService;

    private PersonNameDto.PersonNameDtoBuilder personNameDtoBuilder;

    @Autowired
    private PersonNameMapper personNameMapper;

    @BeforeEach
    public void setup() {
        personNameRepository.deleteAll();
        personNameService = new PersonNameServiceImpl(personNameRepository, personNameMapper);
        personNameDtoBuilder = PersonNameDto.builder()
                .personId(1L)
                .firstName("John")
                .middleName("Umar")
                .lastName("Doe");
    }

    @DisplayName("Test add a new person name")
    @Test
    public void testAddPersonName() {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder.build();

        // when
        // Add the first person name and expect it to be preferred even when preferred in not set
        PersonNameDto savedPerson = personNameService.addPersonName(personNameDto);
        // add second person name with preferred not set and expect preferred not to change
        PersonNameDto savedPerson2 = personNameService.addPersonName(personNameDtoBuilder.firstName("Queen")
                .lastName("Lizzy").build());
        // Add thirst person name a preferred
        PersonNameDto savedPerson3 = personNameService.addPersonName(personNameDtoBuilder.preferred(true).build());
        // Get preferred and expect it to be the last added name
        Optional<PersonNameDto> preferred = personNameService.findPreferredNameByPersonId(savedPerson3.getPersonId());

        // then
        assertNotNull(savedPerson);
        assertNotNull(savedPerson.getPersonNameId());
        assertNotNull(savedPerson.getCreatedBy());
        assertNotNull(savedPerson.getCreatedAt());
        assertTrue(savedPerson.getPreferred());
        assertNotNull(savedPerson2);
        assertNotNull(savedPerson2.getPersonNameId());
        assertNotNull(savedPerson2.getCreatedBy());
        assertNotNull(savedPerson2.getCreatedAt());
        assertFalse(savedPerson2.getPreferred());
        assertTrue(preferred.isPresent());
        assertEquals(savedPerson3.getPersonNameId(), preferred.get().getPersonNameId());
    }

    @DisplayName("Test add a new person name")
    @Test
    public void testGetPersonName() {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder.build();

        // when
        PersonNameDto savedPerson = personNameService.addPersonName(personNameDto);

        // then
        assertNotNull(savedPerson);
        assertNotNull(savedPerson.getPersonNameId());
        assertNotNull(savedPerson.getCreatedBy());
        assertNotNull(savedPerson.getCreatedAt());
    }

    @DisplayName("Test get person names by person ID")
    @Test
    public void testGetPersonNamesByPersonId() {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder.build();

        // when
        personNameService.addPersonName(personNameDto);
        PersonNameDto savedPerson2 = personNameService.addPersonName(personNameDto);

        List<PersonNameDto> personNames = personNameService.findPersonNamesByPersonId(savedPerson2.getPersonId());

        // then
        assertEquals(2, personNames.size());
    }

    @DisplayName("Test delete a person name")
    @Test
    public void testDeletePersonName() {
        // given
        PersonNameDto personNameDto = personNameDtoBuilder.build();

        // when
        PersonNameDto savedPerson = personNameService.addPersonName(personNameDto);
        PersonNameDto savedPerson2 = personNameService.addPersonName(personNameDto);

        RecordVoidRequest recordVoidRequest = RecordVoidRequest.builder()
                .voidReason("test")
                .build();

        personNameService.deletePersonName(savedPerson.getPersonNameId(), recordVoidRequest);

        List<PersonNameDto> voided = personNameService.findPersonNamesByPersonId(savedPerson.getPersonId(), true);
        List<PersonNameDto> notVoided = personNameService.findPersonNamesByPersonId(savedPerson.getPersonId());

        List<PersonNameDto> personNames = personNameService.findPersonNamesByPersonIdBothVoided(
                savedPerson2.getPersonId());

        // then
        assertEquals(2, personNames.size());
        assertEquals(1, voided.size());
        assertEquals(1, notVoided.size());
        assertEquals(savedPerson.getPersonNameId(), voided.get(0).getPersonNameId());
        assertEquals(savedPerson2.getPersonNameId(), notVoided.get(0).getPersonNameId());
        assertTrue(voided.get(0).getVoided());
        assertNotNull(voided.get(0).getVoidedAt());
        assertNotNull(voided.get(0).getVoidedBy());
    }
}
