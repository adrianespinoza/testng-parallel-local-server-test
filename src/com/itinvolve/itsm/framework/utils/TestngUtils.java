/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.utils;

import static com.itinvolve.itsm.framework.env.Setup.BROWSER;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.ITestResult;
import org.testng.annotations.Test;

/**
 * Testng utils class. Provides method to work with testng library.
 * @author Adrian Espinoza
 *
 */
public class TestngUtils {
    /**
     * This method returns the test method name including the class name.
     * @param testResult The test method.
     * @return The test method name.
     */
    public static String getFullMethodName(ITestResult testResult) {
        String testClassName = testResult.getTestClass().getName();
        String testMethodName = testResult.getMethod().getConstructorOrMethod().getName();
        return (testClassName + "." + testMethodName);
    }

    /**
    * This method returns the test method name including class name but without dots.
    * @param testResult  The test method.
    * @return the test method name.
    */
    public static String getFullMethodNameWithoutDot(ITestResult testResult) {
        String result = (getFullMethodName(testResult)).replaceAll("\\.", "");
        return result;
    }

    public static String getMethodName(ITestResult testResult) {
        String testMethodName = testResult.getMethod().getConstructorOrMethod().getName();
        return testMethodName;
    }

    /**
     * This method builds file names using data from test method result.
     * @param testResult The test method result.
     * @return The file name.
     */
    public static String buildFileName(ITestResult testResult) {
        String testName = BROWSER.toUpperCase() + "_" + testResult.getTestClass().getRealClass().getSimpleName() + "_" + getMethodName(testResult);
        return testName;
    }

    /**
     * This method retrieve the file name from test name adding date.
     * @param testName The test name to add date.
     * @return The file name.
     */
    public static String getFileName(String testName) {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss");
        Date date = new Date();
        return dateFormat.format(date) + "_" + testName;
    }

    /**
     * This method retrieve the test name parameter value from Test annotation.
     * @param testResult The test method.
     * @return A value from test name parameter.
     */
    public static String getTestNameValue(ITestResult testResult) {
        Test testAnnotation  = getTestAnnotation(testResult);
        String testName = "";
        if (testAnnotation != null) {
            testName = testAnnotation.testName();
        }
        return testName;
    }

    /**
     * This method retrieve the description parameter value from Test annotation.
     * @param testResult The test method.
     * @return A value from description parameter.
     */
    public static String getDescriptionValue(ITestResult testResult) {
        Test testAnnotation  = getTestAnnotation(testResult);
        String testDescription = "";
        if (testAnnotation != null) {
            testDescription = testAnnotation.description();
        }
        return testDescription;
    }

    /**
     * This method retrieve the Test annotation from a test method.
     * @param testResult The test method.
     * @return The Test annotation.
     */
    public static Test getTestAnnotation(ITestResult testResult) {
        Test testAnnotation  = testResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class);
        return testAnnotation;
    }
}