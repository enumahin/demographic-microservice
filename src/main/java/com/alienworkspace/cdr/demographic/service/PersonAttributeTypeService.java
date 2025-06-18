package com.alienworkspace.cdr.demographic.service;

import com.alienworkspace.cdr.model.dto.person.PersonAttributeTypeDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import java.util.List;

/**
 * This interface defines the methods for managing person attribute types in the system.
 *
 * @author Ikenumah (enumahinm@gmail.com)
 */
public interface PersonAttributeTypeService {

    /**
     * This method is used to save a person attribute type to the database.
     *
     * @param personAttributeTypeDto the person attribute type to be saved
     * @return the saved person attribute type
     */
    PersonAttributeTypeDto savePersonAttributeType(PersonAttributeTypeDto personAttributeTypeDto);

    /**
     * This method is used to update a person attribute type in the database.
     *
     * @param id the id of the person attribute type to be updated
     * @param personAttributeTypeDto the person attribute type to be updated
     * @return the updated person attribute type
     */
    PersonAttributeTypeDto updatePersonAttributeType(int id, PersonAttributeTypeDto personAttributeTypeDto);

    /**
     * This method is used to delete a person attribute type from the database.
     *
     * @param id the id of the person attribute type to be deleted
     * @param resourceVoidRequest the request to void the person attribute type
     */
    void deletePersonAttributeType(int id, RecordVoidRequest resourceVoidRequest);

    /**
     * This method is used to get a person attribute type by its id.
     *
     * @param personAttributeTypeId the id of the person attribute type to be retrieved
     * @return the person attribute type with the specified id
     */
    PersonAttributeTypeDto getPersonAttributeTypeById(int personAttributeTypeId);

    /**
     * This method is used to get all person attribute types.
     *
     * @return a list of all person attribute types
     */
    List<PersonAttributeTypeDto> getAllPersonAttributeTypes();

}
