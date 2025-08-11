package com.tests;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java/com/features",
        glue = "com.definitions",
        plugin = {
                "pretty",
                "junit:target/cucumber-reports/Cucumber.xml"
        },
        tags = "(@Authentication or @HotelRoomBooking) and (not @ignore)"
)
public class CucumberRunnerTests extends AbstractTestNGCucumberTests {
}
