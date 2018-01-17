/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.env;

/**
 * Enum that contains the browsers supported by the framework.
 * @author Adrian Espinoza
 *
 */
public enum BrowserType {
    FIREFOX("firefox"),
    CHROME("chrome"),
    IE("internet explorer"),
    SAFARI("safari"),
    OPERA("opera");

    private final String browser;

    BrowserType(String browser) {
        this.browser = browser;
    }

    public String getBrowser() {
        return browser;
    }
}
