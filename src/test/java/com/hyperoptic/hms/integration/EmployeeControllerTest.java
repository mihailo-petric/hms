package com.hyperoptic.hms.integration;

import static com.hyperoptic.hms.TestType.INTEGRATION;
import static org.assertj.core.api.Assertions.assertThat;

import com.hyperoptic.hms.dto.EmployeeDTO;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

@Tag(INTEGRATION)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
public class EmployeeControllerTest {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  private String getBaseUrl() {
    return "http://localhost:" + port + "/api/v1/employee";
  }

  @Test
  public void givenEmployeeDTOWhenNameIsMissingThenReturn400BadRequest() {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setTeam("Engineering");
    dto.setTeamLead("Ivan");

    ResponseEntity<String> response = postForEntity(dto, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).contains("name is mandatory");
  }

  @Test
  public void givenEmployeeDTOWhenTeamIsMissingThenReturn400BadRequest() {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setName("Petar Petrovic");
    dto.setTeamLead("Matija");

    ResponseEntity<String> response = postForEntity(dto, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).contains("team is mandatory");
  }

  @Test
  public void givenEmployeeDTOWhenTeamLeadIsMissingThenReturn400BadRequest() {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setName("Petar Petrovic");
    dto.setTeam("Engineering");

    ResponseEntity<String> response = postForEntity(dto, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).contains("teamLead is mandatory");
  }

  @Test
  public void givenEmployeeDTOWhenValidDataThenReturn201Created() {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setName("Petar Petrovic");
    dto.setTeam("Dev");
    dto.setTeamLead("Matija");

    ResponseEntity<EmployeeDTO> response = postForEntity(dto, EmployeeDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getName()).isEqualTo("Petar Petrovic");
  }

  @Test
  public void givenEmployeeIdWhenGetEmployeeByIdThenReturn200AndEmployee() {
    String url = getBaseUrl() + "/20";

    ResponseEntity<EmployeeDTO> response = restTemplate.getForEntity(url, EmployeeDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getPersonalId()).isEqualTo(20L);
    assertThat(response.getBody().getName()).isEqualTo("Alice Johnson");
    assertThat(response.getBody().getTeam()).isEqualTo("Marketing");
    assertThat(response.getBody().getTeamLead()).isEqualTo("Tom Brown");
  }

  @Test
  public void givenInvalidEmployeeIdWhenGetEmployeeByIdThenReturn404NotFound() {
    String url = getBaseUrl() + "/999";

    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void givenEmployeeDTOWhenUpdateEmployeeThenReturn200AndUpdatedEmployee() {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setName("Updated Name");
    dto.setTeam("Updated Team");
    dto.setTeamLead("Updated Lead");

    String url = getBaseUrl() + "/10";
    ResponseEntity<EmployeeDTO> response = putForEntity(url, dto, EmployeeDTO.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().getName()).isEqualTo("Updated Name");
  }

  @Test
  public void givenInvalidEmployeeIdWhenUpdateEmployeeThenReturn404NotFound() {
    EmployeeDTO dto = new EmployeeDTO();
    dto.setName("Updated Name");
    dto.setTeam("Updated Team");
    dto.setTeamLead("Updated Lead");

    String url = getBaseUrl() + "/999";
    ResponseEntity<String> response = putForEntity(url, dto, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void givenEmployeeIdWhenDeleteEmployeeThenReturn204NoContent() {
    String url = getBaseUrl() + "/1";

    ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
  }

  @Test
  public void givenInvalidEmployeeIdWhenDeleteEmployeeThenReturn404NotFound() {
    String url = getBaseUrl() + "/999";

    ResponseEntity<String> response =
        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  public void givenPaginationParamsWhenGetAllEmployeesThenReturn200AndPageOfEmployees() {
    String url = getBaseUrl() + "?page=0&size=10";

    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("content");
  }

  @Test
  public void givenSearchParamsWhenSearchEmployeesThenReturn200AndFilteredEmployees() {
    String url = getBaseUrl() + "/search?name=John&page=0&size=10";

    ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("content");
  }

  private <T> ResponseEntity<T> postForEntity(EmployeeDTO dto, Class<T> responseType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<EmployeeDTO> request = new HttpEntity<>(dto, headers);

    return restTemplate.postForEntity(getBaseUrl(), request, responseType);
  }

  private <T> ResponseEntity<T> putForEntity(String url, EmployeeDTO dto, Class<T> responseType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<EmployeeDTO> request = new HttpEntity<>(dto, headers);

    return restTemplate.exchange(url, HttpMethod.PUT, request, responseType);
  }
}
