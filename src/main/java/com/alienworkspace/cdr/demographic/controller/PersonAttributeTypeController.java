package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.helpers.Constants;
import com.alienworkspace.cdr.demographic.service.PersonAttributeTypeService;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeTypeDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for managing person attribute types in the demographic module.
 * Provides endpoints for CRUD operations on PersonAttributeType entities.
 *
 * <p>Endpoints:</p>
 * <ul>
 *   <li>{@code GET /demographic/person-attribute-type} - Retrieves all person attribute types</li>
 *   <li>{@code GET /demographic/person-attribute-type/{id}} - Retrieves a person attribute type by ID</li>
 *   <li>{@code POST /demographic/person-attribute-type} - Adds a new person attribute type</li>
 *   <li>{@code PUT /demographic/person-attribute-type} - Updates a person attribute type</li>
 *   <li>{@code DELETE /demographic/person-attribute-type/{id}} - Deletes a person attribute type by ID</li>
 * </ul>
 *
 * <p>Uses {@link PersonAttributeTypeService} to perform operations
 * and {@link PersonAttributeTypeDto} as the data transfer object.</p>
 *
 * <p>Author: Ikenumah</p>
 */
@Tag(name = "Person Attribute Type")
@RestController
@RequestMapping(Constants.PERSON_ATTRIBUTE_TYPE_BASE_URL)
@SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"},
        justification = "Dependency injection by Spring; safe to store")
@AllArgsConstructor
public class PersonAttributeTypeController {

    private final PersonAttributeTypeService personAttributeTypeService;

    /**
     * Retrieves a person attribute type by its unique identifier.
     *
     * @param id the unique identifier of the person attribute type to be retrieved
     * @return a {@link ResponseEntity} containing the {@link PersonAttributeTypeDto} for the specified id
     */
    @Operation(summary = "Get Person Attribute Type REST API Endpoint",
            description = "Endpoint to fetch a person attribute type record.")
    @ApiResponse(responseCode = "200", description = "Http Status OK", content = @Content(
            schema = @Schema(implementation = PersonAttributeTypeDto.class)
        ))
    @GetMapping("/{id}")
    public ResponseEntity<PersonAttributeTypeDto> getPersonAttributeTypeById(@PathVariable int id) {
        return ResponseEntity.ok(personAttributeTypeService.getPersonAttributeTypeById(id));
    }

    /**
     * Retrieves all person attribute types.
     *
     * @return a {@link ResponseEntity} containing a list of {@link PersonAttributeTypeDto} objects
     */
    @Operation(summary = "Get All Person Attribute Types REST API Endpoint",
            description = "Endpoint to fetch all person attribute types.")
    @ApiResponse(responseCode = "200", description = "Http Status OK", content = @Content(
            schema = @Schema(implementation = PersonAttributeTypeDto.class)
        ))
    @GetMapping
    public ResponseEntity<List<PersonAttributeTypeDto>> getAllPersonAttributeTypes() {
        return ResponseEntity.ok(personAttributeTypeService.getAllPersonAttributeTypes());
    }

    /**
     * Saves a person attribute type to the database.
     *
     * @param personAttributeTypeDto the person attribute type to be saved
     * @return a {@link ResponseEntity} containing the saved {@link PersonAttributeTypeDto}
     */
    @Operation(summary = "Save Person Attribute Type REST API Endpoint",
            description = "Endpoint to save a person attribute type.")
    @ApiResponse(responseCode = "200", description = "Http Status OK", content = @Content(
            schema = @Schema(implementation = PersonAttributeTypeDto.class)
        ))
    @PostMapping
    public ResponseEntity<PersonAttributeTypeDto> savePersonAttributeType(
            @RequestBody PersonAttributeTypeDto personAttributeTypeDto) {
        return new ResponseEntity<>(personAttributeTypeService
                .savePersonAttributeType(personAttributeTypeDto), HttpStatus.CREATED);
    }

    /**
     * Updates a person attribute type in the database.
     *
     * @param id the id of the person attribute type to be updated
     * @param personAttributeTypeDto the person attribute type to be updated
     * @return a {@link ResponseEntity} containing the updated {@link PersonAttributeTypeDto}
     */
    @Operation(summary = "Update Person Attribute Type REST API Endpoint",
            description = "Endpoint to update a person attribute type.")
    @ApiResponse(responseCode = "200", description = "Http Status OK", content = @Content(
            schema = @Schema(implementation = PersonAttributeTypeDto.class)
        ))
    @PutMapping("/{id}")
    public ResponseEntity<PersonAttributeTypeDto> updatePersonAttributeType(
            @PathVariable int id,
            @RequestBody PersonAttributeTypeDto personAttributeTypeDto) {
        return ResponseEntity.ok(personAttributeTypeService.updatePersonAttributeType(id, personAttributeTypeDto));
    }

    /**
     * Deletes a person attribute type from the database.
     *
     * @param id the id of the person attribute type to be deleted
     * @param voidReason the reason for voiding the person attribute type
     */
    @Operation(summary = "Delete Person Attribute Type REST API Endpoint",
            description = "Endpoint to delete a person attribute type.")
    @ApiResponse(responseCode = "200", description = "Http Status OK")
    @DeleteMapping("/{id}")
    public void deletePersonAttributeType(@PathVariable int id, @RequestBody RecordVoidRequest voidReason) {
        personAttributeTypeService.deletePersonAttributeType(id, voidReason);
    }
}
