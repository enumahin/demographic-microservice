package com.alienworkspace.cdr.demographic.service.impl;

import com.alienworkspace.cdr.demographic.exception.ResourceNotFoundException;
import com.alienworkspace.cdr.demographic.model.PersonName;
import com.alienworkspace.cdr.demographic.model.mapper.PersonNameMapper;
import com.alienworkspace.cdr.demographic.repository.PersonNameRepository;
import com.alienworkspace.cdr.demographic.service.PersonNameService;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link PersonNameService} interface.
 *
 * <p>This service provides methods to manage person names, including adding and updating person names.
 * It uses {@link PersonNameRepository} to interact with the data layer and {@link PersonNameMapper}
 * to convert between entity and DTO objects.</p>
 *
 * <p>The service also ensures that only one person name can be marked as preferred for a given person.</p>
 *
 * <p>Author: ikenumah</p>
 */
@AllArgsConstructor
@Service
public class PersonNameServiceImpl implements PersonNameService {

    private final PersonNameRepository personNameRepository;

    private final PersonNameMapper personNameMapper;

    /**
     * Adds a new person name.
     *
     * <p>If the person name is marked as preferred, this method will ensure that no other
     * person name for the person is marked as preferred before saving the new person name.</p>
     *
     * @param personNameDto the PersonNameDto object representing the person name to be added
     * @return a PersonNameDto object representing the newly added person name
     */
    @Transactional
    @Override
    public PersonNameDto addPersonName(PersonNameDto personNameDto) {

        if (personNameDto.getPreferred() != null && personNameDto.getPreferred()) {
            personNameRepository.unsetPreferred(personNameDto.getPersonId());
        } else {
            if (personNameRepository.findAllByPersonId(personNameDto.getPersonId()).isEmpty()) {
                personNameDto.setPreferred(true);
            }
        }

        PersonName personName = personNameMapper.personNameDtoToPersonName(personNameDto);
        return personNameMapper.personNameToPersonNameDto(personNameRepository.save(personName));
    }

    /**
     * Deletes a person name by its ID.
     *
     * <p>If the person name being deleted is the preferred name of the person, this method will
     * not delete the person name if the person has more than one name.</p>
     *
     * @param resourceVoidRequest the request containing the ID of the person name to be deleted,
     *                            the reason for deletion, and the ID of the user performing the deletion
     * @return a message indicating the success of the deletion operation
     * @throws RuntimeException if the preferred person name is attempted to be deleted when the person
     *                          has more than one name
     */
    @Override
    public String deletePersonName(long id, RecordVoidRequest resourceVoidRequest) {
        return personNameRepository.findByPersonNameId(id)
                .map(personName -> {
                    if (personName.isPreferred()) {
                        throw new RuntimeException("Preferred person name can not be deleted.");
                    }
                    personName.setVoidedBy(1L);
                    personName.setVoided(true);
                    personName.setVoidReason("Person name deleted");
                    personName.setVoidedAt(LocalDateTime.now());
                    personNameRepository.save(personName);
                    return "Person name deleted successfully";
                }).orElseThrow(() -> new ResourceNotFoundException("Person name not found"));
    }

    /**
     * Finds a person name by its ID.
     *
     * @param personNameId the ID of the person name to find
     * @return the person name with the given ID, or an empty optional if not found
     * @throws ResourceNotFoundException if the person name is not found
     */
    @Override
    public PersonNameDto findPersonName(long personNameId) {
        return personNameRepository.findByPersonNameId(personNameId)
                .map(personNameMapper::personNameToPersonNameDto)
                .orElseThrow(() -> new ResourceNotFoundException("Person name not found"));
    }

    /**
     * Finds the preferred name of a person.
     *
     * @param personId the ID of the person
     * @return the preferred name of the person, or an empty optional if no preferred name is found
     */
    @Override
    public Optional<PersonNameDto> findPreferredNameByPersonId(Long personId) {
        return personNameRepository.findByPersonIdAndPreferred(personId, true)
                .map(personNameMapper::personNameToPersonNameDto);
    }

    /**
     * Finds all person names associated with a given person.
     *
     * @param personId the ID of the person whose names are to be retrieved
     * @return a list of PersonNameDto objects, or an empty list if no names are found
     */
    @Override
    public List<PersonNameDto> findPersonNamesByPersonId(long personId) {
        return findPersonNamesByPersonId(personId, false);
    }

    /**
     * Finds all person names associated with a given person, with the option to only return those that are not voided.
     *
     * @param personId the ID of the person whose names are to be retrieved
     * @param voided if true, voided names will be included in the result; if false, they will be excluded; if null,
     *               the default behavior is to exclude voided names
     * @return a list of PersonNameDto objects, or an empty list if no names are found
     */
    @Override
    public List<PersonNameDto> findPersonNamesByPersonId(long personId, boolean voided) {
        return personNameRepository.findAllByPersonIdAndVoided(personId, voided)
                .stream()
                .map(personNameMapper::personNameToPersonNameDto).toList();
    }

    /**
     * Fetch all names associated with a person including voided names.
     *
     * @param personId the ID of the person whose names are to be retrieved
     * @return list of person names DTO
     */
    @Override
    public List<PersonNameDto> findPersonNamesByPersonIdBothVoided(long personId) {
        return personNameRepository.findAllByPersonId(personId)
                .stream()
                .map(personNameMapper::personNameToPersonNameDto).toList();
    }
}
