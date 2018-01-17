/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Java Script Utils class. Provide methods to work with javascrip in selenium.
 * @author Adrian Espinoza
 *
 */
public class JavaScriptUtils {
    /**
     * This method executes java script code using selenium driver.
     * @param <T>
     * @param js The java script code.
     * @param driver The web driver.
     * @param arguments The elements.
     * @return A generic result.
     */
    @SuppressWarnings("unchecked")
    public static <T> T execute(String js, WebDriver driver, Object... arguments) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (T) jsExecutor.executeScript(js, arguments);
    }

    /**
     * This method executes java script code using selenium driver.
     * @param <T>
     * @param js The java script code.
     * @param driver The web driver.
     * @return A generic result.
     */
    @SuppressWarnings("unchecked")
    public static <T> T execute(String js, WebDriver driver) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        return (T) jsExecutor.executeScript(js);
    }
}
