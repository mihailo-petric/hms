package com.hyperoptic.hms.rest;

import com.hyperoptic.hms.dto.EmployeeDTO;
import com.hyperoptic.hms.services.EmployeeService;
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
  public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
    EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
  }

  @PutMapping("/{id}")
  public ResponseEntity<EmployeeDTO> updateEmployee(
      @PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
    EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
    return ResponseEntity.ok(updatedEmployee);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
    EmployeeDTO employeeDTO = employeeService.getEmployeeById(id);
    return ResponseEntity.ok(employeeDTO);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    Page<EmployeeDTO> employees = employeeService.getAllEmployees(page, size);
    return ResponseEntity.ok(employees);
  }

  @GetMapping("/search")
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
