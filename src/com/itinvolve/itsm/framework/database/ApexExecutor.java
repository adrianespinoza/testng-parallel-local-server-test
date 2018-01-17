/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import com.itinvolve.itsm.framework.logs.Log;
import com.sforce.soap.apex.ExecuteAnonymousResult;
import com.sforce.ws.ConnectionException;

/**
 * @author Adrian Espinoza
 *
 */
public class ApexExecutor {
    /**
     * This method executes the anonymous apex.
     * @param apexCode The anonymous apex.
     * @return An execution status.
     */
    public static boolean executeApex (String apexCode) {
        boolean wasExecuted = false;
        try {
            Log.logger().info("-> Anonymous apex to execute:\n" + SObjectUtils.MSG_SEPARATOR + "\n" + apexCode + "\n" + SObjectUtils.MSG_SEPARATOR);
            ExecuteAnonymousResult result = ApexConnector.getSoapConnection().executeAnonymous(apexCode);
            if (result.isSuccess()) {
                wasExecuted = true;
                Log.logger().info("-> Anonymous apex was executed successfully");
            } else {
                Log.logger().error("-> Error to trying execute anonymous apex >>> Exception message[" + result.getExceptionMessage() + "]");
                System.out.println("-> Error to trying execute anonymous apex >>> Exception message[" + result.getExceptionMessage() + "]");
                System.out.println(result.getExceptionStackTrace());
            }
        } catch (ConnectionException e) {
            Log.logger().error("-> Error in SOAP Connection >>> [" + e.getMessage() + "]");
            e.printStackTrace();
        }
        return wasExecuted;
    }
}
