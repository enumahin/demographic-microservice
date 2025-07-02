package com.alienworkspace.cdr.demographic.model.mapper;

import com.alienworkspace.cdr.demographic.model.PersonName;
import com.alienworkspace.cdr.demographic.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Maps PersonName entities to and from PersonDtos.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface PersonNameMapper {

    /** The singleton instance of the PersonNameMapper class, needed when you want to call the mapping methods
     * directly without DI For example, PersonNameMapper.INSTANCE.personNameDtoToPersonName(personDto).
     */
    PersonNameMapper INSTANCE = Mappers.getMapper(PersonNameMapper.class);

    /**
     * Converts a PersonName entity to a PersonNameDto.
     *
     * @param personName the PersonName entity to be converted
     * @return a PersonNameDto representation of the given PersonName entity
     */
    default PersonNameDto personNameToPersonNameDto(PersonName personName) {
        PersonNameDto personNameDto = PersonNameDto.builder()
                .personNameId(personName.getPersonNameId())
                .firstName(personName.getFirstName())
                .middleName(personName.getMiddleName())
                .lastName(personName.getLastName())
                .otherName(personName.getOtherName())
                .preferred(personName.isPreferred())
                .build();
        if (personName.getPerson() != null) {
            personNameDto.setPersonId(personName.getPerson().getPersonId());
        }
        AuditTrailMapper.mapToDto(personName, personNameDto);
        return personNameDto;
    }

    /**
     * Converts a PersonNameDto to a PersonName entity.
     *
     * @param personNameDto the PersonNameDto to be converted
     * @return a PersonName entity representation of the given PersonNameDto
     */
    default PersonName personNameDtoToPersonName(PersonNameDto personNameDto) {
        PersonName.PersonNameBuilder builder = PersonName.builder()
                .firstName(personNameDto.getFirstName())
                .middleName(personNameDto.getMiddleName())
                .lastName(personNameDto.getLastName())
                .otherName(personNameDto.getOtherName())
                .preferred(personNameDto.getPreferred() != null && personNameDto.getPreferred());
        if (personNameDto.getPersonNameId() != null) {
            builder.personNameId(personNameDto.getPersonNameId());
        }

        return builder.build();
    }
}
