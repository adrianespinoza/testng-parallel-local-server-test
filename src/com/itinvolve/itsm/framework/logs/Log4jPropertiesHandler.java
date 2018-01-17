/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.logs;

import static com.itinvolve.itsm.framework.env.Setup.FILE_LOG4J_APPENDER;
import static com.itinvolve.itsm.framework.env.Setup.PATH_LOG4J_CONFIG;
import static com.itinvolve.itsm.framework.env.Setup.PATH_LOG4J_FILES;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;

/**
 * Log4j properties handler class. Provides method to work with log files.
 * @author Adrian Espinoza
 *
 */
public class Log4jPropertiesHandler {
    /** Store the log4j properties that point to a properties file */
    private static Properties LOG4J_PROPERTIES = null;

    /**
     * This method allows to know if the LOG4J_PROPERTIES was already loaded.
     * @return boolean.
     */
    public static boolean log4jPropertiesWasLoaded() {
        boolean wasLoaded = true;
        if (LOG4J_PROPERTIES == null) {
            wasLoaded = false;
        }
        return wasLoaded;
    }

    /**
     * This method configure and return the log4j properties.
     * @return The log4j properties.
     */
    public static Properties getLog4jProperties() {
        if (LOG4J_PROPERTIES == null) {
            setUp();
        }
        return LOG4J_PROPERTIES;
    }

    /**
     * This method configures the log4j properties.
     */
    public static void setUp() {
        LOG4J_PROPERTIES = loadLog4jPropertiesFrom(PATH_LOG4J_CONFIG);
        PropertyConfigurator.configure(LOG4J_PROPERTIES);
        swithTestCaseLogFileName("catchAll", "log4j.appender.defaultLog.File");

        //DOMConfigurator.configure(PATH_LOG4J_CONFIG);
    }

    /**
     * This method changes the log file name by another.
     * @param fileName The new file name.
     */
    public static void swithTestCaseLogFileName(String fileName) {
        swithTestCaseLogFileName(fileName, FILE_LOG4J_APPENDER);
    }

    /**
     * @param fileName The new file name.
     * @param appender The log4j appender
     */
    private static void swithTestCaseLogFileName(String fileName, String appender) {
        LOG4J_PROPERTIES = getLog4jProperties();
        //log4jProperties.setProperty("log4j.rootLogger","DEBUG, FILE");
        LOG4J_PROPERTIES.setProperty(appender, PATH_LOG4J_FILES + fileName + ".log");
        LogManager.resetConfiguration();
        PropertyConfigurator.configure(LOG4J_PROPERTIES);
    }

    /**
     * This method loads the properties file from a specific location.
     * @param log4JPropertyFilePath The properties file location.
     * @return The properties loaded from file.
     */
    private static Properties loadLog4jPropertiesFrom(String log4JPropertyFilePath) {
        Properties log4jPropertiesResult = new Properties();
        try {
            FileInputStream configStream = new FileInputStream(log4JPropertyFilePath);
            log4jPropertiesResult.load(configStream);
            configStream.close();
        } catch(IOException e) {
            System.out.println("Error: Cannot laod configuration file ");
        }
        return log4jPropertiesResult;
    }
}

