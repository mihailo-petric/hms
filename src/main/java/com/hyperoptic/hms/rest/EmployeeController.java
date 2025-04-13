package com.hyperoptic.hms.rest;

import com.hyperoptic.hms.dto.EmployeeDTO;
import com.hyperoptic.hms.services.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

  private final EmployeeService employeeService;

  @PostMapping
  @Operation(
      summary = "Create a new employee",
      description = "Creates a new employee with the provided details.")
  public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
    EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Update an existing employee",
      description = "Updates the details of an existing employee.")
  public ResponseEntity<EmployeeDTO> updateEmployee(
      @PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
    EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
    return ResponseEntity.ok(updatedEmployee);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get an employee by ID",
      description = "Retrieves the details of an employee by their ID.")
  public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
    EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
    return ResponseEntity.ok(employeeDTO);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an employee by ID", description = "Deletes an employee by their ID.")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Operation(
      summary = "Get all employees with pagination",
      description = "Retrieves a paginated list of all employees.")
  public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    Page<EmployeeDTO> employees = employeeService.getAllEmployees(page, size);
    return ResponseEntity.ok(employees);
  }

  @GetMapping("/search")
  @Operation(
      summary = "Search employees by name, team, or team lead",
      description = "Searches for employees based on their name, team, or team lead.")
  public ResponseEntity<Page<EmployeeDTO>> searchEmployees(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String team,
      @RequestParam(required = false) String teamLead,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Page<EmployeeDTO> employees = employeeService.searchEmployees(name, team, teamLead, page, size);
    return ResponseEntity.ok(employees);
  }
}
