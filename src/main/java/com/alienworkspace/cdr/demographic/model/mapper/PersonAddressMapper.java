package com.alienworkspace.cdr.demographic.model.mapper;

import com.alienworkspace.cdr.demographic.model.PersonAddress;
import com.alienworkspace.cdr.demographic.model.audit.AuditTrailMapper;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import com.alienworkspace.cdr.model.dto.person.PersonAddressDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Maps PersonAddress entities to and from PersonAddressDtos.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Mapper(componentModel = "spring")
public interface PersonAddressMapper {

    /**
     * The singleton instance of the PersonAddressMapper class.
     */
    PersonAddressMapper INSTANCE = Mappers.getMapper(PersonAddressMapper.class);

    /**
     * Maps a PersonAddress entity to a PersonAddressDto.
     *
     * @param personAddress the PersonAddress entity to map
     * @return the mapped PersonAddressDto
     */
    default PersonAddressDto toDto(PersonAddress personAddress) {
        PersonAddressDto  personAddressDto = PersonAddressDto.builder()
                .personAddressId(personAddress.getPersonAddressId())
                .addressLine1(personAddress.getAddressLine1())
                .addressLine2(personAddress.getAddressLine2())
                .addressLine3(personAddress.getAddressLine3())
                .postalCode(personAddress.getPostalCode())
                .startDate(personAddress.getStartDate())
                .endDate(personAddress.getEndDate())
                .preferred(personAddress.isPreferred())
                .build();
        if (personAddress.getPerson() != null) {
            personAddressDto.setPersonId(personAddress.getPerson().getPersonId());
        }
        AuditTrailMapper.mapToDto(personAddress, personAddressDto);
        return personAddressDto;
    }

    /**
     * Maps a PersonAddressDto to a PersonAddress entity.
     *
     * @param personAddressDto the PersonAddressDto to map
     * @return the mapped PersonAddress entity
     */
    default PersonAddress toEntity(PersonAddressDto personAddressDto) {
        PersonAddress personAddress = PersonAddress.builder()
                .country(personAddressDto.getCountry().getCountryId())
                .state(personAddressDto.getState().getStateId())
                .county(personAddressDto.getCounty().getCountyId())
                .city(personAddressDto.getCity().getCityId())
                .community(personAddressDto.getCommunity().getCommunityId())
                .addressLine1(personAddressDto.getAddressLine1())
                .addressLine2(personAddressDto.getAddressLine2())
                .addressLine3(personAddressDto.getAddressLine3())
                .postalCode(personAddressDto.getPostalCode())
                .startDate(personAddressDto.getStartDate())
                .endDate(personAddressDto.getEndDate())
                .preferred(personAddressDto.isPreferred())
                .build();

        if (personAddressDto.getPersonAddressId() != null) {
            personAddress.setPersonAddressId(personAddressDto.getPersonAddressId());
        }
        AuditTrailMapper.mapFromDto(personAddressDto, personAddress);
        return personAddress;
    }
}
