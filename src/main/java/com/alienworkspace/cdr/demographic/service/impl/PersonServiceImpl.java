package com.alienworkspace.cdr.demographic.service.impl;

import com.alienworkspace.cdr.demographic.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.demographic.helpers.CurrentUser;
import com.alienworkspace.cdr.demographic.model.Person;
import com.alienworkspace.cdr.demographic.model.PersonAddress;
import com.alienworkspace.cdr.demographic.model.PersonAttribute;
import com.alienworkspace.cdr.demographic.model.PersonAttributeType;
import com.alienworkspace.cdr.demographic.model.PersonName;
import com.alienworkspace.cdr.demographic.model.mapper.PersonAddressMapper;
import com.alienworkspace.cdr.demographic.model.mapper.PersonAttributeMapper;
import com.alienworkspace.cdr.demographic.model.mapper.PersonMapper;
import com.alienworkspace.cdr.demographic.model.mapper.PersonNameMapper;
import com.alienworkspace.cdr.demographic.repository.PersonAttributeTypeRepository;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.demographic.service.PersonService;
import com.alienworkspace.cdr.demographic.service.client.MetadataFeignClient;
import com.alienworkspace.cdr.model.dto.metadata.CityDto;
import com.alienworkspace.cdr.model.dto.metadata.CommunityDto;
import com.alienworkspace.cdr.model.dto.metadata.CountryDto;
import com.alienworkspace.cdr.model.dto.metadata.CountyDto;
import com.alienworkspace.cdr.model.dto.metadata.StateDto;
import com.alienworkspace.cdr.model.dto.person.PersonAddressDto;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeDto;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing person operations.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
@Service
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PersonRepository personRepository;
    private final PersonAttributeTypeRepository attributeTypeRepository;
    private final PersonMapper personMapper;
    private final PersonNameMapper personNameMapper;
    private final PersonAddressMapper personAddressMapper;
    private PersonAttributeMapper personAttributeMapper;

    private final MetadataFeignClient metadataFeignClient;

    /**
     * Retrieves a person by ID.
     *
     * @param personId the ID of the person to be retrieved
     * @param includeVoided whether to include voided records
     * @return a PersonDto representation of the person with the given ID
     */
    @Override
    public PersonDto getPerson(String correlationId, Long personId, boolean includeVoided) {
        if (personId == null) {
            throw new ResourceNotFoundException("PersonId can nul be null");
        }
        return personRepository.findCompleteById(personId)
                .map(person -> {
                    PersonDto personDto = personMapper.personToPersonDto(person);
                    fetchPersonAddresses(correlationId, person, personDto);
                    if (!includeVoided) {
                        personDto.setName(person.getNames().stream()
                                .filter(name -> !name.isVoided())
                                .map(personNameMapper::personNameToPersonNameDto)
                                .collect(Collectors.toSet()));
                        personDto.setAttributes(person.getAttributes().stream()
                                .filter(attribute -> !attribute.isVoided())
                                .map(personAttributeMapper::toDto)
                                .collect(Collectors.toSet()));
                        personDto.setAddress(personDto.getAddress().stream()
                                .filter(address -> !address.getVoided())
                                .collect(Collectors.toSet()));
                    }
                    return personDto;
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Person with Id of %d not found.", personId)));
    }

    private Person getPerson(long personId) {
        return personRepository.findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Person with Id of %d not found.", personId)));
    }

    /**
     * Retrieves all persons.
     *
     * @return a list of PersonDto representations of all persons
     */
    @Override
    public List<PersonDto> getPersons() {
        return personRepository.findCompleteAll()
                .stream()
                .map(personMapper::personToPersonDto)
                .toList();
    }

    /**
     * Adds a new person.
     *
     * @param personDto the PersonDto representation of the person to be added
     * @return a PersonDto representation of the newly added person
     */
    @Override
    public PersonDto addPerson(PersonDto personDto, String correlationId) {
        try {
            logger.info("Adding person: {}", personDto);
            Person person = personMapper.personDtoToPerson(personDto);
            Person savedPerson = personRepository.save(person);
            personDto.getName().forEach(name ->
                    savedPerson.addName(PersonNameMapper.INSTANCE.personNameDtoToPersonName(name))
            );
            personDto.getAttributes().forEach(attribute -> {
                PersonAttributeType attributeType = attributeTypeRepository
                        .findById(attribute.getPersonAttributeType().getPersonAttributeTypeId())
                        .orElseThrow(() -> new ResourceNotFoundException("Person attribute type not found"));
                PersonAttribute personAttribute = personAttributeMapper.toEntity(attribute);
                personAttribute.setPersonAttributeType(attributeType);
                savedPerson.addAttribute(personAttribute);
            });
            personDto.getAddress().forEach(address ->
                    savedPerson.addAddress(
                            PersonAddressMapper.INSTANCE.toEntity(address)
                    ));
            personRepository.save(savedPerson);
            PersonDto savedPersonDto = personMapper.personToPersonDto(savedPerson);
            fetchPersonAddresses(correlationId, savedPerson, savedPersonDto);
            return savedPersonDto;
        } catch (Exception e) {
            logger.error("Error adding person:", e);
            throw new IllegalArgumentException("Error adding person: {}", e);
        }
    }

    /**
     * Updates a person.
     *
     * @param personDto the PersonDto representation of the person to be updated
     * @return a PersonDto representation of the updated person
     */
    @Override
    public PersonDto updatePerson(long personId, PersonDto personDto, String correlationId) {
        if (personDto.getPersonId() == null) {
            throw new ResourceNotFoundException("PersonId can not be null.");
        }
        Person person = personRepository.findCompleteById(personDto.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("PersonId of %d not found",
                        personDto.getPersonId())));
        PersonDto updatedPerson = personMapper.personToPersonDto(updatePerson(person, personDto));
        fetchPersonAddresses(correlationId, person, updatedPerson);
        return updatedPerson;
    }

    private Person updatePerson(Person person, PersonDto personDto) {
        try {
            if (personDto.getBirthDate() != null) {
                person.setBirthDate(personDto.getBirthDate());
            }

            if (personDto.getDead() != null) {
                person.setDead(personDto.getDead());
            }

            if (personDto.getDeathDate() != null) {
                person.setDeathDate(personDto.getDeathDate());
            }

            if (personDto.getCauseOfDeath() != null) {
                person.setCauseOfDeath(personDto.getCauseOfDeath());
            }

            if (personDto.getGender() != null) {
                person.setGender(personDto.getGender());
            }

            person.setLastModifiedBy(1L);
            person.setLastModifiedAt(LocalDateTime.now());

            return personRepository.save(person);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating person", e);
        }
    }

    /**
     * Deletes a person by ID.
     *
     * @param id the ID of the person to be deleted
     * @param recordVoidRequest the record void request containing the ID of the person to be deleted
     * @return a message indicating the outcome of the deletion
     */
    @Override
    public ResponseDto deletePerson(long id, RecordVoidRequest recordVoidRequest) {
        String reason = recordVoidRequest.getVoidReason();
        return personRepository.findById(id)
                .map(person -> {
                    person.setVoided(true);
                    person.setVoidReason(reason);
                    person.setVoidedAt(LocalDateTime.now());
                    person.setVoidedBy(1L);
                    personRepository.save(person);
                    return new ResponseDto(200, "Person deleted successfully");
                }).orElseThrow(() -> new ResourceNotFoundException(String.format("PersonId of %d not found", id)));
    }

    /**
     * Adds a name to a person.
     *
     * @param personId the ID of the person to add the name to
     * @param personNameDto the PersonNameDto representation of the name to be added
     * @return a PersonDto representation of the person with the added name
     */
    @Override
    public PersonNameDto addPersonName(long personId, PersonNameDto personNameDto) {
        return personRepository.findCompleteById(personId)
                .map(person -> {
                    person.addName(personNameMapper.personNameDtoToPersonName(personNameDto));
                    Person savedPerson = personRepository.save(person);

                    if (personNameDto.getPreferred() != null && personNameDto.getPreferred()) {
                        return personNameMapper.personNameToPersonNameDto(savedPerson.getPreferredName());
                    } else {
                        PersonName personName = savedPerson.getNames().stream()
                                .filter(name ->
                                        Objects.equals(name.getLastName(), personNameDto.getLastName())
                                                && Objects.equals(name.getFirstName(), personNameDto.getFirstName())
                                                && Objects.equals(name.getMiddleName(), personNameDto.getMiddleName())
                                                && Objects.equals(name.getOtherName(), personNameDto.getOtherName()))
                                .findFirst()
                                .orElseThrow(() -> new ResourceNotFoundException("Person name not found"));
                        return personNameMapper.personNameToPersonNameDto(personName);
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
    }

    /**
     * Deletes a name from a person.
     *
     * @param personId the ID of the person to delete the name from
     * @param personNameId the ID of the name to be deleted
     * @param resourceVoidRequest the record void request containing the ID of the person to be deleted
     */
    @Override
    public void deletePersonName(long personId, long personNameId, RecordVoidRequest resourceVoidRequest) {
        Person person = getPerson(personId);
        person.getNames().stream()
                .filter(name -> name.getPersonNameId() == personNameId)
                .findFirst()
                .map(personName -> {
                    personName.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                    personName.setVoided(true);
                    personName.setVoidReason(resourceVoidRequest.getVoidReason());
                    personName.setVoidedAt(LocalDateTime.now());
                    return personRepository.save(person);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person name not found"));
    }

    /**
     * Adds an address to a person.
     *
     * @param personId the ID of the person to add the address to
     * @param personAddressDto the PersonAddressDto representation of the address to be added
     * @return a PersonDto representation of the person with the added address
     */
    @Override
    @Transactional
    public PersonAddressDto addAddress(Long personId, PersonAddressDto personAddressDto, String correlationId) {
        return personRepository.findCompleteById(personId)
                .map(person -> {
                    person.addAddress(personAddressMapper.toEntity(personAddressDto));
                    Person savedPerson = personRepository.save(person);
                    return fetchAddress(correlationId, savedPerson.getAddresses().stream()
                            .filter(address ->
                                    Objects.equals(address.getAddressLine1(), personAddressDto.getAddressLine1())
                                    && Objects.equals(address.getAddressLine2(), personAddressDto.getAddressLine2()))
                            .findFirst().orElseThrow(() -> new ResourceNotFoundException("Person address not found")));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
    }

    /**
     * Adds an attribute to a person.
     *
     * @param personId the ID of the person to add the attribute to
     * @param personAttributeDto the PersonAttributeDto representation of the attribute to be added
     * @return a PersonDto representation of the person with the added attribute
     */
    @Override
    public PersonAttributeDto addAttribute(Long personId, PersonAttributeDto personAttributeDto) {

        if (personAttributeDto.getPersonAttributeType() == null
                || personAttributeDto.getPersonAttributeType().getPersonAttributeTypeId() == null) {
            throw new ResourceNotFoundException(
                    "Person attribute type and person attribute type id can not be not null");
        }
        attributeTypeRepository
                .findById(personAttributeDto.getPersonAttributeType().getPersonAttributeTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Person attribute type not found"));

        return personRepository.findCompleteById(personId)
                .map(person -> {
                    person.addAttribute(personAttributeMapper.toEntity(personAttributeDto));
                    personRepository.save(person);
                    return personAttributeMapper.toDto(person.getPreferredAttribute(
                            personAttributeDto.getPersonAttributeType().getPersonAttributeTypeId()));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
    }

    /**
     * Updates a name in a person.
     *
     * @param personId the ID of the person to update the name in
     * @param personNameId the ID of the name to be updated
     * @param preferred the preferred name
     * @return a PersonDto representation of the person with the updated name
     */
    @Override
    public PersonNameDto updatePersonName(long personId, long personNameId, boolean preferred) {
        Person person = getPerson(personId);
        return person.getNames().stream()
                .filter(name -> name.getPersonNameId() == personNameId)
                .findFirst()
                .map(name -> {
                    if (!name.isPreferred() && preferred) {
                        person.getPreferredName().setPreferred(false);
                        name.setPreferred(true);
                        name.setLastModifiedAt(LocalDateTime.now());
                        name.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                        personRepository.save(person);
                        return personNameMapper.personNameToPersonNameDto(person.getPreferredName());
                    } else {
                        throw new IllegalArgumentException("Preferred name cannot be updated, add a new name instead");
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person name not found"));
    }

    /**
     * Updates an address in a person.
     *
     * @param personId the ID of the person to update the address in
     * @param personAddressId the ID of the address to be updated
     * @param preferred the preferred address
     * @return a PersonDto representation of the person with the updated address
     */
    @Override
    public PersonAddressDto updateAddress(long personId, long personAddressId, boolean preferred,
                                          String correlationId) {
        Person person = getPerson(personId);
        return person.getAddresses().stream()
                .filter(address -> address.getPersonAddressId() == personAddressId)
                .findFirst()
                .map(address -> {
                    if (!address.isPreferred() && preferred) {
                        person.getPreferredAddress().setPreferred(false);
                        address.setPreferred(true);
                        address.setLastModifiedAt(LocalDateTime.now());
                        address.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                        personRepository.save(person);
                        return fetchAddress(correlationId, person.getPreferredAddress());
                    } else {
                        throw new IllegalArgumentException(
                                "Preferred address cannot be updated, add another preferred address instead");
                    }
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person address not found"));
    }

    /**
     * Updates an attribute in a person.
     *
     * @param personId the ID of the person to update the attribute in
     * @param personAttributeId the ID of the attribute to be updated
     * @param preferred the preferred attribute
     * @return a PersonDto representation of the person with the updated attribute
     */
    @Override
    public PersonAttributeDto updateAttribute(long personId, long personAttributeId, boolean preferred) {

        Person person = personRepository
                .findById(personId)
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));

        if (preferred) {

            PersonAttributeType personAttributeType = person.getAttributes()
                    .stream()
                    .filter(attribute -> attribute.getPersonAttributeId() == personAttributeId)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Person attribute not found"))
                    .getPersonAttributeType();

            person.getAttributes().stream()
                    .filter(attribute -> attribute.getPersonAttributeId() != personAttributeId
                            && attribute.getPersonAttributeType().getPersonAttributeTypeId()
                            == personAttributeType.getPersonAttributeTypeId())
                    .forEach(attribute -> attribute.setPreferred(false));

            person.getAttributes().stream()
                    .filter(attribute -> attribute.getPersonAttributeId() == personAttributeId)
                    .findFirst()
                    .ifPresent(attribute -> {
                        attribute.setPreferred(true);
                        attribute.setLastModifiedAt(LocalDateTime.now());
                        attribute.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                        personRepository.save(person);
                    });
            return personAttributeMapper.toDto(person.getPreferredAttribute(
                    personAttributeType.getPersonAttributeTypeId()));
        } else {
            throw new IllegalArgumentException(
                    "Preferred Attribute cannot be changed, instead add another preferred attribute");
        }
    }

    /**
     * Deletes an address from a person.
     *
     * @param personId the ID of the person to delete the address from
     * @param personAddressId the ID of the address to be deleted
     * @param voidRequest the RecordVoidRequest containing the void reason for the address
     */
    @Override
    public void deleteAddress(long personId, long personAddressId, RecordVoidRequest voidRequest) {
        Person person = getPerson(personId);
        person.getAddresses().stream()
                .filter(address -> address.getPersonAddressId() == personAddressId)
                .findFirst()
                .map(address -> {
                    address.setVoided(true);
                    address.setVoidedAt(LocalDateTime.now());
                    address.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                    address.setVoidReason(voidRequest.getVoidReason());
                    return personRepository.save(person);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person address not found"));
    }

    /**
     * Deletes an attribute from a person.
     *
     * @param personId the ID of the person to delete the attribute from
     * @param personAttributeId the ID of the attribute to be deleted
     * @param voidRequest the RecordVoidRequest containing the void reason for the attribute
     */
    @Override
    public void deleteAttribute(long personId, long personAttributeId, RecordVoidRequest voidRequest) {
        Person person = getPerson(personId);
        person.getAttributes().stream()
                .filter(attribute -> attribute.getPersonAttributeId() == personAttributeId)
                .findFirst()
                .map(attribute -> {
                    attribute.setVoided(true);
                    attribute.setVoidedAt(LocalDateTime.now());
                    attribute.setVoidedBy(CurrentUser.getCurrentUser().getPersonId());
                    attribute.setVoidReason(voidRequest.getVoidReason());
                    return personRepository.save(person);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person attribute not found"));
    }

    /**
     * Gets a person address by its ID.
     *
     * @param personId the ID of the person the address belongs to
     * @param personAddressId the ID of the person address to be retrieved
     * @return a PersonAddressDto representation of the person address
     */
    @Override
    public PersonAddressDto getPersonAddress(long personId, long personAddressId) {
        return personRepository.findCompleteById(personId)
                .map(Person::getAddresses)
                .map(addresses -> addresses.stream()
                        .filter(address -> address.getPersonAddressId() == personAddressId)
                        .findFirst()
                        .map(personAddressMapper::toDto)
                        .orElseThrow(() -> new ResourceNotFoundException("Person address not found")))
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
    }

    /**
     * Gets all addresses for a person.
     *
     * @param personId the ID of the person to retrieve addresses for
     * @return a Set of PersonAddressDto representations of all addresses for the person
     */
    @Override
    public Set<PersonAddressDto> getPersonAddresses(long personId) {
        return personRepository.findCompleteById(personId)
                .map(Person::getAddresses)
                .map(addresses -> addresses.stream()
                        .map(personAddressMapper::toDto)
                        .collect(Collectors.toSet()))
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
    }

    private void fetchPersonAddresses(String correlationId, Person person, PersonDto personDto) {

        if (personDto.getAddress() == null) {
            personDto.setAddress(new HashSet<>());
        }

        try {
            person.getAddresses()
                    .forEach(address -> {
                        try {
                            PersonAddressDto perssonAddressDto = fetchAddress(correlationId, address);
                            if (perssonAddressDto != null) {
                                Set<PersonAddressDto> personAddressDtos = new HashSet<>(personDto.getAddress());
                                personAddressDtos.add(perssonAddressDto);
                                personDto.setAddress(personAddressDtos);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw new IllegalStateException("Error getting address", e);
                        }
                    });
        } catch (Exception e) {
            throw new IllegalStateException("Error getting country", e);
        }
    }

    private PersonAddressDto fetchAddress(String correlationId, PersonAddress address) {
        try {
            if (address == null) {
                return null;
            }
            PersonAddressDto personAddressDto = personAddressMapper.toDto(address);
            if (address.getCountry() > 0) {
                CountryDto countryDto = metadataFeignClient.getPersonLocation(correlationId, address.getCountry(),
                        address.getState(), address.getCounty(), address.getCity(), address.getCommunity()).getBody();
                if (countryDto == null) {
                    throw new IllegalStateException("Error getting country");
                }
                personAddressDto.setCountry(
                        CountryDto.builder()
                                .countryId(address.getCountry())
                                .countryName(countryDto.getCountryName())
                                .build()
                );
                StateDto stateDto = countryDto.getStates().stream()
                        .filter(state -> state.getStateId() == address.getState())
                        .findFirst()
                        .orElse(null);
                if (stateDto != null) {
                    personAddressDto.setState(
                            StateDto.builder()
                                    .stateId(address.getState())
                                    .stateName(stateDto.getStateName())
                                    .build()
                    );
                    CountyDto countyDto = stateDto.getCounties().stream()
                            .filter(county -> county.getCountyId() == address.getCounty())
                            .findFirst()
                            .orElse(null);
                    if (countyDto != null) {
                        personAddressDto.setCounty(
                                CountyDto.builder()
                                        .countyId(address.getCounty())
                                        .countyName(countyDto.getCountyName())
                                        .build()
                        );
                        CityDto cityDto = countyDto.getCities().stream()
                                .filter(city -> city.getCityId() == address.getCity())
                                .findFirst()
                                .orElse(null);
                        if (cityDto != null) {
                            personAddressDto.setCity(
                                    CityDto.builder()
                                            .cityId(address.getCity())
                                            .cityName(cityDto.getCityName())
                                            .build()
                            );

                            cityDto.getCommunities().stream()
                                    .filter(community ->
                                            community.getCommunityId() == address.getCommunity())
                                    .findFirst()
                                    .ifPresent(communityDto -> personAddressDto.setCommunity(
                                            CommunityDto.builder()
                                                    .communityId(address.getCommunity())
                                                    .communityName(communityDto.getCommunityName())
                                                    .build()
                                    ));
                        }
                    }
                }
            }
            return personAddressDto;
        } catch (Exception e) {
            throw new IllegalStateException("Error getting address: " + e.getMessage(), e);
        }
    }
}
