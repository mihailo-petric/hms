package com.hyperoptic.hms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * EmployeeDTO is a Data Transfer Object (DTO) that represents an employee's data.
 * It is used to transfer employee information between different layers of the application.
 * This class contains fields for personal ID, name, team, and team lead.
 * The personal ID is used to uniquely identify an employee.
 */
@Data
public class EmployeeDTO {

  @Schema(description = "Employee ID", example = "123456")
  private Long personalId;

  @NotBlank(message = "name is mandatory")
  @Size(max = 100, message = "name should not exceed 100 characters")
  @Schema(description = "Employee name", example = "John Doe")
  private String name;

  @NotBlank(message = "team is mandatory")
  @Size(max = 100, message = "team should not exceed 100 characters")
  @Schema(description = "Employee team", example = "Development")
  private String team;

  @NotBlank(message = "teamLead is mandatory")
  @Size(max = 100, message = "teamLead should not exceed 100 characters")
  @Schema(description = "Employees team lead", example = "Jane Smith")
  private String teamLead;
}
