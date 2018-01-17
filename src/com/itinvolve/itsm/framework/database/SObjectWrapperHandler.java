/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.util.HashMap;
import java.util.Map;

import com.itinvolve.itsm.framework.env.Setup;

/**
 * This class contains methods to manage the SObject wrapper.
 * @author Adrian Espinoza
 *
 */
public class SObjectWrapperHandler {
    /** Store the SObject wrapper */
    private static Map<String, SObjectWrapper> sobjectsMap;

    /**
     * This method retrieves a SObject wrapper from local cache.
     * @param sObjectApiName The SObject api name.
     * @return A SObject wrapper.
     */
    public static SObjectWrapper getSObject(String sObjectApiName) {
        SObjectWrapper sObjResult;
        String sObjApiNameWithoutPrefix = SObjectUtils.removePrefix(sObjectApiName);
        String fullPath = Setup.SOBJECT_FILE_PATH + sObjApiNameWithoutPrefix + ".object";
        if (sobjectsMap != null) {
            if (sobjectsMap.containsKey(sObjApiNameWithoutPrefix)) {
                sObjResult = sobjectsMap.get(sObjApiNameWithoutPrefix);
            } else {
                sObjResult = SObjectXmlParser.readSObject(sObjApiNameWithoutPrefix, fullPath);
            }
        } else {
            sobjectsMap = new HashMap<String, SObjectWrapper>();
            sObjResult = SObjectXmlParser.readSObject(sObjApiNameWithoutPrefix, fullPath);
        }
        sobjectsMap.put(sObjApiNameWithoutPrefix, sObjResult);
        return sObjResult;
    }
}
