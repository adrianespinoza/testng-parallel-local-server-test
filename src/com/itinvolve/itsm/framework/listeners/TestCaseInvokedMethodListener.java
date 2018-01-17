package com.itinvolve.itsm.framework.listeners;

import static com.itinvolve.itsm.framework.env.Setup.TESTLINK_TICKET_URL;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.itinvolve.itsm.framework.annotations.AnnotationParser;
import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.logs.Log4jPropertiesHandler;
import com.itinvolve.itsm.framework.logs.TestCaseFileLogger;
import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.utils.TestngUtils;


public class TestCaseInvokedMethodListener implements IInvokedMethodListener {

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (testResult.getInstance() != null) {
            Log.logger().info("-> The " + TestngUtils.getFullMethodName(testResult) + " test has FINISHED");
            AnnotationParser.parseAfterTestMethod(testResult.getInstance(), TestngUtils.getMethodName(testResult));
        }
    }

    @Override
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (testResult.getInstance() != null) {
            TestCaseLogger.reset();
            String logFileName = TestngUtils.buildFileName(testResult);

            String threadId = String.valueOf(Thread.currentThread().getId());
            TestCaseFileLogger.putLogFileName(threadId, logFileName);

            //Log4jPropertiesHandler.swithTestCaseLogFileName(logFileName);//only name
            Log.logger().info("-> The " + TestngUtils.getFullMethodName(testResult) + " test has STARTED");

            Reporter.setCurrentTestResult(testResult);

            String annotationTestName = TestngUtils.getTestNameValue(testResult);
            if (!annotationTestName.isEmpty()) {
                Reporter.log("<a href='" + TESTLINK_TICKET_URL + annotationTestName + "'>" + annotationTestName + "</a><br/>");
            }

            String annotationTestDescription = TestngUtils.getDescriptionValue(testResult);
            if (!annotationTestDescription.isEmpty()) {
                Reporter.log(annotationTestDescription + "<br/>");
            }
            AnnotationParser.parseBeforeTestMethod(testResult.getInstance(), TestngUtils.getMethodName(testResult));
        }
    }
}
