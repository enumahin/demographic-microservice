package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.helpers.Constants;
import com.alienworkspace.cdr.demographic.service.PersonService;
import com.alienworkspace.cdr.demographic.service.impl.PersonServiceImpl;
import com.alienworkspace.cdr.model.dto.person.PersonAddressDto;
import com.alienworkspace.cdr.model.dto.person.PersonAttributeDto;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
import com.alienworkspace.cdr.model.helper.ErrorResponseDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


/**
 * Controller for managing person-related operations in the demographic module.
 * Provides endpoints for CRUD operations on Person entities.
 *
 * <p>Endpoints:</p>
 * <ul>
 *   <li>{@code GET /demographic/person} - Retrieves all persons</li>
 *   <li>{@code GET /demographic/person/{id}} - Retrieves a person by ID</li>
 *   <li>{@code POST /demographic/person} - Adds a new person</li>
 *   <li>{@code PUT /demographic/person} - Updates a person</li>
 *   <li>{@code DELETE /demographic/person/{id}} - Deletes a person by ID</li>
 * </ul>
 *
 * <p>Uses {@link PersonServiceImpl} to perform operations and {@link PersonDto} as the data transfer object.</p>
 *
 * <p>Author: Codeium Engineering Team</p>
 */
@RestController
@RequestMapping(Constants.PERSON_BASE_URL)
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Dependency injection by Spring; safe to store")
@Tag(
        name = "PersonController",
        description = "CRUD operations related to Person entities"
)
public class PersonController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    /**
     * Constructs a new {@code PersonController} instance with the given {@link PersonServiceImpl}.
     *
     * @param personService the service to use for performing operations
     */
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Retrieves all persons.
     *
     * @return a list of all persons as PersonDto objects
     */
    @Operation(
            summary = "Get Persons REST API Endpoint",
            description = "Endpoint to fetch all persons record."
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @GetMapping
    public ResponseEntity<List<PersonDto>> getPersons() {
        return ResponseEntity.ok(personService.getPersons());
    }

    /**
     * Retrieves a person by their ID.
     *
     * @param id the ID of the person to retrieve
     * @return the person with the specified ID as a PersonDto
     */
    @Operation(
            summary = "Get Person REST API Endpoint",
            description = "Endpoint to fetch a person's record."
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @GetMapping("{id}/{includeVoided}")
    public ResponseEntity<PersonDto> getPerson(@RequestHeader("X-cdr-correlation-id") String correlationId,
                                               @PathVariable("id") long id, @PathVariable boolean includeVoided) {
        log.debug("Retrieving person with ID: {} with correlationId: {}", id, correlationId);
        return ResponseEntity.ok(personService.getPerson(correlationId, id, includeVoided));
    }

    /**
     * Adds a new person.
     *
     * @param personDto the data transfer object containing the details of the person to add
     * @return the added person as a PersonDto
     */
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create Person REST API Endpoint",
            description = "Endpoint to create new person record."
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @PostMapping
    public PersonDto addPerson(@RequestHeader("X-cdr-correlation-id") String correlationId,
                               @Valid @RequestBody PersonDto personDto) {
        log.debug("Adding person with correlationId: {}", correlationId);
        return personService.addPerson(personDto, correlationId);
    }

    /**
     * Updates an existing person.
     *
     * @param personDto the data transfer object containing the updated details of the person
     * @return the updated person as a PersonDto
     */
    @Operation(
            summary = "Update Person REST API Endpoint",
            description = "Endpoint to update a person's record."
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Http Status NotFound",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @PutMapping("/{id}")
    public ResponseEntity<PersonDto> updatePerson(@RequestHeader("X-cdr-correlation-id") String correlationId,
                                                  @PathVariable("id") Long id,
                                                  @Valid @RequestBody PersonDto personDto) {
        log.debug("Updating person with ID: {} with correlationId: {}", id, correlationId);
        return ResponseEntity.ok(personService.updatePerson(id, personDto, correlationId));
    }

    /**
     * Deletes a person by their ID.
     *
     * @param voidRequest the request containing the ID of the person to delete and the reason for deletion
     * @return a message indicating the successful deletion of the person
     */
    @Operation(
            summary = "Delete Person REST API Endpoint",
            description = "Endpoint to delete a person."
    )

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Http Status NotFound",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deletePerson(@PathVariable("id") long id,
                                                    @RequestBody RecordVoidRequest voidRequest) {
        return ResponseEntity.ok(personService.deletePerson(id, voidRequest));
    }

    /**
     * Adds a new person name.
     *
     * @param personId the ID of the person to add the name to
     * @param personNameDto the data transfer object containing the details of the person name to add
     * @return the added person name as a PersonNameDto
     */
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create Person Name REST API Endpoint",
            description = "Endpoint to create new person name record.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @PostMapping("/{personId}/names")
    public ResponseEntity<PersonNameDto> addPersonName(@PathVariable("personId") long personId,
                                                   @Valid @RequestBody PersonNameDto personNameDto) {
        return ResponseEntity.ok(personService.addPersonName(personId, personNameDto));
    }

    /**
     * Updates an existing person name.
     *
     * @param personId the ID of the person to update the name for
     * @param personNameId the ID of the person name to update
     * @param preferred the preferred name
     * @return the updated person name as a PersonNameDto
     */
    @Operation(
            summary = "Update Person Name REST API Endpoint",
            description = "Endpoint to update a person name record.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @PutMapping("/{personId}/names/{personNameId}")
    public ResponseEntity<PersonNameDto> updatePersonName(@PathVariable("personId") long personId,
                                                      @PathVariable("personNameId") long personNameId,
                                                      @RequestBody boolean preferred) {
        return ResponseEntity.ok(personService.updatePersonName(personId, personNameId, preferred));
    }

    /**
     * Deletes a person name by its ID.
     *
     * @param personId the ID of the person to delete the name from
     * @param personNameId the ID of the person name to delete
     * @param voidRequest the request containing the ID of the person name to delete
     */
    @Operation(
            summary = "Delete Person Name REST API Endpoint",
            description = "Endpoint to delete a person name record.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @DeleteMapping("/{personId}/names/{personNameId}")
    public void deletePersonName(@PathVariable("personId") long personId,
                                 @PathVariable("personNameId") long personNameId,
                                 @RequestBody RecordVoidRequest voidRequest) {
        personService.deletePersonName(personId, personNameId, voidRequest);
    }

    /**
     * Adds a new person address.
     *
     * @param personId the ID of the person to add the address to
     * @param personAddressDto the data transfer object containing the details of the person address to add
     * @return the added person address as a PersonAddressDto
     */
    @Operation(
            summary = "Create Person Address REST API Endpoint",
            description = "Endpoint to create new person address record.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{personId}/addresses")
    public ResponseEntity<PersonAddressDto> addPersonAddress(
            @RequestHeader("X-cdr-correlation-id") String correlationId,
            @PathVariable("personId") long personId,
            @Valid @RequestBody PersonAddressDto personAddressDto) {
        log.info("Correlation ID: {}", correlationId);
        return ResponseEntity.ok(personService.addAddress(personId, personAddressDto, correlationId));
    }

    /**
     * Updates an existing person address.
     *
     * @param personId the ID of the person to update the address for
     * @param personAddressId the ID of the person address to update
     * @param preferred the preferred address
     * @return the updated person address as a PersonAddressDto
     */
    @Operation(
            summary = "Update Person Address REST API Endpoint",
            description = "Endpoint to update a person address record.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @PutMapping("/{personId}/addresses/{personAddressId}")
    public ResponseEntity<PersonAddressDto> updatePersonAddress(
                                                         @RequestHeader("X-cdr-correlation-id") String correlationId,
                                                         @PathVariable("personId") long personId,
                                                         @PathVariable("personAddressId") long personAddressId,
                                                         @RequestBody boolean preferred) {
        log.info("Correlation ID: {}", correlationId);
        return ResponseEntity.ok(personService.updateAddress(personId, personAddressId, preferred, correlationId));
    }

    /**
     * Retrieves a person address by its ID.
     *
     * @param personId the ID of the person to retrieve the address from
     * @param personAddressId the ID of the person address to retrieve
     * @return the person address with the specified ID as a PersonAddressDto
     */
    @Operation(
            summary = "Get Person Address REST API Endpoint",
            description = "Endpoint to get a person address record.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @GetMapping("/{personId}/addresses/{personAddressId}")
    public ResponseEntity<PersonAddressDto> getPersonAddress(@PathVariable("personId") long personId,
                                    @PathVariable("personAddressId") long personAddressId) {
        return ResponseEntity.ok().body(personService.getPersonAddress(personId, personAddressId));
    }

    /**
     * Retrieves all person addresses for a person.
     *
     * @param personId the ID of the person to retrieve addresses for
     * @return a list of all person addresses for the specified person as a Set of PersonAddressDto objects
     */
    @Operation(
            summary = "Get Person Addresses REST API Endpoint",
            description = "Endpoint to get all person addresses for a person.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @GetMapping("/{personId}/addresses")
    public ResponseEntity<Set<PersonAddressDto>> getPersonAddresses(@PathVariable("personId") long personId) {
        return ResponseEntity.ok().body(personService.getPersonAddresses(personId));
    }

    /**
     * Deletes a person address by its ID.
     *
     * @param personId the ID of the person to delete the address from
     * @param personAddressId the ID of the person address to delete
     * @param voidRequest the request containing the ID of the person address to delete
     */
    @Operation(
            summary = "Delete Person Address REST API Endpoint",
            description = "Endpoint to delete a person address record.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @DeleteMapping("/{personId}/addresses/{personAddressId}")
    public void deletePersonAddress(@PathVariable("personId") long personId,
                                    @PathVariable("personAddressId") long personAddressId,
                                    @RequestBody RecordVoidRequest voidRequest) {
        personService.deleteAddress(personId, personAddressId, voidRequest);
    }

    /**
     * Adds a new person attribute.
     *
     * @param personId the ID of the person to add the attribute to
     * @param personAttributeDto the data transfer object containing the details of the person attribute to add
     * @return the added person attribute as a PersonAttributeDto
     */
    @Operation(
            summary = "Create Person Attribute REST API Endpoint",
            description = "Endpoint to create new person attribute record.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{personId}/attributes")
    public ResponseEntity<PersonAttributeDto> addPersonAttribute(@PathVariable("personId") long personId,
                                                        @Valid @RequestBody PersonAttributeDto personAttributeDto) {
        return ResponseEntity.ok(personService.addAttribute(personId, personAttributeDto));
    }

    /**
     * Updates an existing person attribute.
     *
     * @param personId the ID of the person to update the attribute for
     * @param personAttributeId the ID of the person attribute to update
     * @param preferred the preferred attribute
     * @return the updated person attribute as a PersonAttributeDto
     */
    @Operation(
            summary = "Update Person Attribute REST API Endpoint",
            description = "Endpoint to update a person attribute record.")

    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @PutMapping("/{personId}/attributes/{personAttributeId}")
    public ResponseEntity<PersonAttributeDto> updatePersonAttribute(@PathVariable("personId") long personId,
                                                           @PathVariable("personAttributeId") long personAttributeId,
                                                           @RequestBody boolean preferred) {
        return ResponseEntity.ok(personService.updateAttribute(personId, personAttributeId, preferred));
    }

    /**
     * Deletes a person attribute by its ID.
     *
     * @param personId the ID of the person to delete the attribute from
     * @param personAttributeId the ID of the person attribute to delete
     * @param voidRequest the request containing the ID of the person attribute to delete
     */
    @Operation(
            summary = "Delete Person Attribute REST API Endpoint",
            description = "Endpoint to delete a person attribute record.")
    @ApiResponse(
            responseCode = "200",
            description = "Http Status OK",
            content = @Content(
                    schema = @Schema(implementation = ResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "500",
            description = "Http Status INTERNAL_SERVER_ERROR",
            content = @Content(
                    schema = @Schema(implementation = ErrorResponseDto.class)
            )
    )
    @DeleteMapping("/{personId}/attributes/{personAttributeId}")
    public void deletePersonAttribute(@PathVariable("personId") long personId,
                                      @PathVariable("personAttributeId") long personAttributeId,
                                      @RequestBody RecordVoidRequest voidRequest) {
        personService.deleteAttribute(personId, personAttributeId, voidRequest);
    }
}
