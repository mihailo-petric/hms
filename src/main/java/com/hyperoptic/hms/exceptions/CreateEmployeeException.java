package com.hyperoptic.hms.exceptions;

public class CreateEmployeeException extends RuntimeException {

  public CreateEmployeeException() {
    super("personalId should not be provided for new employee");
  }
}
