package com.alienworkspace.cdr.demographic.repository;

import com.alienworkspace.cdr.demographic.integration.AbstractionContainerBaseTest;
import com.alienworkspace.cdr.demographic.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PersonRepositoryIntegrationTest extends AbstractionContainerBaseTest {

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    public void setup() {
        personRepository.deleteAll();
    }

    @Test
    public void testSave() {
        // given
        Person person = Person.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
        // when
        Person saved = personRepository.save(person);

        // then
        assertTrue(saved.getPersonId() > 0L);
    }

    @Test
    public void testDelete() {
        // given
        Person person = Person.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();

        // when
        Person saved = personRepository.save(person);
        personRepository.delete(saved);

        // then
        assertTrue(personRepository.findById(saved.getPersonId()).isEmpty());
    }

    @Test
    public void testUpdate() {
        // given
        Person person = Person.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();

        // when
        Person saved = personRepository.save(person);
        saved.setGender('F');
        saved.setLastModifiedAt(LocalDateTime.now());
        saved.setLastModifiedBy(1L);
        Person updated = personRepository.save(saved);

        // then
        assertTrue(updated.getPersonId() == saved.getPersonId());
        assertTrue(updated.getGender() == 'F');
        assertTrue(updated.getLastModifiedAt().isAfter(saved.getCreatedAt()));
        assertTrue(updated.getLastModifiedBy() == 1L);
    }

    @Test
    public void testFindAll() {
        // given
        Person person = Person.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();
        Person person2 = Person.builder()
                .gender('F')
                .birthDate(LocalDate.parse("2000-01-01"))
                .build();

        // when
        personRepository.save(person);
        personRepository.save(person2);
        List<Person> all = personRepository.findAll();

        // then
        assertTrue(all.size() == 2);
    }

    @Test
    public void testFindById() {
        // given
        Person person = Person.builder()
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .build();

        // when
        Person saved = personRepository.save(person);
        Optional<Person> optionalPerson = personRepository.findById(saved.getPersonId());

        // then
        assertTrue(optionalPerson.isPresent());
    }
}
