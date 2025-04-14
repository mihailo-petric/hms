package com.hyperoptic.hms.repository;

import com.hyperoptic.hms.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  Page<Employee> findByNameLikeOrTeamLikeOrTeamLeadLike(
      String name, String team, String teamLead, Pageable pageable);

  @Query(
      "SELECT e FROM employees e WHERE (:name is null or e.name = :name) and "
          + "(:teamLead is null or e.teamLead = :teamLead) and"
          + "(:team is null or e.team = :team)")
  Page<Employee> findByNameOrTeamOrTeamLead(
      @Param("name") String name,
      @Param("team") String team,
      @Param("teamLead") String teamLead,
      Pageable pageable);
}
