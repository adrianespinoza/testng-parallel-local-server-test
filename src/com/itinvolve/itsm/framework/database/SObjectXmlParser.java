/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class contains methods to read a special xml files (.object)
 * @author Adrian Espinoza
 *
 */
public class SObjectXmlParser {
    /** Store a document builder */
    private static DocumentBuilder docBuilder;

    static {
        init();
    }

    /**
     * This method allows to read a xml file from a specific location and for a specific SObject.
     * @param sObjectApiName The SObject api name.
     * @param path The file location.
     * @return A SObject wrapper.
     */
    public static SObjectWrapper readSObject(String sObjectApiName, String path) {
        Document doc;
        SObjectWrapper sObj = null;
        Map<String, FieldWrapper> fieldDescribe;
        System.out.println("sobject xml file path: " + path);
        try {
            doc = docBuilder.parse(new File(path));
            doc.getDocumentElement().normalize();
            NodeList nodesRoot = doc.getDocumentElement().getElementsByTagName("fields");

            fieldDescribe = new HashMap<String, FieldWrapper>();
            for (int i = 0; i < nodesRoot.getLength(); i++) {
                Element xmlElem = (Element)nodesRoot.item(i);
                FieldWrapper field = buildFields(xmlElem);
                if (field != null) {
                    fieldDescribe.put((String) field.fieldDescription.get("fullName"), field);
                }
            }
            sObj = new SObjectWrapper();
            sObj.fullName = sObjectApiName;
            sObj.fieldDescribe.putAll(fieldDescribe);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sObj;
    }

    /**
     * This method allows build SObject wrapper fields.
     * @param xmlElement The XML element.
     * @return A SObject wrapper field.
     */
    private static FieldWrapper buildFields(Element xmlElement) {
        FieldWrapper field = null;
        Map<String, Object> fieldDescription = new HashMap<String, Object>();
        NodeList childNode = xmlElement.getChildNodes();

        field = new FieldWrapper();

        for (int i = 0; i < childNode.getLength(); i++) {
            String key = childNode.item(i).getNodeName();
            if (!key.equals("#text")) {
                if(key.equals("picklist")) {
                    NodeList childChildNode = ((Element) childNode.item(i)).getElementsByTagName("picklistValues");
                    List<FieldWrapper.PicklistValues> picklistValeus = new ArrayList<FieldWrapper.PicklistValues>();
                    for (int j = 0; j < childChildNode.getLength(); j++) {
                        FieldWrapper.PicklistValues persist = field.new PicklistValues();
                        persist.fullName =  ((Element) childChildNode.item(j)).getElementsByTagName("fullName").item(0).getTextContent();
                        persist.isdefault =  ((Element) childChildNode.item(j)).getElementsByTagName("default").item(0).getTextContent();;
                        picklistValeus.add(persist);
                    }
                    fieldDescription.put(key, picklistValeus);
                } else {
                    String val = childNode.item(i).getTextContent();
                    fieldDescription.put(key, val);
                }
            }
        }
        field.fieldDescription = fieldDescription;
        return field;
    }

    /**
     * This method initializes the document builder.
     */
    private static void init() {
        DocumentBuilderFactory docBuildFactory = DocumentBuilderFactory.newInstance();
        try {
            docBuilder = docBuildFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
