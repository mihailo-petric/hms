package com.hyperoptic.hms.services;

import com.hyperoptic.hms.dto.EmployeeDTO;
import org.springframework.data.domain.Page;

/**
 * EmployeeService interface for managing employee-related operations. This interface defines
 * methods for creating, updating, deleting, and retrieving employee information. It also includes
 * methods for paginated retrieval and searching of employees based on various criteria.
 */
public interface EmployeeService {

  EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

  EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

  void deleteEmployee(Long id);

  EmployeeDTO getEmployeeById(Long id);

  Page<EmployeeDTO> getAllEmployees(int page, int size);

  Page<EmployeeDTO> searchEmployees(String name, String team, String teamLead, int page, int size);
}
