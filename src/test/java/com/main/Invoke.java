package com.main;

import org.testng.TestNG;
import org.testng.collections.Lists;

import java.util.List;

public class Invoke {
    public void checkAgent() {
        //Invoke the test using TestNG ONLY HERE.
        try {
            TestNG testng = new TestNG();
            List<String> suites = Lists.newArrayList();
            String workingDir = System.getProperty("user.dir");
            System.out.println("user.dir "+ workingDir);
            suites.add(workingDir + "/testng.xml");

            testng.setTestSuites(suites);
            testng.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Invoke iv=new Invoke();
        iv.checkAgent();
    }
}