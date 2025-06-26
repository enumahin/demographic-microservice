package com.alienworkspace.cdr.demographic.exception;

import com.alienworkspace.cdr.model.helper.ErrorResponseDto;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global Exception Handler.
 * With the @ControllerAdvice annotation, and exception that is thrown in any controller
 * will be forwarded to this exception handler
 * Extend ResponseEntityExceptionHandler to have access to endpoint input validations
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles global exceptions that occur in the application. This class uses the
     * {@link ResponseEntityExceptionHandler} to handle exceptions that occur at the
     * controller level.
     *
     * @param exception the exception that was thrown
     * @param webRequest the current web request during which the exception was thrown
     * @return a ResponseEntity containing an ErrorResponseDto with details of the error
     * @author Ikenumah (enumahinm@gmail.com)
     */
    @ExceptionHandler(CustomerAlreadyExistException.class)
    public ResponseEntity<ErrorResponseDto> handleCustomerAlreadyExistsException(
            CustomerAlreadyExistException exception,
            WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .apiPath(webRequest.getDescription(false))
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link CustomerAlreadyExistException} exceptions. This exception is
     * thrown when a customer already exists in the database.
     *
     * @param exception the exception thrown
     * @param webRequest the web request
     * @return an {@link ErrorResponseDto} containing the error code, error message,
     *     path of the API, and the timestamp of the error
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                            WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .apiPath(webRequest.getDescription(false))
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link BadRequestException} exceptions. This exception is thrown when a
     * customer already exists in the database.
     *
     * @param exception the exception thrown
     * @param webRequest the web request
     * @return an {@link ErrorResponseDto} containing the error code, error message,
     *     path of the API, and the timestamp of the error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleBadRequestException(IllegalArgumentException exception,
                                                                            WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .apiPath(webRequest.getDescription(false))
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles global exceptions that occur in the application.
     *
     * <p>This method handles any exception that is not specifically handled by another
     * exception handler in this class. It provides a generic error response for unknown
     * exceptions.
     *
     * @param exception the exception that was thrown
     * @param webRequest the current web request during which the exception was thrown
     * @return a ResponseEntity containing an ErrorResponseDto with details of the error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                               WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .apiPath(webRequest.getDescription(false))
                .errorMessage(exception.getMessage())
                .errorTime(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String validationMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMessage);
        });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }
}
