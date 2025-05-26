package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import java.util.List;

/**
 * This interface defines the methods for managing person records in the system.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface PersonService {

    /**
     * Retrieves all persons in the system.
     *
     * @return a list of PersonDto objects, each containing the data of a person
     */
    List<PersonDto> getPersons();

    /**
     * Retrieves a person by their ID.
     *
     * @param personId the ID of the person to be retrieved
     * @return a PersonDto object containing the data of the person with the given ID
     */
    PersonDto getPerson(Long personId);

    /**
     * Adds a new person to the system.
     *
     * @param personDto the PersonDto object containing the data of the person to be added
     * @return a PersonDto object containing the data of the newly added person
     */
    PersonDto addPerson(PersonDto personDto);

    /**
     * Updates an existing person in the system.
     *
     * @param personId the ID of the person to be updated
     * @param personDto the PersonDto object containing the updated data of the person
     * @return a PersonDto object containing the updated data of the person
     */
    PersonDto updatePerson(long personId, PersonDto personDto);

    /**
     * Deletes a person by their ID.
     *
     * @param personId the ID of the person to be deleted
     * @param voidRequest the record void request containing the ID of the person to be deleted
     * @return a message indicating the outcome of the deletion
     */
    ResponseDto deletePerson(long personId, RecordVoidRequest voidRequest);
}
