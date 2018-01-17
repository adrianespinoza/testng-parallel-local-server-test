package com.itinvolve.itsm.framework.logs;

import java.util.HashMap;
import java.util.Map;

public class TestCaseFileLogger {
    private static Map<String, String> testLogNamesCollector;

    public static String getLogFileName(String threadId) {
        if (testLogNamesCollector == null) {
            return null;
        }
        return testLogNamesCollector.get(threadId);
    }

    public static void putLogFileName(String threadId, String logFileName) {
        if (testLogNamesCollector == null) {
            testLogNamesCollector = new HashMap<String, String>();
        }
        testLogNamesCollector.put(threadId, logFileName);
    }
}
