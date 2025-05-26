package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.util.List;
import java.util.Optional;

/**
 * Service interface that provides methods for managing person names.
 *
 * <p>This interface contains methods for creating, updating, and deleting person
 * names, as well as methods for retrieving person names by ID or by the ID of the
 * person the name belongs to.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface PersonNameService {

    /**
     * Adds a new person name.
     *
     * <p>This method takes a {@link PersonNameDto} object as a parameter and returns a
     * {@link PersonNameDto} object representing the newly added person name.
     *
     * @param personNameDto the PersonNameDto object representing the person name to be
     *                      added
     * @return a PersonNameDto object representing the newly added person name
     */
    PersonNameDto addPersonName(PersonNameDto personNameDto);

    /**
     * Deletes a person name by its ID.
     *
     * <p>This method accepts a {@link RecordVoidRequest} that contains the reason for the deletion
     * and returns a {@link String} indicating the result of the deletion.
     *
     * @param personNameId the ID of the person name to be deleted
     * @param resourceVoidRequest the request containing the ID of the person name to delete
     * @return a String indicating the outcome of the deletion
     */
    String deletePersonName(long personNameId, RecordVoidRequest resourceVoidRequest);

    /**
     * Retrieves a person name by its ID.
     *
     * <p>This method takes an ID as a parameter and returns a {@link PersonNameDto}
     * object representing the person name with the given ID.
     *
     * @param personNameId the ID of the person name to be retrieved
     * @return a PersonNameDto object representing the person name with the given ID
     */
    PersonNameDto findPersonName(long personNameId);

    /**
     * Retrieves the preferred name of a person by their ID.
     *
     * <p>This method takes an ID as a parameter and returns a {@link PersonNameDto}
     * object representing the preferred name of the person with the given ID.
     *
     * @param personId the ID of the person whose preferred name is to be retrieved
     * @return a PersonNameDto object representing the preferred name of the person
     *         with the given ID
     */
    Optional<PersonNameDto> findPreferredNameByPersonId(Long personId);

    /**
     * Retrieves a list of all person names associated with a given person.
     *
     * <p>This method takes the ID of the person as a parameter and returns a list of
     * {@link PersonNameDto} objects representing all the names associated with the
     * given person.
     *
     * @param personId the ID of the person whose names are to be retrieved
     * @return a list of PersonNameDto objects, or an empty list if no names are found
     */
    List<PersonNameDto> findPersonNamesByPersonId(long personId);

    /**
     * Retrieves a list of all person names associated with a given person.
     *
     * <p>This method takes the ID of the person as a parameter and returns a list of
     * {@link PersonNameDto} objects representing all the names associated with the
     * given person.
     *
     * @param personId the ID of the person whose names are to be retrieved
     * @return a list of PersonNameDto objects, or an empty list if no names are found
     */
    List<PersonNameDto> findPersonNamesByPersonId(long personId, boolean voided);

    /**
     * Retrieves a list of all person names associated with a given person including voided records.
     *
     * <p>This method takes the ID of the person as a parameter and returns a list of
     * {@link PersonNameDto} objects representing all the names associated with the
     * given person.
     *
     * @param personId the ID of the person whose names are to be retrieved
     * @return a list of PersonNameDto objects, or an empty list if no names are found
     */
    List<PersonNameDto> findPersonNamesByPersonIdBothVoided(long personId);
}
