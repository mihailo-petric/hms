package com.hyperoptic.hms.exceptions;

public class EmployeeNotFoundException extends RuntimeException {

  public EmployeeNotFoundException(Long id) {
    super("Employee not found with ID: " + id);
  }
}
