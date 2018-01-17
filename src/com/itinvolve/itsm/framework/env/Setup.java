/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.env;

import org.apache.commons.lang3.StringUtils;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.sfusers.UserHandler;

/**
 * Setup class. Provide main configurations for all framework
 * @author Adrian Espinoza
 *
 */
public class Setup {
    /** Store the properties instance */
    public static PropertiesFileReader PROPERTIES = new PropertiesFileReader("config/config.properties");

    public enum Page {SALESFORCE, ITINVOLVE, LOGIN};

    public static String BROWSER = System.getProperty("browser");
    public static String CREDENTIAL_ID = System.getProperty("credential");//"adrian.sandbox";

    public static boolean EXECUTE_IN_PARALLEL = Boolean.valueOf(System.getProperty("parallel"));
    public static String VERSION = "1.2";
    public static String PLATFORM = "WINDOWS";

    public static String REPORT_DIRECTORY_NAME = System.getProperty("reportdirname");

    public static boolean USE_BROWSER_FOR_TEST = StringUtils.isNotBlank(BROWSER);
    public static boolean USE_CREDENTIAL_FOR_TEST = StringUtils.isNotBlank(CREDENTIAL_ID);

    public static String PATH_SALESFORCE_CREDENTIALS = PROPERTIES.getPropertyConfig("path.salesforce.credentials");

    public static String START_URL = PROPERTIES.getPropertyConfig("sf.url");
    public static String TESTLINK_TICKET_URL = PROPERTIES.getPropertyConfig("testlink.ticket.url");
    public static String AUTHENDPOINT_URL = PROPERTIES.getPropertyConfig("sf.authendpoint.url");
    public static String SFDC_TYPE = "login";// [default login means ] This depend of credential file type users.

    public static String PATH_DRIVER_CHROME = PROPERTIES.getPropertyConfig("path.driver.chrome");
    public static String PATH_DRIVER_IE = PROPERTIES.getPropertyConfig("path.driver.ie");

    public static String FILE_LOG4J_APPENDER = "log4j.appender.tcFileLog.File";
    public static String PATH_LOG4J_CONFIG = PROPERTIES.getPropertyConfig("path.log4j.config");
    public static String PATH_LOG4J_FILES = PROPERTIES.getPropertyConfig("path.log4j.files") + BROWSER + "\\" + REPORT_DIRECTORY_NAME + "\\html\\logs\\";

    public static String PATH_SCREENSHOT_FILES = PROPERTIES.getPropertyConfig("path.screenshot.files") + BROWSER + "\\" + REPORT_DIRECTORY_NAME + "\\html\\images\\";

    public static String INPUT_FILE_PATH = PROPERTIES.getPropertyConfig("path.input.files");
    public static String SOBJECT_FILE_PATH = PROPERTIES.getPropertyConfig("path.sobject.files");;
    public static String ORG_PREFIX = PROPERTIES.getPropertyConfig("itinvolve.package.prefix") + "__";
    public final static String SOBJECT_STANDARD_POSTFIX = "__c";
    public final static String SOBJECT_REFERENCE_POSTFIX = "__r";

    public static String SELENIUM_REMOTE_ADDRESS = PROPERTIES.getPropertyConfig("selenium.remote.address");

    /**
     * This method configure the environment before execute the test cases.
     */
    public static boolean setupEnvironment(String credentialId) {
        boolean wasSetup = true;

        if(USE_CREDENTIAL_FOR_TEST) {
            if (StringUtils.isNotBlank(credentialId)) {
                wasSetup = UserHandler.setup(credentialId);//load user profile information at first time from credentials file configuration
            } else {
                wasSetup = false;
            }
        }

        if (wasSetup) {
            SFDC_TYPE = UserHandler.getSfdcTypeAttributeValue();// could return null

            String toReplace = "[" + UserHandler.getSfdcTypeAttribute() + "]";

            if (StringUtils.isNotBlank(SFDC_TYPE)) {
                if (!START_URL.contains(SFDC_TYPE)) {
                    START_URL = START_URL.replace(toReplace, SFDC_TYPE);
                }
                if (!AUTHENDPOINT_URL.contains(SFDC_TYPE)) {
                    AUTHENDPOINT_URL = AUTHENDPOINT_URL.replace(toReplace, SFDC_TYPE);
                }
            } else {
                wasSetup = false;
                Log.logger().error("-> The " + UserHandler.getSfdcTypeAttribute() + " attibute value not defined in credentials.xml file for credential id = " + UserHandler.getIdAttributeValue());
            }
        }
        return wasSetup;
    }
}
