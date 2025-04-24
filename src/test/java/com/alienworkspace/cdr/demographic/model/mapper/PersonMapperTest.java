package com.alienworkspace.cdr.demographic.model.mapper;

import com.alienworkspace.cdr.demographic.model.Person;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersonMapperTest {

    @Test
    public void testPersonToPersonDto() {
        // given
        Person person = Person.builder()
                .personId(1L)
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .dead(false)
                .deathDate(null)
                .causeOfDeath(null)
                .build();

        // when
        PersonDto personDto = PersonMapper.INSTANCE.personToPersonDto(person);

        // then
        assertEquals(person.getPersonId(), personDto.getPersonId());
        assertEquals(person.getGender(), personDto.getGender());
        assertEquals(person.getBirthDate(), personDto.getBirthDate());
        assertEquals(person.isDead(), personDto.getDead());
        assertEquals(person.getDeathDate(), personDto.getDeathDate());
        assertEquals(person.getCauseOfDeath(), personDto.getCauseOfDeath());
    }

    @Test
    public void testPersonDtoToPerson() {
        // given
        PersonDto personDto = PersonDto.builder()
                .personId(1L)
                .gender('M')
                .birthDate(LocalDate.parse("1990-01-01"))
                .dead(false)
                .deathDate(null)
                .causeOfDeath(null)
                .build();

        // when
        Person person = PersonMapper.INSTANCE.personDtoToPerson(personDto);

        // then
        assertEquals(personDto.getPersonId(), person.getPersonId());
        assertEquals(personDto.getGender(), person.getGender());
        assertEquals(personDto.getBirthDate(), person.getBirthDate());
        assertEquals(personDto.getDead(), person.isDead());
        assertEquals(personDto.getDeathDate(), person.getDeathDate());
        assertEquals(personDto.getCauseOfDeath(), person.getCauseOfDeath());
    }
}
