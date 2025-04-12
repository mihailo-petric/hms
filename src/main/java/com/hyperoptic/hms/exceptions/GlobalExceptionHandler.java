package com.hyperoptic.hms.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmployeeNotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleEmployeeNotFoundException(
      EmployeeNotFoundException ex) {
    var body = parseExceptionToResponseBody(ex);
    return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(CreateEmployeeException.class)
  public ResponseEntity<Map<String, Object>> handleCreateEmployeeException(
      CreateEmployeeException ex) {
    var body = parseExceptionToResponseBody(ex);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    var errors = extractErrorsFromFields(ex);
    var body = addErrorsToResponseBody(errors);
    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
    log.error(ex.getMessage(), ex);
    var body = parseExceptionToResponseBody(ex);
    return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private Map<String, Object> parseExceptionToResponseBody(Exception ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", ex.getMessage());
    return addErrorsToResponseBody(errors);
  }

  private Map<String, String> extractErrorsFromFields(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              String fieldName = ((FieldError) error).getField();
              String errorMessage = error.getDefaultMessage();
              errors.put(fieldName, errorMessage);
            });
    return errors;
  }

  private Map<String, Object> addErrorsToResponseBody(Map<String, String> errors) {
    Map<String, Object> body = new HashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("errors", errors);
    return body;
  }
}
