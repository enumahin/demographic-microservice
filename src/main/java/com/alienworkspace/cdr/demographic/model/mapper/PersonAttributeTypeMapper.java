package com.alienworkspace.cdr.demographic.model.mapper;

import static org.mapstruct.factory.Mappers.getMapper;

import com.alienworkspace.cdr.demographic.model.PersonAttributeType;
import com.alienworkspace.cdr.demographic.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeTypeDto;
import org.mapstruct.Mapper;

/**
 * Maps PersonAttributeType entities to and from PersonAttributeTypeDtos.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface PersonAttributeTypeMapper {
    /**
     * The singleton instance of the PersonAttributeTypeMapper class.
     */
    PersonAttributeTypeMapper INSTANCE = getMapper(PersonAttributeTypeMapper.class);

    /**
     * Converts a PersonAttributeType entity to a PersonAttributeTypeDto.
     *
     * @param personAttributeType the PersonAttributeType entity to be converted
     * @return a PersonAttributeTypeDto representation of the given PersonAttributeType entity
     */
    default PersonAttributeTypeDto toDto(PersonAttributeType personAttributeType) {
        PersonAttributeTypeDto personAttributeTypeDto = PersonAttributeTypeDto.builder()
                .personAttributeTypeId(personAttributeType.getPersonAttributeTypeId())
                .name(personAttributeType.getName())
                .description(personAttributeType.getDescription())
                .format(personAttributeType.getFormat())
                .build();
        AuditTrailMapper.mapToDto(personAttributeType, personAttributeTypeDto);
        return personAttributeTypeDto;
    }

    /**
     * Converts a PersonAttributeTypeDto to a PersonAttributeType entity.
     *
     * @param personAttributeTypeDto the PersonAttributeTypeDto to be converted
     * @return a PersonAttributeType entity representation of the given PersonAttributeTypeDto
     */
    default PersonAttributeType toEntity(PersonAttributeTypeDto personAttributeTypeDto) {
        PersonAttributeType personAttributeType = PersonAttributeType.builder()
                .name(personAttributeTypeDto.getName())
                .description(personAttributeTypeDto.getDescription())
                .format(personAttributeTypeDto.getFormat())
                .build();
        if (personAttributeTypeDto.getPersonAttributeTypeId() != null) {
            personAttributeType.setPersonAttributeTypeId(personAttributeTypeDto.getPersonAttributeTypeId());
        }
        AuditTrailMapper.mapFromDto(personAttributeTypeDto, personAttributeType);
        return personAttributeType;
    }
}
