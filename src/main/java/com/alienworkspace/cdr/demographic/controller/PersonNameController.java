package com.alienworkspace.cdr.demographic.controller;

import com.alienworkspace.cdr.demographic.service.PersonNameService;
import com.alienworkspace.cdr.demographic.service.impl.PersonNameServiceImpl;
import com.alienworkspace.cdr.model.dto.person.PersonNameDto;
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
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller that handles CRUD operations related to Person name entities.
 *
 * <p>Endpoints:</p>
 * <ul>
 *   <li>{@code GET /demographic/person-name} - Retrieves all person names</li>
 *   <li>{@code GET /demographic/person-name/{id}} - Retrieves a person name by ID</li>
 *   <li>{@code POST /demographic/person-name} - Adds a new person name</li>
 *   <li>{@code PUT /demographic/person-name} - Updates a person name</li>
 *   <li>{@code DELETE /demographic/person-name/{id}} - Deletes a person name by ID</li>
 * </ul>
 *
 * <p>Author: Ikenumah (enumahinm@gmail.com)</p>
 */
@RestController
@RequestMapping("demographic/person-name")
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Dependency injection by Spring; safe to store")
@Tag(
        name = "PersonNameController",
        description = "CRUD operations related to Person name entities"
)
public class PersonNameController {

    private final PersonNameService personNameService;

    /**
     * Constructs a new {@code PersonNameController} instance with the given {@link PersonNameServiceImpl}.
     *
     * @param personNameService the service to use for performing operations
     */
    public PersonNameController(PersonNameService personNameService) {
        this.personNameService = personNameService;
    }

    /**
     * Retrieves all person names.
     *
     * @return a list of all person names as {@link PersonNameDto} objects
     */
    @Operation(
            summary = "Get person names REST API Endpoint",
            description = "Endpoint to fetch all person names record by person ID."
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
                    description = "Http Status NOT FOUND",
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
    @GetMapping("/{personId}/names")
    public ResponseEntity<List<PersonNameDto>> getPersonNamsByPersonId(@PathVariable("personId") Long personId) {
        return ResponseEntity.ok(personNameService.findPersonNamesByPersonId(personId));
    }

    /**
     * Retrieves a person name by record ID.
     *
     * @param id the ID of the person name to retrieve
     * @return the person name with the specified ID as a PersonNameDto
     */
    @Operation(
            summary = "Get person name REST API Endpoint",
            description = "Endpoint to fetch a person name record."
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
                            description = "Http Status NOT FOUND",
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
    @GetMapping("{id}")
    public ResponseEntity<PersonNameDto> getPersonName(@PathVariable("id") Long id) {
        return ResponseEntity.ok(personNameService.findPersonName(id));
    }

    /**
     * Retrieves a person name by record ID.
     *
     * @param id the ID of the person name to retrieve
     * @return the person name with the specified ID as a PersonNameDto
     */
    @Operation(
            summary = "Get person name REST API Endpoint",
            description = "Endpoint to fetch a person name record."
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
                            description = "Http Status NOT FOUND",
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
    @GetMapping("{id}/preferred")
    public ResponseEntity<PersonNameDto> getPreferredPersonName(@PathVariable("id") Long id) {
        return personNameService.findPreferredNameByPersonId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Adds a new person name.
     *
     * @param personNameDto the data transfer object containing the details of the person name to add
     * @return the added person name as a PersonNameDto
     */
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Add person name REST API Endpoint",
            description = "Endpoint to create add a person name record."
    )
    @ApiResponses(
            {
                @ApiResponse(
                            responseCode = "201",
                            description = "Http Status CREATED",
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
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public PersonNameDto addPerson(@Valid @RequestBody PersonNameDto personNameDto) {
        PersonNameDto personNameDto1 = personNameService.addPersonName(personNameDto);
        System.out.println(personNameDto1);
        return personNameDto1;
    }

    /**
     * Deletes a person name by record ID.
     *
     * @param voidRequest the request containing the ID of the person name to delete and the reason for deletion
     * @return a message indicating the successful deletion of the person name record
     */
    @Operation(
            summary = "Delete a person name REST API Endpoint",
            description = "Endpoint to delete a person name record."
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
        return Optional.ofNullable(personNameService.deletePersonName(voidRequest))
                .map(response -> new ResponseDto(200, response))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().body(
                        new ResponseDto(400, "Error Deleting Person Name")));
    }
}
