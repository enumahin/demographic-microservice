package com.alienworkspace.cdr.demographic.model.mapper;

import com.alienworkspace.cdr.demographic.model.Person;
import com.alienworkspace.cdr.demographic.model.audit.AuditTrailMapper;
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
                .name(person.getNames().stream()
                        .map(PersonNameMapper.INSTANCE::personNameToPersonNameDto)
                        .toList())
                .address(person.getAddresses().stream()
                        .map(PersonAddressMapper.INSTANCE::toDto)
                        .toList())
                .attributes(person.getAttributes().stream().map(PersonAttributeMapper.INSTANCE::toDto).toList())
                .build();
        AuditTrailMapper.mapToDto(person, personDto);
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
