@HotelRoomBooking
Feature: Verify Hotel Room booking API Response

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

Scenario: Update room price for an existing room
  Given set request header "Content-Type" as "application/x-www-form-urlencoded"
  And set request parameters in request body:
    | roomId    | 101      |
    | roomPrice | 1200.0   |
  And get the response object for put response at endpoint "/updateRoomPrice"
  Then check if status code is "200"
  Given I send a GET request to "/viewRoomById/101"
  Then verify room with ID 101 has price 1200.0

Scenario: Delete an existing room by ID
  Given set request header "Accept" as "application/json"
  And set request parameters in request body:
    | roomId | 302 |
  And get the response object for delete response at endpoint "/deleteRoomById/302"
  Then check if status code is "200"
  Given I send a GET request to "/viewRoomList"
  Then the room with ID 302 should not be present in the list




