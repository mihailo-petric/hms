package com.hyperoptic.hms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Employee entity class representing an employee in the system.
 * This class is mapped to the "employees" table in the database.
 * It contains fields for personal ID, name, team, and team lead.
 * The personal ID is auto-generated and serves as the primary key.
 */
@Entity(name = "employees")
@Data
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long personalId;

  @NotNull private String name;

  @NotNull private String team;

  @NotNull private String teamLead;
}
