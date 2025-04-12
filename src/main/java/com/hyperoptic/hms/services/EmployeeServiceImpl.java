package com.hyperoptic.hms.services;

import com.hyperoptic.hms.dto.EmployeeDTO;
import com.hyperoptic.hms.entity.Employee;
import com.hyperoptic.hms.exceptions.CreateEmployeeException;
import com.hyperoptic.hms.exceptions.EmployeeNotFoundException;
import com.hyperoptic.hms.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final ModelMapper modelMapper;

  @Override
  public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
    log.debug("Creating employee with details: {}", employeeDTO);
    if (employeeDTO.getPersonalId() != null) {
      throw new CreateEmployeeException();
    }
    Employee employee = modelMapper.map(employeeDTO, Employee.class);
    Employee savedEmployee = employeeRepository.save(employee);
    return modelMapper.map(savedEmployee, EmployeeDTO.class);
  }

  @Override
  public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
    Employee employee =
        employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    log.debug("Updating employee with details: {}", employee);
    employeeDTO.setPersonalId(employee.getPersonalId());
    employee = modelMapper.map(employeeDTO, Employee.class);
    Employee updatedEmployee = employeeRepository.save(employee);
    return modelMapper.map(updatedEmployee, EmployeeDTO.class);
  }

  @Override
  public void deleteEmployee(Long id) {
    employeeRepository.deleteById(id);
  }

  @Override
  public EmployeeDTO getEmployeeById(Long id) {
    log.debug("Fetching employee with ID: {}", id);
    Employee employee =
        employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));
    return modelMapper.map(employee, EmployeeDTO.class);
  }

  @Override
  public Page<EmployeeDTO> getAllEmployees(int page, int size) {
    log.debug("Fetching all employees");
    Pageable pageable = PageRequest.of(page, size);
    Page<Employee> employees = employeeRepository.findAll(pageable);
    return employees.map(employee -> modelMapper.map(employee, EmployeeDTO.class));
  }

  @Override
  public Page<EmployeeDTO> searchEmployees(
      String name, String team, String teamLead, int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Employee> employees =
        employeeRepository.findByNameOrTeamOrTeamLead(name, team, teamLead, pageable);
    return employees.map(employee -> modelMapper.map(employee, EmployeeDTO.class));
  }
}
