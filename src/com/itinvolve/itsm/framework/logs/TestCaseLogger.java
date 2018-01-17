/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.logs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Reporter;

/**
 * Test case logger class. Provide methods to write information in log files and in the test report
 * @author Adrian Espinoza
 *
 */
public class TestCaseLogger {
    /** Store the logs to write after */
    private static Map<String,List<String>> logsCollector;

    /**
    * This method save log information and write in log files.
    * @param message The message to write in logs.
    */
    public static void log(String message) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        putLog(threadId, message);
        Log.logger().info(message);
    }

    /**
     * This method adds the test cases steps into test report.
     */
    public static void logsToReport() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        if ((logsCollector != null) && !logsCollector.isEmpty() && logsCollector.containsKey(threadId)) {
            List<String> logs = logsCollector.get(threadId);
            if ((null != logs) && !logs.isEmpty()) {
                String result = "";
                result += "<fieldset>";
                result += "<legend>Test Case Execution</legend>";
                result += "<ol style=\"padding-left: 3em;\">";
                for (String message : logs) {
                    result += "<li>" + message +"</li>";
                }
                result += "</ol>";
                result += "</fieldset>";
                Reporter.log(result);
            }
        }
    }

    /**
     * This method make a reset to log list information.
     */
    public static void reset() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        if (logsCollector != null && logsCollector.containsKey(threadId)) {
            logsCollector.remove(threadId);
        }
    }

    public static List<String> getLog(String key) {
        if (logsCollector == null) {
            return null;
        }
        return logsCollector.get(key);
    }

    public static boolean containsLog(String key) {
        if (logsCollector == null) {
            return false;
        }
        return logsCollector.containsKey(key);
    }

    public static void putLog(String key, String message) {
        if (logsCollector == null) {
            logsCollector = new HashMap<String, List<String>>();
        }

        if (logsCollector.containsKey(key)) {
            logsCollector.get(key).add(message);
        } else {
            logsCollector.put(key, new ArrayList<String>(Arrays.asList(message)));
        }
    }
}
