package com.alienworkspace.cdr.demographic.model.mapper;

import static org.mapstruct.factory.Mappers.getMapper;

import com.alienworkspace.cdr.demographic.model.PersonAttribute;
import com.alienworkspace.cdr.demographic.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeDto;
import org.mapstruct.Mapper;

/**
 * Maps PersonAttribute entities to and from PersonAttributeDtos.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface PersonAttributeMapper {
    /**
     * The singleton instance of the PersonAttributeMapper class.
     */
    PersonAttributeMapper INSTANCE = getMapper(PersonAttributeMapper.class);

    /**
     * Converts a PersonAttribute entity to a PersonAttributeDto.
     *
     * @param personAttribute the PersonAttribute entity to be converted
     * @return a PersonAttributeDto representation of the given PersonAttribute entity
     */
    default PersonAttributeDto toDto(PersonAttribute personAttribute) {
        PersonAttributeDto personAttributeDto = PersonAttributeDto.builder()
                .personAttributeId(personAttribute.getPersonAttributeId())
                .personAttributeType(
                        PersonAttributeTypeMapper.INSTANCE.toDto(personAttribute.getPersonAttributeType()))
                .value(personAttribute.getAttributeValue())
                .preferred(personAttribute.isPreferred())
                .build();
        if (personAttribute.getPerson() != null) {
            personAttributeDto.setPerson_id(personAttribute.getPerson().getPersonId());
        }
        AuditTrailMapper.mapToDto(personAttribute, personAttributeDto);
        return personAttributeDto;
    }

    /**
     * Converts a PersonAttributeDto to a PersonAttribute entity.
     *
     * @param personAttributeDto the PersonAttributeDto to be converted
     * @return a PersonAttribute entity representation of the given PersonAttributeDto
     */
    default PersonAttribute toEntity(PersonAttributeDto personAttributeDto) {
        PersonAttribute personAttribute = PersonAttribute.builder()
                .personAttributeType(
                        PersonAttributeTypeMapper.INSTANCE.toEntity(personAttributeDto.getPersonAttributeType()))
                .attributeValue(personAttributeDto.getValue())
                .preferred(personAttributeDto.isPreferred())
                .build();
        if (personAttributeDto.getPersonAttributeId() != null) {
            personAttribute.setPersonAttributeId(personAttributeDto.getPersonAttributeId());
        }
        AuditTrailMapper.mapFromDto(personAttributeDto, personAttribute);
        return personAttribute;
    }
}
