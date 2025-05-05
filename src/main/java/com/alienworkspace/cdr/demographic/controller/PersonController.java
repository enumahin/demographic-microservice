package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.service.PersonService;
import com.alienworkspace.cdr.demographic.service.impl.PersonServiceImpl;
import com.alienworkspace.cdr.model.dto.person.PersonDto;
import com.alienworkspace.cdr.model.helper.ErrorResponseDto;
import com.alienworkspace.cdr.model.helper.RecordVoidRequest;
import com.alienworkspace.cdr.model.helper.ResponseDto;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@RequestMapping("demographic/person")
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Dependency injection by Spring; safe to store")
@Tag(
        name = "PersonController",
        description = "CRUD operations related to Person entities"
)
public class PersonController {

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
    @ApiResponses(
            {
                @ApiResponse(
                    responseCode = "200",
                    description = "Http Status OK",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
                ),
                @ApiResponse(
                    responseCode = "500",
                    description = "Http Status INTERNAL_SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
                )
            }
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
    @ApiResponses(
            {
                @ApiResponse(
                    responseCode = "200",
                    description = "Http Status OK",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
                ),
                @ApiResponse(
                    responseCode = "500",
                    description = "Http Status INTERNAL_SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
                )
            }
    )
    @GetMapping("{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personService.getPerson(id));
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
    @ApiResponses(
            {
                @ApiResponse(
                    responseCode = "200",
                    description = "Http Status OK",
                    content = @Content(
                            schema = @Schema(implementation = ResponseDto.class)
                    )
                ),
                @ApiResponse(
                    responseCode = "500",
                    description = "Http Status INTERNAL_SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
                )
            }
    )
    @PostMapping
    public PersonDto addPerson(@Valid @RequestBody PersonDto personDto) {
        return personService.addPerson(personDto);
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
    @ApiResponses(
            {
                @ApiResponse(
                        responseCode = "200",
                        description = "Http Status OK",
                        content = @Content(
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Http Status NotFound",
                        content = @Content(
                                schema = @Schema(implementation = ErrorResponseDto.class)
                        )
                ),
                @ApiResponse(
                    responseCode = "500",
                    description = "Http Status INTERNAL_SERVER_ERROR",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
                )
            }
    )
    @PutMapping
    public ResponseEntity<PersonDto> updatePerson(@RequestBody PersonDto personDto) {
        return ResponseEntity.ok(personService.updatePerson(personDto));
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
    @ApiResponses(
            {
                @ApiResponse(
                        responseCode = "200",
                        description = "Http Status OK",
                        content = @Content(
                                schema = @Schema(implementation = ResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Http Status NotFound",
                        content = @Content(
                                schema = @Schema(implementation = ErrorResponseDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Http Status INTERNAL_SERVER_ERROR",
                        content = @Content(
                                schema = @Schema(implementation = ErrorResponseDto.class)
                        )
                )
            }
    )
    @DeleteMapping
    public ResponseEntity<ResponseDto> deletePerson(@RequestBody RecordVoidRequest voidRequest) {
        return ResponseEntity.ok(personService.deletePerson(voidRequest));
    }
}
