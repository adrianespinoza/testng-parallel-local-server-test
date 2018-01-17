/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.sfusers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.itinvolve.itsm.framework.logs.Log;

/**
 * XML parser class. Provide methods to read user credendial file.
 * @author Adrian Espinoza
 *
 */
public class CredentialXmlParser {
    /** Store the xml document builder */
    private static DocumentBuilder docBuilder;
    private static Map<String, Element> credentialElementsMap;

    static {
        init();
    }

    /**
     * This method retrieve an attribute value.
     * @param attributeName
     * @return The attribute value.
     */
    public static String getAttributeValue(String credentialId, String attributeName) {
        String value = null;
        if (credentialElementsMap != null && credentialElementsMap.containsKey(credentialId)) {
            Element element = credentialElementsMap.get(credentialId);
            if (element != null && element.hasAttribute(attributeName)) {
                value = element.getAttribute(attributeName);
            } else {
                Log.logger().error("-> The credentials for " + attributeName + " is not defined.");
            }
        }
        return value;
    }

    /**
     * This method read the credendials file and return the users defined into this file.
     * @param path The location of the credentials file.
     * @return A map of users according profile.
     */
    public static Map<String, User> getUsersFromXmlFile(String credentialId, String path) {
        Map<String, User> usersMap = new HashMap<String, User>();
        Document doc;
        try {
            String credentialIdValue = credentialId;

            if (!credentialIdValue.equals("${credential}")) {
                Log.logger().info("-> Loaging user profiles from: " + path);
                doc = docBuilder.parse(new File(path));
                doc.getDocumentElement().normalize();
                NodeList nodesRoot = doc.getDocumentElement().getElementsByTagName("credential");

                boolean credentialIsnotDefined = true;

                for (int i = 0; i < nodesRoot.getLength(); i++) {
                    Element elem = (Element)nodesRoot.item(i);
                    if (elem.hasAttribute("id") && elem.getAttribute("id").equals(credentialIdValue)) {
                         NodeList adminChildNodes = elem.getElementsByTagName("admin");
                         fillUserMapByReference(adminChildNodes, usersMap);

                         NodeList userChildNodes = elem.getElementsByTagName("user");
                         fillUserMapByReference(userChildNodes, usersMap);

                         putCredentialElement(credentialId, elem);
                         //credentialElement = elem;
                         credentialIsnotDefined = false;
                         break;
                    }
                }
                if (credentialIsnotDefined) {
                    Log.logger().error("-> The credentials for " + credentialIdValue + " are not defined.");
                } else {
                    Log.logger().info("-> The user profiles were loaded successfully from: " + path + " to " + credentialIdValue + " credential.");
                }
            } else {
                Log.logger().error("-> The credential type have to be enter by command, current value is " + credentialIdValue);
            }
        } catch (SAXException e) {
            Log.logger().error("-> SAX Exception occurred while trying to load user profiles from: " + path, e);
            e.printStackTrace();
        } catch (IOException e) {
            Log.logger().error("-> IO Exception occurred while trying to load user profiles from: " + path, e);
            e.printStackTrace();
        }
        return usersMap;
    }

    /**
     * This method fills the users map according XML node.
     * @param nodesList The XML nodes list.
     * @param toFillMap The map to fill with node list information.
     */
    private static void fillUserMapByReference(NodeList nodesList, Map<String, User> toFillMap) {
        if (toFillMap != null && nodesList != null) {
            Element elem;
            for (int i = 0; i < nodesList.getLength(); i++) {
                User usr = new User();
                elem = (Element) nodesList.item(i);
                if (elem.hasAttribute("id")) {
                    usr.profile = Profile.DEFAULT.getProfile(elem.getAttribute("id"));
                }
                usr.name = elem.getAttribute("name");
                usr.password = elem.getAttribute("password");
                usr.token = elem.getAttribute("token");

                toFillMap.put(usr.profile.name(), usr);
            }
        }
    }

    public static void putCredentialElement(String key, Element value) {
        if (credentialElementsMap == null) {
            credentialElementsMap = new HashMap<String, Element>();
        }
        credentialElementsMap.put(key, value);
    }

    /**
     * This method initializes the XML document builder.
     */
    private static void init() {
        DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docBuildFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            Log.logger().error("Parser Configuration Exception", e);
            e.printStackTrace();
        }
    }
}
