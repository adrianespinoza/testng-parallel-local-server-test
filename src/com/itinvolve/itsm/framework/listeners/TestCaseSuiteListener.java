package com.itinvolve.itsm.framework.listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.itinvolve.itsm.framework.env.Setup;
import com.itinvolve.itsm.framework.env.WebDriverHandler;
import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.logs.Log4jPropertiesHandler;


public class TestCaseSuiteListener implements ISuiteListener {

    @Override
    public void onFinish(ISuite arg0) {
        //Log4jPropertiesHandler.swithTestCaseLogFileName("catchAllTC");

        if (!Setup.EXECUTE_IN_PARALLEL) {
            if (Setup.USE_BROWSER_FOR_TEST) {
                WebDriverHandler.closeWindows();
                Log.logger().info("-> The browser was CLOSED");
                System.out.println("The browser was closed at the end suite...");
            }
        }
    }

    @Override
    public void onStart(ISuite arg0) {
        Log4jPropertiesHandler.setUp();

        System.out.println("-> Is parallel: " + Setup.EXECUTE_IN_PARALLEL);

        if (!Setup.EXECUTE_IN_PARALLEL) {
            if (Setup.USE_BROWSER_FOR_TEST) {
                WebDriverHandler.openStartPage(Setup.EXECUTE_IN_PARALLEL, Setup.BROWSER, Setup.VERSION, Setup.PLATFORM, Setup.CREDENTIAL_ID);
            }
        }
    }
}
