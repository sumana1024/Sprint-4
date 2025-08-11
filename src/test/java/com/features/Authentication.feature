@Authentication
Feature: Testing the Authentication URL endpoints
  Background:
    Given Set RestAssured Base URL for the Authentication
    And set request header "accept" as "application/json"

  Scenario: Get Authentication Code is working
    And set parameter "username" in request body as "user1"
    And set parameter "password" in request body as "pass123"
    And get the response object for post response at endpoint "/login"
    Then check if status code is "200"
    Then set retrieved auth_code as environment variable "auth_code"

  Scenario: Get Authentication Token is working
    And get environment variable "auth_code" for request body
    And get the response object for post response at endpoint "/token"
    Then check if status code is "200"
    Then set retrieved access_token as environment variable "access_token"

  Scenario: Get Authentication Code for invalid password
    And set parameter "username" in request body as "user1"
    And set parameter "password" in request body as "pass1234"
    And get the response object for post response at endpoint "/login"
    Then check if status code is "401"

  Scenario: Get Authentication Token for invalid auth code
    And set parameter "auth_code" in request body as "invalid"
    And get the response object for post response at endpoint "/token"
    Then check if status code is "400"