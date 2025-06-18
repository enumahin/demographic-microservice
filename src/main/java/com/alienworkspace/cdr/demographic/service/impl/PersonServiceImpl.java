package com.alienworkspace.cdr.demographic.service.impl;

import com.alienworkspace.cdr.demographic.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.demographic.helpers.CurrentUser;
import com.alienworkspace.cdr.demographic.model.Person;
import com.alienworkspace.cdr.demographic.model.mapper.PersonAddressMapper;
import com.alienworkspace.cdr.demographic.model.mapper.PersonAttributeMapper;
import com.alienworkspace.cdr.demographic.model.mapper.PersonMapper;
import com.alienworkspace.cdr.demographic.model.mapper.PersonNameMapper;
import com.alienworkspace.cdr.demographic.repository.PersonAttributeTypeRepository;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.demographic.service.PersonService;
import com.alienworkspace.cdr.model.dto.person.PersonAddressDto;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeDto;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    /**
     * Retrieves a person by ID.
     *
     * @param personId the ID of the person to be retrieved
     * @return a PersonDto representation of the person with the given ID
     */
    @Override
    public PersonDto getPerson(Long personId) {
        if (personId == null) {
            throw new ResourceNotFoundException("PersonId can nul be null");
        }
        return personRepository.findCompleteById(personId)
                .map(personMapper::personToPersonDto)
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
    public PersonDto addPerson(PersonDto personDto) {
        try {
            Person person = personMapper.personDtoToPerson(personDto);
            Person savedPerson = personRepository.save(person);
            return personMapper.personToPersonDto(savedPerson);
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
    public PersonDto updatePerson(long personId, PersonDto personDto) {
        if (personDto.getPersonId() == null) {
            throw new ResourceNotFoundException("PersonId can not be null.");
        }
        Person person = personRepository.findByPersonId(personDto.getPersonId())
                .orElseThrow(() -> new ResourceNotFoundException(String.format("PersonId of %d not found",
                        personDto.getPersonId())));
        return personMapper.personToPersonDto(updatePerson(person, personDto));
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
    public PersonDto addPersonName(long personId, PersonNameDto personNameDto) {
        return personRepository.findCompleteById(personId)
                .map(person -> {
                    person.addName(personNameMapper.personNameDtoToPersonName(personNameDto));
                    personRepository.save(person);
                    return personMapper.personToPersonDto(person);
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
    public PersonDto addAddress(Long personId, PersonAddressDto personAddressDto) {
        return personRepository.findCompleteById(personId)
                .map(person -> {
                    person.addAddress(personAddressMapper.toEntity(personAddressDto));
                    personRepository.save(person);
                    return personMapper.personToPersonDto(person);
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
    public PersonDto addAttribute(Long personId, PersonAttributeDto personAttributeDto) {

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
                    return personMapper.personToPersonDto(person);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person not found"));
    }

    /**
     * Updates a name in a person.
     *
     * @param personId the ID of the person to update the name in
     * @param personNameId the ID of the name to be updated
     * @param personNameDto the PersonNameDto representation of the updated name
     * @return a PersonDto representation of the person with the updated name
     */
    @Override
    public PersonDto updatePersonName(long personId, long personNameId, PersonNameDto personNameDto) {
        Person person = getPerson(personId);
        return person.getNames().stream()
                .filter(name -> name.getPersonNameId() == personNameId)
                .findFirst()
                .map(name -> {
                    if (Boolean.TRUE.equals(personNameDto.getPreferred())) {
                        person.getPreferredName().setPreferred(false);
                        name.setPreferred(true);
                    } else {
                        name.setPreferred(false);
                    }

                    name.setLastModifiedAt(LocalDateTime.now());
                    name.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                    personRepository.save(person);
                    return personMapper.personToPersonDto(person);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person name not found"));
    }

    /**
     * Updates an address in a person.
     *
     * @param personId the ID of the person to update the address in
     * @param personAddressId the ID of the address to be updated
     * @param personAddressDto the PersonAddressDto representation of the updated address
     * @return a PersonDto representation of the person with the updated address
     */
    @Override
    public PersonDto updateAddress(long personId, long personAddressId, PersonAddressDto personAddressDto) {
        Person person = getPerson(personId);
        return person.getAddresses().stream()
                .filter(address -> address.getPersonAddressId() == personAddressId)
                .findFirst()
                .map(address -> {
                    if (personAddressDto.isPreferred()) {
                        person.getPreferredAddress().setPreferred(false);
                        address.setPreferred(true);
                    } else {
                        address.setPreferred(false);
                    }

                    address.setLastModifiedAt(LocalDateTime.now());
                    address.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                    personRepository.save(person);
                    return personMapper.personToPersonDto(person);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person address not found"));
    }

    /**
     * Updates an attribute in a person.
     *
     * @param personId the ID of the person to update the attribute in
     * @param personAttributeId the ID of the attribute to be updated
     * @param personAttributeDto the PersonAttributeDto representation of the updated attribute
     * @return a PersonDto representation of the person with the updated attribute
     */
    @Override
    public PersonDto updateAttribute(long personId, long personAttributeId, PersonAttributeDto personAttributeDto) {

        if (personAttributeDto.getPersonAttributeType() == null
                || personAttributeDto.getPersonAttributeType().getPersonAttributeTypeId() == null) {
            throw new ResourceNotFoundException(
                    "Person attribute type and person attribute type id can not be not null");
        }
        attributeTypeRepository
                .findById(personAttributeDto.getPersonAttributeType().getPersonAttributeTypeId())
                .orElseThrow(() -> new ResourceNotFoundException("Person attribute type not found"));
        Person person = getPerson(personId);
        return person.getAttributes().stream()
                .filter(attribute -> attribute.getPersonAttributeId() == personAttributeId)
                .findFirst()
                .map(attribute -> {
                    if (personAttributeDto.isPreferred()) {
                        person.getPreferredAttribute(
                                personAttributeDto.getPersonAttributeType().getPersonAttributeTypeId())
                                .setPreferred(false);
                        attribute.setPreferred(true);
                    } else {
                        attribute.setPreferred(false);
                    }
                    attribute.setLastModifiedAt(LocalDateTime.now());
                    attribute.setLastModifiedBy(CurrentUser.getCurrentUser().getPersonId());
                    personRepository.save(person);
                    return personMapper.personToPersonDto(person);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Person attribute not found"));
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
}
