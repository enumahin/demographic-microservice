package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.model.dto.person.PersonAddressDto;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeDto;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import java.util.List;
import java.util.Set;

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
     * @param personId      the ID of the person to be retrieved
     * @param includeVoided a flag indicating whether to include voided records
     * @return a PersonDto object containing the data of the person with the given ID
     */
    PersonDto getPerson(Long personId, boolean includeVoided);

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
     * @param personId  the ID of the person to be updated
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

    /**
     * Adds a new person name to the system.
     *
     * @param personId the ID of the person the name belongs to
     * @param personNameDto the PersonNameDto object containing the data of the person name to be added
     * @return a PersonNameDto object containing the data of the newly added person name
     */
    PersonNameDto addPersonName(long personId, PersonNameDto personNameDto);

    /**
     * Deletes a person name by its ID.
     *
     * @param personId the ID of the person the name belongs to
     * @param personNameId the ID of the person name to be deleted
     * @param resourceVoidRequest the request containing the ID of the person name to delete
     */
    void deletePersonName(long personId, long personNameId, RecordVoidRequest resourceVoidRequest);

    /**
     * Adds a new person address to the system.
     *
     * @param personId         the ID of the person the address belongs to
     * @param personAddressDto the PersonAddressDto object containing the data of the person address to be added
     * @return a PersonAddressDto object containing the data of the newly added person address
     */
    PersonAddressDto addAddress(Long personId, PersonAddressDto personAddressDto);

    /**
     * Adds a new person attribute to the system.
     *
     * @param personId the ID of the person the attribute belongs to
     * @param personAttributeDto the PersonAttributeDto object containing the data of the person attribute to be added
     * @return a PersonAttributeDto object containing the data of the newly added person attribute
     */
    PersonAttributeDto addAttribute(Long personId, PersonAttributeDto personAttributeDto);

    /**
     * Updates an existing person name in the system.
     *
     * @param personId the ID of the person the name belongs to
     * @param personNameId the ID of the person name to be updated
     * @param preferred the preferred name
     * @return a PersonNameDto object containing the updated data of the person name
     */
    PersonNameDto updatePersonName(long personId, long personNameId, boolean preferred);

    /**
     * Updates an existing person address in the system.
     *
     * @param personId        the ID of the person the address belongs to
     * @param personAddressId the ID of the person address to be updated
     * @param preferred       the preferred address
     * @return a PersonAddressDto object containing the updated data of the person address
     */
    PersonAddressDto updateAddress(long personId, long personAddressId, boolean preferred);

    /**
     * Updates an existing person attribute in the system.
     *
     * @param personId the ID of the person the attribute belongs to
     * @param personAttributeId the ID of the person attribute to be updated
     * @param preferred the preferred attribute
     * @return a PersonAttributeDto object containing the updated data of the person attribute
     */
    PersonAttributeDto updateAttribute(long personId, long personAttributeId, boolean preferred);

    /**
     * Deletes a person address by its ID.
     *
     * @param personId the ID of the person the address belongs to
     * @param personAddressId the ID of the person address to be deleted
     * @param voidRequest the request containing the ID of the person address to delete
     */
    void deleteAddress(long personId, long personAddressId, RecordVoidRequest voidRequest);

    /**
     * Deletes a person attribute by its ID.
     *
     * @param personId the ID of the person the attribute belongs to
     * @param personAttributeId the ID of the person attribute to be deleted
     * @param voidRequest the request containing the ID of the person attribute to delete
     */
    void deleteAttribute(long personId, long personAttributeId, RecordVoidRequest voidRequest);

    /**
     * Retrieves a person address by its ID.
     *
     * @param personId the ID of the person the address belongs to
     * @param personAddressId the ID of the person address to retrieve
     * @return a PersonAddressDto object containing the data of the person address
     */
    PersonAddressDto getPersonAddress(long personId, long personAddressId);

    /**
     * Retrieves all person addresses for a person.
     *
     * @param personId the ID of the person to retrieve addresses for
     * @return a Set of PersonAddressDto objects containing the data of all person addresses
     */
    Set<PersonAddressDto> getPersonAddresses(long personId);
}
