package com.itinvolve.itsm.framework.listeners;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import com.itinvolve.itsm.framework.env.ScreenshotHandler;
import com.itinvolve.itsm.framework.env.Setup;
import com.itinvolve.itsm.framework.env.WebDriverHandler;
import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.utils.TestngUtils;

public class TestCaseTestListener extends TestListenerAdapter  {
    @Override
    public void onTestFailure(ITestResult testResult) {
        Reporter.setCurrentTestResult(testResult);
        Log.logger().error("-> The " + TestngUtils.getFullMethodName(testResult) + " test has FAILED because "  + testResult.getThrowable().getMessage());


        if (Setup.USE_BROWSER_FOR_TEST) {
            String fileName = TestngUtils.buildFileName(testResult);
            Reporter.log("<a href='logs/" + fileName + ".log'>Test Case Log</a>&nbsp &nbsp");
            Reporter.log("<a href='logs/catchAll.log'>General Log</a><br/>");

            ScreenshotHandler.takeScreenshotAddToReport(fileName, testResult);

            TestCaseLogger.logsToReport();

            ScreenshotHandler.compareImageFileToReport();
        }
    }

    @Override
    public void onTestSkipped(ITestResult testResult) {
        Log.logger().warn("-> The " + TestngUtils.getFullMethodName(testResult) + " test has SKIPPED");
    }

    @Override
    public void onTestSuccess(ITestResult testResult) {
        Reporter.setCurrentTestResult(testResult);
        Log.logger().info("-> The " + TestngUtils.getFullMethodName(testResult) + " test has PASSED");

        if (Setup.USE_BROWSER_FOR_TEST) {
            ScreenshotHandler.compareImageFileToReport();
        }
    }

    @Override
    public void onStart(ITestContext testContext) {
        String threadKey = String.valueOf(Thread.currentThread().getId());

        if (WebDriverHandler.containsDriver(threadKey)) {
            System.out.println("-> The driver was already created for thread: " + threadKey);
        } else {
            if (Setup.EXECUTE_IN_PARALLEL) {
                Map<String, String> parameters = testContext.getCurrentXmlTest().getAllParameters();

                String browser = parameters.get("browser");
                String credential = parameters.get("credential");
                String version = parameters.get("version");
                String platform = parameters.get("platform");

                Setup.BROWSER = browser;
                Setup.CREDENTIAL_ID = credential;
                Setup.VERSION = version;
                Setup.PLATFORM = platform;

                Setup.USE_BROWSER_FOR_TEST = StringUtils.isNotBlank(Setup.BROWSER);
                Setup.USE_CREDENTIAL_FOR_TEST = StringUtils.isNotBlank(Setup.CREDENTIAL_ID);

                Setup.PATH_LOG4J_FILES = Setup.PROPERTIES.getPropertyConfig("path.log4j.files") + Setup.BROWSER + "\\" + Setup.REPORT_DIRECTORY_NAME + "\\html\\logs\\";
                Setup.PATH_SCREENSHOT_FILES = Setup.PROPERTIES.getPropertyConfig("path.screenshot.files") + Setup.BROWSER + "\\" + Setup.REPORT_DIRECTORY_NAME + "\\html\\images\\";

                System.out.println("use browser: " + Setup.USE_BROWSER_FOR_TEST);

                if (Setup.USE_BROWSER_FOR_TEST) {
                    WebDriverHandler.openStartPage(Setup.EXECUTE_IN_PARALLEL, browser, version, platform, credential);
                }
            }
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
        if (Setup.EXECUTE_IN_PARALLEL) {
            WebDriverHandler.closeWindows();
        }
    }
}
