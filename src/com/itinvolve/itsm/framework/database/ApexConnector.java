/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import com.sforce.soap.apex.SoapConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * @author Adrian Espinoza
 *
 */
public class ApexConnector {
    private static SoapConnection connection;
    /**
     * This method returns the soap connection.
     * @return A soap connection.
     */
    public static SoapConnection getSoapConnection() {
        try {
            ConnectorConfig config = getConnectorConfig ();
            connection = new SoapConnection(config);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * This method returns the connector configuration.
     * @return A connector configuration.
     */
    private static ConnectorConfig getConnectorConfig () {
        ConnectorConfig config = DatabaseConnector.getConnectorConfig();
        if (config != null) {
            ConnectorConfig soapConfig = new ConnectorConfig();

            soapConfig.setAuthEndpoint(config.getAuthEndpoint());
            soapConfig.setServiceEndpoint(config.getServiceEndpoint().replace("/Soap/u/", "/Soap/s/"));//take careful
            soapConfig.setSessionId(config.getSessionId());
            return soapConfig;
        }
        else
        {
            return null;
        }
    }
}
