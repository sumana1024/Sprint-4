@HotelRoomBooking
Feature: Verify Room List API Response

  Scenario: Verify room list API response and data
    Given I send a GET request to "/viewRoomList"
    Then the response status code should be 200
    And the response header "Content-Type" should be "application/json"
    And the response should contain a non-empty room list
    And the room with ID 301 should have price 5000.0
    And all room types should be valid

Scenario Outline: Validate room details response fields and values
  Given set request header "accept" as "application/json"
  When I send a GET request to /viewRoomById/101
  Then the response status code should be 200
  And the response should contain all expected keys
  And the response field values should match the expected data
  And the response should not contain invalid key "invalidField"
  And the room price should be greater than 0
  And the data types of fields should be valid



