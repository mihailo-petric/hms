package com.hyperoptic.hms.repository;

import com.hyperoptic.hms.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Page<Employee> findByNameLikeOrTeamLikeOrTeamLeadLike(
      String name, String team, String teamLead, Pageable pageable);

  Page<Employee> findByNameOrTeamOrTeamLead(
      String name, String team, String teamLead, Pageable pageable);
}
