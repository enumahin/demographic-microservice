package com.alienworkspace.cdr.demographic.model.mapper;

import com.alienworkspace.cdr.demographic.model.Person;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * Maps Person entities to and from PersonDtos.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {

    /** The singleton instance of the PersonMapper class, needed when you want to call the mapping methods
     * directly without DI For example, PersonMapper.INSTANCE.personDtoToPerson (personDto).
     */
    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    /**
     * Converts a Person entity to a PersonDto.
     *
     * @param person the Person entity to be converted
     * @return a PersonDto representation of the given Person entity
     */
    default PersonDto personToPersonDto(Person person) {
        PersonDto personDto = PersonDto.builder()
                .personId(person.getPersonId())
                .gender(person.getGender())
                .birthDate(person.getBirthDate())
                .dead(person.isDead())
                .deathDate(person.getDeathDate())
                .causeOfDeath(person.getCauseOfDeath())
                .build();
        personDto.setCreatedAt(person.getCreatedAt());
        personDto.setCreatedBy(person.getCreatedBy());
        personDto.setLastModifiedAt(person.getLastModifiedAt());
        personDto.setLastModifiedBy(person.getLastModifiedBy());
        personDto.setVoided(person.isVoided());
        personDto.setVoidedAt(person.getVoidedAt());
        personDto.setVoidedBy(person.getVoidedBy());
        personDto.setVoidReason(person.getVoidReason());
        personDto.setUuid(person.getUuid());
        return personDto;
    }

    /**
     * Converts a PersonDto to a Person entity.
     *
     * @param personDto the PersonDto to be converted
     * @return a Person entity representation of the given PersonDto
     */
    default Person personDtoToPerson(PersonDto personDto) {
        Person.PersonBuilder builder = Person.builder()
                .gender(personDto.getGender())
                .birthDate(personDto.getBirthDate())
                .deathDate(personDto.getDeathDate())
                .causeOfDeath(personDto.getCauseOfDeath());
        if (personDto.getPersonId() != null) {
            builder.personId(personDto.getPersonId());
        }
        if (personDto.getDead() != null) {
            builder.dead(personDto.getDead());
        }
        return builder.build();
    }
}
