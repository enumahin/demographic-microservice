package com.alienworkspace.cdr.demographic.repository;

import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.model.Person;
import com.alienworkspace.cdr.demographic.model.PersonName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonNameRepositoryIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private PersonNameRepository personNameRepository;

    @BeforeEach
    public void setup() {
        personNameRepository.deleteAll();
    }

    @Test
    public void testSave() {
        // given
         PersonName personName =  PersonName.builder()
                .firstName("John")
                 .middleName("A")
                .lastName("Doe")
                 .otherName("Smith")
                 .preferred(true)
                .build();
        // when
         PersonName saved =   personNameRepository.save(personName);

        // then
        assertTrue(saved.getPersonNameId() > 0L);
    }

    @Test
    public void testDelete() {
        // given
        PersonName personName = PersonName.builder()
                .firstName("John")
                .middleName("A")
                .lastName("Doe")
                .otherName("Smith")
                .build();

        // when
        PersonName saved =  personNameRepository.save(personName);
        personNameRepository.delete(saved);

        // then
        assertTrue( personNameRepository.findById(saved.getPersonNameId()).isEmpty());
    }

    @Test
    public void testFindByPerson() {
        // given
        PersonName personName = PersonName.builder()
                .personId(1L)
                .firstName("John")
                .middleName("A")
                .lastName("Doe")
                .build();
        PersonName personName2 = personName.builder()
                .personId(1L)
                .firstName("John")
                .middleName("Will")
                .lastName("Smith")
                .build();

        // when
         personNameRepository.save(personName);
         personNameRepository.save(personName2);
        List<PersonName> all =  personNameRepository.findAllByPersonId(personName.getPersonId());

        // then
        assertTrue(all.size() == 2);
    }

    @DisplayName("Test findByPersonNameId")
    @Test
    public void testFindByPersonNameId() {
        // given
        PersonName personName = PersonName.builder()
                .firstName("John")
                .middleName("A")
                .lastName("Doe")
                .otherName("Smith")
                .build();

        // when
        personNameRepository.deleteAll();
        PersonName saved =  personNameRepository.save(personName);
        Optional<PersonName> optionalPersonName =  personNameRepository.findByPersonNameId(saved.getPersonNameId());
        // then
        assertTrue(optionalPersonName.isPresent());
    }

    @DisplayName("Test findByPreferredAndPersonId")
    @Test
    public void testFindByPreferredAndPersonId() {
        // given
        PersonName personName = PersonName.builder()
                .firstName("John")
                .middleName("A")
                .lastName("Doe")
                .otherName("Smith")
                .preferred(true)
                .build();
        PersonName personName2 = PersonName.builder()
                .firstName("John")
                .middleName("A")
                .lastName("Doe Jr.")
                .otherName("Smith")
                .preferred(false)
                .build();

        // when
        personNameRepository.save(personName);
        PersonName saved = personNameRepository.save(personName2);
        Optional<PersonName> optionalPersonName =  personNameRepository.findByPersonIdAndPreferred(saved.getPersonId(), true);
        // then
        assertTrue(optionalPersonName.isPresent());
        assertEquals(personName.getLastName(), optionalPersonName.get().getLastName());
    }
}
