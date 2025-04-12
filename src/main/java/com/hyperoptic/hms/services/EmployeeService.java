package com.hyperoptic.hms.services;

import com.hyperoptic.hms.dto.EmployeeDTO;
import org.springframework.data.domain.Page;

public interface EmployeeService {

  EmployeeDTO createEmployee(EmployeeDTO employeeDTO);

  EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

  void deleteEmployee(Long id);

  EmployeeDTO getEmployeeById(Long id);

  Page<EmployeeDTO> getAllEmployees(int page, int size);

  Page<EmployeeDTO> searchEmployees(String name, String team, String teamLead, int page, int size);
}
