package com.alienworkspace.cdr.demographic.service.impl;

import com.alienworkspace.cdr.demographic.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.demographic.model.Person;
import com.alienworkspace.cdr.demographic.model.mapper.PersonMapper;
import com.alienworkspace.cdr.demographic.repository.PersonRepository;
import com.alienworkspace.cdr.demographic.service.PersonService;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
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
    private final PersonMapper personMapper;

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
        return personRepository.findById(personId)
                .map(personMapper::personToPersonDto)
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
        return personRepository.findAll()
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
            throw new RuntimeException("Error adding person: {}", e);
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
            throw new RuntimeException("Error updating person", e);
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
}
