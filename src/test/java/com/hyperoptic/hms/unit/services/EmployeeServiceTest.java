package com.hyperoptic.hms.unit.services;

import static com.hyperoptic.hms.TestType.UNIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.hyperoptic.hms.dto.EmployeeDTO;
import com.hyperoptic.hms.entity.Employee;
import com.hyperoptic.hms.exceptions.CreateEmployeeException;
import com.hyperoptic.hms.exceptions.EmployeeNotFoundException;
import com.hyperoptic.hms.repository.EmployeeRepository;
import com.hyperoptic.hms.services.EmployeeService;
import com.hyperoptic.hms.services.EmployeeServiceImpl;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@Tag(UNIT)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

  private EmployeeService employeeService;

  @Mock private EmployeeRepository employeeRepository;

  @Mock private ModelMapper modelMapper;

  @BeforeEach
  void setUp() {
    employeeService = spy(new EmployeeServiceImpl(employeeRepository, modelMapper));
  }

  /*
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
    log.debug("Creating employee with details: {}", employeeDTO);
    if (employeeDTO.getPersonalId() != null) {
      throw new CreateEmployeeException();
    }
    Employee employee = modelMapper.map(employeeDTO, Employee.class);
    Employee savedEmployee = employeeRepository.save(employee);
    return modelMapper.map(savedEmployee, EmployeeDTO.class);
  }
    **/

  @Test
  void givenValidEmployeeDTO_whenCreateEmployee_thenReturnCreatedEmployee() {
    var employeeDTO = createEmployeeDTO();
    employeeDTO.setPersonalId(null);
    var employee = createEmployee();
    doReturn(employee).when(modelMapper).map(employeeDTO, Employee.class);
    doReturn(employee).when(employeeRepository).save(employee);
    doReturn(createEmployeeDTO()).when(modelMapper).map(employee, EmployeeDTO.class);

    var createdEmployee = employeeService.createEmployee(employeeDTO);

    assertThat(createdEmployee).isNotNull();
    assertThat(createdEmployee.getPersonalId()).isNotNull();
    assertThat(createdEmployee.getName()).isEqualTo(employeeDTO.getName());
    assertThat(createdEmployee.getTeam()).isEqualTo(employeeDTO.getTeam());
    assertThat(createdEmployee.getTeamLead()).isEqualTo(employeeDTO.getTeamLead());
  }

  @Test
  void givenInvalidEmployeeDTO_whenCreateEmployee_thenThrowException() {
    var employeeDTO = createEmployeeDTO();
    employeeDTO.setPersonalId(1L);

    assertThatThrownBy(() -> employeeService.createEmployee(employeeDTO))
        .isInstanceOf(CreateEmployeeException.class)
        .hasMessageContaining("personalId should not be provided for new employee");
  }

  @Test
  void givenValidEmployeeDTO_whenUpdateEmployee_thenReturnUpdatedEmployee() {
    var employeeDTO = createEmployeeDTO();
    var employee = createEmployee();
    var employeeOptional = Optional.of(employee);
    doReturn(employee).when(modelMapper).map(employeeDTO, Employee.class);
    doReturn(employeeOptional).when(employeeRepository).findById(1L);
    doReturn(employee).when(employeeRepository).save(employee);
    doReturn(createEmployeeDTO()).when(modelMapper).map(employee, EmployeeDTO.class);

    var updatedEmployee = employeeService.updateEmployee(1L, employeeDTO);

    assertThat(updatedEmployee).isNotNull();
    assertThat(updatedEmployee.getPersonalId()).isEqualTo(1L);
    assertThat(updatedEmployee.getName()).isEqualTo(employeeDTO.getName());
    assertThat(updatedEmployee.getTeam()).isEqualTo(employeeDTO.getTeam());
    assertThat(updatedEmployee.getTeamLead()).isEqualTo(employeeDTO.getTeamLead());
  }

  @Test
  void givenInvalidEmployeeId_whenUpdateEmployee_thenThrowException() {
    var employeeDTO = createEmployeeDTO();
    doReturn(Optional.empty()).when(employeeRepository).findById(1L);

    assertThatThrownBy(() -> employeeService.updateEmployee(1L, employeeDTO))
        .isInstanceOf(EmployeeNotFoundException.class)
        .hasMessageContaining("Employee not found with ID: 1");
  }

  @Test
  void givenValidEmployeeId_whenGetEmployeeById_thenReturnEmployee() {
    var employee = createEmployee();
    doReturn(Optional.of(employee)).when(employeeRepository).findById(1L);
    doReturn(createEmployeeDTO()).when(modelMapper).map(employee, EmployeeDTO.class);

    var foundEmployee = employeeService.getEmployeeById(1L);

    assertThat(foundEmployee).isNotNull();
    assertThat(foundEmployee.getPersonalId()).isEqualTo(1L);
    assertThat(foundEmployee.getName()).isEqualTo(employee.getName());
    assertThat(foundEmployee.getTeam()).isEqualTo(employee.getTeam());
    assertThat(foundEmployee.getTeamLead()).isEqualTo(employee.getTeamLead());
  }

  @Test
  void givenInvalidEmployeeId_whenGetEmployeeById_thenThrowException() {
    doReturn(Optional.empty()).when(employeeRepository).findById(1L);

    assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
        .isInstanceOf(EmployeeNotFoundException.class)
        .hasMessageContaining("Employee not found with ID: 1");
  }

  @Test
  void givenValidEmployeeId_whenDeleteEmployee_thenDeleteEmployee() {
    var employee = createEmployee();
    doReturn(Optional.of(employee)).when(employeeRepository).findById(1L);

    doNothing().when(employeeRepository).deleteById(1L);

    employeeService.deleteEmployee(1L);

    verify(employeeRepository, times(1)).deleteById(1L);
  }

  @Test
  void givenInvalidEmployeeId_whenDeleteEmployee_thenThrowException() {
    doReturn(Optional.empty()).when(employeeRepository).findById(1L);

    assertThatThrownBy(() -> employeeService.deleteEmployee(1L))
        .isInstanceOf(EmployeeNotFoundException.class)
        .hasMessageContaining("Employee not found with ID: 1");
  }

  @Test
  void givenValidPageAndSize_whenGetAllEmployees_thenReturnEmployeePage() {
    var pageable = PageRequest.of(0, 10);
    var employeePage = createEmployeePage();
    var employeeMap = createEmployeeMap();

    doReturn(employeePage).when(employeeRepository).findAll(pageable);
    employeePage.forEach(
        employee -> {
          var employeeDTO = employeeMap.get(employee);
          doReturn(employeeDTO).when(modelMapper).map(employee, EmployeeDTO.class);
        });

    var foundEmployee = employeeService.getAllEmployees(0, 10);

    assertThat(foundEmployee).isNotNull();
    assertThat(foundEmployee.getContent()).isNotEmpty();
    assertThat(foundEmployee.getTotalElements()).isEqualTo(employeeMap.size());
  }

  @Test
  void givenValidSearchCriteria_whenSearchEmployees_thenReturnEmployeePage() {
    var pageable = PageRequest.of(0, 10);
    var employeePage = createEmployeePage();
    var employeeMap = createEmployeeMap();

    doReturn(employeePage)
        .when(employeeRepository)
        .findByNameOrTeamOrTeamLead("John", "Development", "Mark", pageable);
    employeePage.forEach(
        employee -> {
          var employeeDTO = employeeMap.get(employee);
          doReturn(employeeDTO).when(modelMapper).map(employee, EmployeeDTO.class);
        });

    var foundEmployee = employeeService.searchEmployees("John", "Development", "Mark", 0, 10);

    assertThat(foundEmployee).isNotNull();
    assertThat(foundEmployee.getContent()).isNotEmpty();
    assertThat(foundEmployee.getTotalElements()).isEqualTo(employeeMap.size());
  }

  @Test
  void givenNoSearchCriteria_whenSearchEmployees_thenReturnAllEmployees() {
    var pageable = PageRequest.of(0, 10);
    var employeePage = createEmployeePage();
    var employeeMap = createEmployeeMap();

    doReturn(employeePage)
        .when(employeeRepository)
        .findByNameOrTeamOrTeamLead(null, null, null, pageable);
    employeePage.forEach(
        employee -> {
          var employeeDTO = employeeMap.get(employee);
          doReturn(employeeDTO).when(modelMapper).map(employee, EmployeeDTO.class);
        });

    var foundEmployee = employeeService.searchEmployees(null, null, null, 0, 10);

    assertThat(foundEmployee).isNotNull();
    assertThat(foundEmployee.getContent()).isNotEmpty();
    assertThat(foundEmployee.getTotalElements()).isEqualTo(employeeMap.size());
  }

  private Employee createEmployee() {
    var employee = new Employee();
    employee.setPersonalId(1L);
    employee.setName("John");
    employee.setTeam("Development");
    employee.setTeamLead("Mark");
    return employee;
  }

  private Employee createBasicEmployee(Long personalId, String name, String team, String teamLead) {
    var employee = new Employee();
    employee.setPersonalId(personalId);
    employee.setName(name);
    employee.setTeam(team);
    employee.setTeamLead(teamLead);
    return employee;
  }

  private EmployeeDTO createEmployeeDTO() {
    var employeeDTO = new EmployeeDTO();
    employeeDTO.setPersonalId(1L);
    employeeDTO.setName("John");
    employeeDTO.setTeam("Development");
    employeeDTO.setTeamLead("Mark");
    return employeeDTO;
  }

  private EmployeeDTO createBasicEmployeeDTO(
      Long personalId, String name, String team, String teamLead) {
    var employeeDTO = new EmployeeDTO();
    employeeDTO.setPersonalId(personalId);
    employeeDTO.setName(name);
    employeeDTO.setTeam(team);
    employeeDTO.setTeamLead(teamLead);
    return employeeDTO;
  }

  private Page<Employee> createEmployeePage() {
    var employeeList =
        List.of(
            createEmployee(),
            createBasicEmployee(2L, "Aaron", "Development", "Mark"),
            createBasicEmployee(3L, "Sarah", "Development", "Mark"),
            createBasicEmployee(4L, "Ana", "Accounting", "Kate"));
    var pageable = PageRequest.of(0, 10);
    return new PageImpl<>(employeeList, pageable, employeeList.size());
  }

  private List<Employee> createEmployeeList() {
    return List.of(
        createEmployee(),
        createBasicEmployee(2L, "Aaron", "Development", "Mark"),
        createBasicEmployee(3L, "Sarah", "Development", "Mark"),
        createBasicEmployee(4L, "Ana", "Accounting", "Kate"));
  }

  private List<EmployeeDTO> createEmployeeDTOList() {
    return List.of(
        createEmployeeDTO(),
        createBasicEmployeeDTO(2L, "Aaron", "Development", "Mark"),
        createBasicEmployeeDTO(3L, "Sarah", "Development", "Mark"),
        createBasicEmployeeDTO(4L, "Ana", "Accounting", "Kate"));
  }

  private Map<Employee, EmployeeDTO> createEmployeeMap() {
    return Map.of(
        createEmployee(), createEmployeeDTO(),
        createBasicEmployee(2L, "Aaron", "Development", "Mark"),
            createBasicEmployeeDTO(2L, "Aaron", "Development", "Mark"),
        createBasicEmployee(3L, "Sarah", "Development", "Mark"),
            createBasicEmployeeDTO(3L, "Sarah", "Development", "Mark"),
        createBasicEmployee(4L, "Ana", "Accounting", "Kate"),
            createBasicEmployeeDTO(4L, "Ana", "Accounting", "Kate"));
  }
}
