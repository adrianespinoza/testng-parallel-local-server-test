/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.sfusers;

import static com.itinvolve.itsm.framework.env.Setup.PATH_SALESFORCE_CREDENTIALS;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.itinvolve.itsm.framework.logs.Log;

/**
 * User Handler class. Provide methods to interact with salesforce users.
 * @author Adrian Espinoza
 *
 */
public class UserHandler {
    private static Map<String, User> CURRENT_USERS_MAP;//only one type
    private static final String SFDC_TYPE_ATTRIBUTE = "sfdc-type";


    private static Map<String, Map<String, User>> AVAILABLE_CREDENTIALS_MAP;
    private static Map<String, String> AVAILABLE_CREDENTIALS_IDS_MAP;
    private static Map<String, String> AVAILABLE_CREDENTIALS_SFDC_TYPES_MAP;

    /**
     * This method return the user in the current session.
     * @return Current user.
     */
    public static User getCurrentUser() {
        if (CURRENT_USERS_MAP == null) {
            return null;
        }
        String threadId = String.valueOf(Thread.currentThread().getId());
        return CURRENT_USERS_MAP.get(threadId);
    }

    public static void setCurrentUser(User newUser) {
        if (CURRENT_USERS_MAP == null) {
            CURRENT_USERS_MAP = new HashMap<String, User>();
        }
        String threadId = String.valueOf(Thread.currentThread().getId());
        CURRENT_USERS_MAP.put(threadId, newUser);
    }

    public static User getUser(Profile userProfile) {
        String threadId = String.valueOf(Thread.currentThread().getId());

        String credentialId = AVAILABLE_CREDENTIALS_IDS_MAP.get(threadId);

        if (AVAILABLE_CREDENTIALS_MAP == null || AVAILABLE_CREDENTIALS_MAP.isEmpty() || !AVAILABLE_CREDENTIALS_MAP.containsKey(credentialId)) {
            Map<String, User> availableUsers = CredentialXmlParser.getUsersFromXmlFile(credentialId, PATH_SALESFORCE_CREDENTIALS);
            putCredential(credentialId, availableUsers);
        }

        User resultUser = AVAILABLE_CREDENTIALS_MAP.get(credentialId).get(userProfile.name());
        if (null == resultUser) {
            System.out.println("Profile " + userProfile + " not defined, please check your credentials setting.");
            Log.logger().error("-> Profile " + userProfile + " not defined, please check your credentials setting.");
        }
        return resultUser;
    }

    public static boolean setup(String credentialId) {
        Map<String, User> availableUsers = CredentialXmlParser.getUsersFromXmlFile(credentialId, PATH_SALESFORCE_CREDENTIALS);

        putCredential(credentialId, availableUsers);
        String threadId = String.valueOf(Thread.currentThread().getId());
        putCredentialId(threadId, credentialId);

        boolean wasSetup = false;
        if (availableUsers.size() > 0) {
            String env = CredentialXmlParser.getAttributeValue(credentialId, SFDC_TYPE_ATTRIBUTE);
            env = StringUtils.isNotBlank(env) ? env.toLowerCase() : null;
            putCredentialSfdcType(threadId, env);
            wasSetup = true;
        }
        return wasSetup;
    }

    public static User setupCurrentUser(Profile userProfile) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        User tmpUser = getUser(userProfile);
        if (CURRENT_USERS_MAP == null) {
            CURRENT_USERS_MAP = new HashMap<String, User>();
        }

        if (CURRENT_USERS_MAP.get(threadId) == null) {
            CURRENT_USERS_MAP.put(threadId, tmpUser);
        } else if (!(currentIsTheSameProfile(userProfile))) {
            CURRENT_USERS_MAP.put(threadId, tmpUser);//review conditional
        }
        return CURRENT_USERS_MAP.get(threadId);
    }

    public static Boolean currentIsTheSameProfile(Profile userProfile) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        User tmpUser = getUser(userProfile);
        return (tmpUser.profile.equals(CURRENT_USERS_MAP.get(threadId).profile));
    }

    public static String getSfdcTypeAttribute() {
        return SFDC_TYPE_ATTRIBUTE;
    }

    public static String getSfdcTypeAttributeValue() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        return AVAILABLE_CREDENTIALS_SFDC_TYPES_MAP.get(threadId);
    }

    public static String getIdAttributeValue() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        return AVAILABLE_CREDENTIALS_IDS_MAP.get(threadId);
    }

    public static void putCredential(String key, Map<String, User> value) {
        if (AVAILABLE_CREDENTIALS_MAP == null) {
            AVAILABLE_CREDENTIALS_MAP = new HashMap<String, Map<String,User>>();
        }
        AVAILABLE_CREDENTIALS_MAP.put(key, value);
    }

    public static void putCredentialId(String key, String value) {
        if (AVAILABLE_CREDENTIALS_IDS_MAP == null) {
            AVAILABLE_CREDENTIALS_IDS_MAP = new HashMap<String, String>();
        }
        AVAILABLE_CREDENTIALS_IDS_MAP.put(key, value);
    }

    public static void putCredentialSfdcType(String key, String value) {
        if (AVAILABLE_CREDENTIALS_SFDC_TYPES_MAP == null) {
            AVAILABLE_CREDENTIALS_SFDC_TYPES_MAP = new HashMap<String, String>();
        }
        AVAILABLE_CREDENTIALS_SFDC_TYPES_MAP.put(key, value);
    }
}
