package com.definitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.*;

import java.util.List;
import java.util.Map;

import com.setup.Setup;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class StepDef {

    public static RequestSpecification httpRequest;
    public static Response response;
    public static Setup configFileReader = new Setup();

    @Given("Set RestAssured Base URL for the Authentication")
    public void setRestAssuredBaseURLForTheAuthentication() {
        RestAssured.baseURI = configFileReader.getAuthURL();
    }

    @Given("Set RestAssured Base URL for the Hotel Room BaseURL")
    public void setRestAssuredBaseURLForTheHotelRoomBaseURL() {
        RestAssured.baseURI = configFileReader.getBaseURL();
    }

    @And("set request header {string} as {string}")
    public static void setRequestHeaderAs(String header, String value) {
        httpRequest = RestAssured.given().header(header, value);
    }

    @And("set parameter {string} in request body as {string}")
    public void setParameterInRequestBodyAs(String paramName, String paramValue) {
        httpRequest = httpRequest.formParam(paramName, paramValue);
    }
    @And("get the response object for post response at endpoint {string}")
    public void getTheResponseObjectAtEndpoint(String endpoint) {
        response = httpRequest.post(endpoint);
    }

    @Then("check if status code is {string}")
    public void ifStatusCodeIs(String expectedStatusCode) {
        int statuscode = response.getStatusCode();
        assertThat(statuscode, is(Integer.parseInt(expectedStatusCode)));
    }


    @Then("set retrieved auth_code as environment variable {string}")
    public void setRetrievedAuth_codeAsEnvironmentVariable(String auth) {
        System.setProperty(auth, response.jsonPath().getString("auth_code"));
        System.out.println("auth_code: "+response.jsonPath().getString("auth_code"));
    }

    @And("get environment variable {string} for request body")
    public void getEnvironmentVariableForRequestBody(String auth) {
        String authCode = System.getProperty(auth);
        httpRequest = httpRequest.formParam("auth_code", authCode);
    }

    @Then("set retrieved access_token as environment variable {string}")
    public void setRetrievedAccess_tokenAsEnvironmentVariable(String access) {
        System.setProperty(access, response.jsonPath().getString("access_token"));
        System.out.println("access_token: "+response.jsonPath().getString("access_token"));
    }

    @Given("I send a GET request to {string}")
    public void i_send_a_get_request_to(String endpoint) {
    	RestAssured.baseURI = configFileReader.getBaseURL();
    	httpRequest = RestAssured.given()
    		    .header("Accept", "application/json");
        response = httpRequest.get(endpoint);
        
        
        

    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int expectedStatus) {
    	System.out.println(response.getStatusCode());
        assertEquals(expectedStatus, response.getStatusCode());
    }

    @Then("the response header {string} should be {string}")
    public void the_response_header_should_be(String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        System.out.println("Checking header: " + headerName);
        System.out.println("Expected value: " + expectedValue);
        System.out.println("Actual value: " + actualValue);
        assertEquals(expectedValue, actualValue);
    }


    @Then("the response should contain a non-empty room list")
    public void the_response_should_contain_a_non_empty_room_list() {
        List<?> roomList = response.jsonPath().getList("$");
        assertNotNull(roomList);
        assertTrue(roomList.size() > 0);
    }

    @Then("the room with ID {int} should have price {double}")
    public void the_room_with_id_should_have_price(int roomId, double expectedPrice) {
        List<Map<String, Object>> rooms = response.jsonPath().getList("$");
        Map<String, Object> room301 = rooms.stream()
            .filter(room -> roomId == ((Number)room.get("roomId")).intValue())
            .findFirst()
            .orElse(null);

        
        assertNotNull(room301, "Room with ID " + roomId + " not found");


        double actualPrice = ((Number)room301.get("roomPrice")).doubleValue();
        System.out.println(actualPrice);
        assertEquals(expectedPrice, actualPrice, 0.01);
    }

    @Then("all room types should be valid")
    public void all_room_types_should_be_valid() {
        List<Map<String, Object>> rooms = response.jsonPath().getList("$");
        List<String> validTypes = List.of("SINGLE", "DOUBLE", "DELUXE");

        for (Map<String, Object> room : rooms) {
            String roomType = (String) room.get("roomType");
            assertTrue(validTypes.contains(roomType), "Invalid room type: " + roomType);

        }


}
  

//    @When("I send a GET request to {string}")
//    public void i_send_a_get_request_to(String endpoint) {
//        RestAssured.baseURI = configFileReader.getBaseURL();
//        response = httpRequest.get(endpoint);
// 
    @When("I send a GET request to \\/viewRoomById\\/{int}")
    public void i_send_a_get_request_to_viewRoomById(int roomId) {
        // Set the base URI
        RestAssured.baseURI = "https://webapps.tekstac.com/HotelAPI/RoomService";

        // Create the GET request
        httpRequest = RestAssured.given()
                        .header("Accept", "application/json")
                        .pathParam("roomId", roomId);

        // Call the endpoint
        response = httpRequest.get("/viewRoomById/{roomId}");

        // Debug outputs
        System.out.println("Full URL: " + RestAssured.baseURI + "/viewRoomById/" + roomId);
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());
    }



    @Then("the response should contain all expected keys")
    public void the_response_should_contain_all_expected_keys() {
        Map<String, ?> jsonData = response.jsonPath().getMap("$");
        assertTrue(jsonData.containsKey("roomId"));
        assertTrue(jsonData.containsKey("hotelId"));
        assertTrue(jsonData.containsKey("roomType"));
        assertTrue(jsonData.containsKey("roomStatus"));
        assertTrue(jsonData.containsKey("roomPrice"));
    }

    @Then("the response field values should match the expected data")
    public void the_response_field_values_should_match_expected_data() {
        Map<String, ?> jsonData = response.jsonPath().getMap("$");

        assertEquals(jsonData.get("roomId"), 101);
        assertEquals(jsonData.get("hotelId"), "H2001");
        assertEquals(jsonData.get("roomType"), "SINGLE");
        assertEquals(jsonData.get("roomStatus"), "AVAILABLE");
        assertEquals(((Number) jsonData.get("roomPrice")).doubleValue(), 1200.0, 0.01);
    }

    @Then("the response should not contain invalid key {string}")
    public void the_response_should_not_contain_invalid_key(String invalidKey) {
        Map<String, ?> jsonData = response.jsonPath().getMap("$");
        assertFalse(jsonData.containsKey(invalidKey), "Response should NOT contain key: " + invalidKey);
    }

    @Then("the room price should be greater than 0")
    public void the_room_price_should_be_greater_than_0() {
        Map<String, ?> jsonData = response.jsonPath().getMap("$");
        double price = ((Number) jsonData.get("roomPrice")).doubleValue();
        assertTrue(price > 0, "Room price should be greater than 0");
    }

    @Then("the data types of fields should be valid")
    public void the_data_types_of_fields_should_be_valid() {
        Map<String, ?> jsonData = response.jsonPath().getMap("$");

        assertTrue(jsonData.get("roomId") instanceof Integer || jsonData.get("roomId") instanceof Number);
        assertTrue(jsonData.get("hotelId") instanceof String);
        assertTrue(jsonData.get("roomType") instanceof String);
        assertTrue(jsonData.get("roomStatus") instanceof String);
        assertTrue(jsonData.get("roomPrice") instanceof Number);
    }}