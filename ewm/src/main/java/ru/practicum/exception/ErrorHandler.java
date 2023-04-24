package ru.practicum.exception;

import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.exception.duplicate.CategoryNameDuplicateException;
import ru.practicum.exception.duplicate.EmailDuplicateException;
import ru.practicum.exception.duplicate.RequestDuplicateException;
import ru.practicum.exception.notfound.NotFoundException;
import ru.practicum.exception.time.ToEarlyEventStart1HException;
import ru.practicum.exception.time.ToEarlyEventStart2HException;
import ru.practicum.exception.unavailable.EventUnavailableException;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    private static final String INCORRECT_REQUEST = "Incorrectly made request.";
    private static final String INTEGRITY_CONSTRAINT = "Integrity constraint has been violated.";
    private static final String OBJECT_NOT_FOUND = "The required object was not found.";
    private static final String REQUEST_OPERATION_CONDITION = "For the requested operation the conditions are not met.";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleJsonParseException(final JsonParseException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST),
                INCORRECT_REQUEST,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse(
                String.valueOf(HttpStatus.BAD_REQUEST),
                INCORRECT_REQUEST,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolationExceptionJavax(final ConstraintViolationException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse(
                "BAD_REQUEST",
                INCORRECT_REQUEST,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConstraintViolationExceptionHibernate(final org.hibernate.exception.ConstraintViolationException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(
                "CONFLICT",
                INTEGRITY_CONSTRAINT,
                e.getCause().getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlePropertyValueException(final PropertyValueException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse(
                "BAD_REQUEST",
                INCORRECT_REQUEST,
                e.getCause().getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleEntityExistsException(final EntityExistsException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse(
                "BAD_REQUEST",
                INCORRECT_REQUEST,
                e.getCause().getMessage(),
                LocalDateTime.now());
    }

    /*@ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.warn("400 {}", e.getMessage(), e);
        return new ErrorResponse(
                "BAD_REQUEST",
                INCORRECT_REQUEST,
                e.getCause().getMessage(),
                LocalDateTime.now());
    }*/


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(final NotFoundException e) {
        log.warn("404 {}", e.getMessage(), e);
        return new ErrorResponse(
                "NOT_FOUND",
                OBJECT_NOT_FOUND,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailDuplicateException(final EmailDuplicateException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(
                "CONFLICT",
                INTEGRITY_CONSTRAINT,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleCategoryNameDuplicateException(final CategoryNameDuplicateException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(
                "CONFLICT",
                INTEGRITY_CONSTRAINT,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleExistRelatedDataException(final ExistRelatedDataException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(
                "CONFLICT",
                REQUEST_OPERATION_CONDITION,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleToEarlyEventStart2HException(final ToEarlyEventStart2HException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(
                "CONFLICT",
                REQUEST_OPERATION_CONDITION,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleToEarlyEventStart1HException(final ToEarlyEventStart1HException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(
                "CONFLICT",
                REQUEST_OPERATION_CONDITION,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleRequestDuplicateException(final RequestDuplicateException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(
                "CONFLICT",
                REQUEST_OPERATION_CONDITION,
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEventUnavailableException(final EventUnavailableException e) {
        log.warn("409 {}", e.getMessage(), e);
        return new ErrorResponse(
                "FORBIDDEN",
                REQUEST_OPERATION_CONDITION,
                e.getMessage(),
                LocalDateTime.now());
    }
}
