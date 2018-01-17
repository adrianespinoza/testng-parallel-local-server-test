/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import com.itinvolve.itsm.framework.env.Setup;
import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.sfusers.Profile;
import com.itinvolve.itsm.framework.sfusers.User;
import com.itinvolve.itsm.framework.sfusers.UserHandler;
import com.sforce.soap.partner.Connector;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * Class Web Service Connector that allow connect with any salesforce org
 * @author Adrian Espinoza
 *
 */
public class DatabaseConnector {
    /** Store a partner connection instance */
    private static PartnerConnection connection = null;
    private static ConnectorConfig config = null;

    /** Store the user connected to the data base */
    private static User userConnected;
    private static String userName;

    /**
     * This method returns the current connection instance, use the sysadmin user by default to connect with the data base.
     * @return The connection instance.
     */
    public static PartnerConnection getConnection() {
        User currentUser = UserHandler.getCurrentUser();
        switchConnection(currentUser);
        return connection;
    }

    public static ConnectorConfig getConnectorConfig() {
        if (config == null) {
            getConnection();
        }
        return config;
    }

    public static String getUserName() {
        return userName;
    }

    /**
     * This method changes the current connection with another user.
     * @param user The new user to establish new connection
     * @return A switch status connection
     */
    public static boolean switchConnection(User user) {
        boolean wasChanged = false;
        if (connection == null) {
            Profile profileToConnect = user == null ? Profile.SYSADMIN : user.profile;
            wasChanged = connect(profileToConnect);
        } else if ((user != null) && (userConnected != null) && (userConnected.compareTo(user) != 0)) {
            Log.logger().info("-> Web Service Connector: Switching connection with different user >>> [new user name: " + user.name + "] [current user name: " + userConnected.name + "]");
            wasChanged = disconnect() ? connect(user.profile) : false;
        }
        if (wasChanged) {
            userConnected = user;
        }
        return wasChanged;
    }

    /**
     * This method establishes the WSC connection with a Salesforce user.
     * @param user The user to establish data base connection
     * @return A connection status
     */
    public static boolean connect(User user) {
        return switchConnection(user);
    }

    /**
     * This method establishes the WSC connection using an user profile.
     * @param profile The Salesforce user profile
     * @return A connection status
     */
    public static boolean connect(Profile profile) {
        userConnected = UserHandler.getUser(profile);
        return connect(userConnected.name, userConnected.password, userConnected.token, Setup.AUTHENDPOINT_URL);
    }

    /**
     * Establish the WSC connection.
     * @param username The Salesforce user name
     * @param password The user password
     * @param token The Salesforce security token
     * @return A connection status
     */
    public static boolean connect(String username, String password, String token) {// default authendpoint https://test.salesforce.com/services/Soap/c/27.0
        return connect(username, password, token, null);
    }

    /**
     * This method establishes the WSC connection.
     * @param username The Salesforce user name
     * @param password The user password
     * @param token The Salesforce security token
     * @param authEndPoint The url used to make OAuth authentication requests to Salesforce
     * @return a connection status
     */
    public static boolean connect(String username, String password, String token, String authEndPoint) {
        boolean success = false;

        String fullPassword = password + token;

        config = new ConnectorConfig();
        config.setUsername(username);
        config.setPassword(fullPassword);

        if (authEndPoint != null && !authEndPoint.isEmpty()) {
            config.setAuthEndpoint(authEndPoint);
        }

        String message;
        try {
            connection = Connector.newConnection(config);
            userName = username;
            message = "-> Web Service Connector: Connection established successful! >>> [user name: " + userName + "]";
            System.out.println(message);
            Log.logger().info(message);
            success = true;
        } catch (ConnectionException e1) {
            message = "-> Web Service Connector Error: Connection failed! >>> [user name: " + userName + "]";
            System.out.println(message + " " + e1.getMessage());
            Log.logger().error(message, e1);
            e1.printStackTrace();
        }
        return success;
    }

    /**
     * This method disconnect a connection made to Salesforce.
     * @return A disconnect status
     */
    public static boolean disconnect() {
        boolean success = true;
        String message;
        try {
            if (connection != null) {
                connection.logout();
                message = "-> Web Service Connector: Connection closed! >>> [user name: " + userName + "]";
                System.out.println(message);
                Log.logger().info(message);
            }
        } catch (ConnectionException e) {
            message = "-> Web Service Connector Error: Error to trying to disconnect >>> [user name: " + userName + "]";
            System.out.println(message + e.getMessage());
            Log.logger().error(message, e);
            success = false;
            e.printStackTrace();
        }
        return success;
    }
}
