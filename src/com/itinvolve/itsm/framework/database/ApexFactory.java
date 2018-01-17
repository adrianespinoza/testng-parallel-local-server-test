/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.soql.QueryBuilder;

/**
 * Apex Factory. provide method to build anonymous apex code.
 * @author Adrian Espinoza
 *
 */
public class ApexFactory {
    /**
     * This method build an apex code to delete sobjects
     * @param query The query to execute.
     * @return An apex code.
     */
    public static String toDeleteSObjects(QueryBuilder query) {
        String apex = "delete [" + query.toSoql() + "]; ";
        return apex;
    }

    /**
     * This method build an apex code to update sobjects
     * @param objectType
     * @param id
     * @param fieldToChange
     * @param value
     * @return
     */
    public static String toUpdateSObject(String objectType, String id, String fieldToChange, String value) {
        fieldToChange = SObjectUtils.addPrefix(fieldToChange);
        objectType = SObjectUtils.addPrefix(objectType);
        String apex = "SObject sObj = [SELECT Id, " + fieldToChange + " FROM " + objectType + " WHERE id='" + id + "' LIMIT 1];\n" +
        "sObj.put('" + fieldToChange + "', " + SObjectUtils.getRealValueApex(objectType, fieldToChange, value) + ");\n" +
        "update new List<SObject>{sObj};";
        return apex;
    }

    /**
     * This method build an apex code to update sobjects
     * @param objectType
     * @param id
     * @param fieldValuesMap
     * @return
     */
    public static String toUpdateSObject(String objectType, String id, Map<String, String> fieldValuesMap) {
        String fieldsToSelect = joinFields(new ArrayList<String>(fieldValuesMap.keySet()), ",");
        objectType = SObjectUtils.addPrefix(objectType);

        String apex = "SObject sObj = [SELECT Id, " + fieldsToSelect + " FROM " + objectType + " WHERE id='" + id + "' LIMIT 1];\n" +
        putFieldValues(objectType, fieldValuesMap) + "\n" +
        "update new List<SObject>{sObj};";
        return apex;
    }

    /**
     * This method build an apex code to update sobjects
     * @param objectType The sObject api name.
     * @param fieldToChange
     * @param value
     * @return
     */
    public static String toUpdateSObjects(String objectType, String fieldToChange, String value) {
        fieldToChange = SObjectUtils.addPrefix(fieldToChange);
        objectType = SObjectUtils.addPrefix(objectType);
        String apex = "List<SObject> sObjList = [SELECT Id, " + fieldToChange + " FROM " + objectType + "];\n" +
        "for (SObject sObj : sObjList) {\n" +
            "\t sObj.put('" + fieldToChange + "', " + SObjectUtils.getRealValueApex(objectType, fieldToChange, value) + ");\n" +
        "}\n" +
        "update sObjList;";
        return apex;
    }

    /**
     * This method build an apex code to update sobjects
     * @param objectType The sObject api name.
     * @param fieldValuesMap
     * @return
     */
    public static String toUpdateSObjects(String objectType, Map<String, String> fieldValuesMap) {
        String fieldsToSelect = joinFields(new ArrayList<String>(fieldValuesMap.keySet()), ",");
        objectType = SObjectUtils.addPrefix(objectType);

        String apex = "List<SObject> sObjList = [SELECT Id, " + fieldsToSelect + " FROM " + objectType + "];\n" +
        "for (SObject sObj : sObjList) {\n" +
        putFieldValues(objectType, fieldValuesMap) +
        "}\n" +
        "update sObjList;";
        return apex;
    }

    /**
     * This method build an apex code to update sobjects
     * @param query
     * @param fieldValuesMap
     * @return
     */
    public static String toUpdateSObjects(QueryBuilder query, Map<String, String> fieldValuesMap) {
        String apex = null;
        if ((query.getSelectColumns() != null) && (query.getSelectColumns().size() > 0)) {
            query.addColumnToSelect("Id");
            apex = "List<SObject> sObjList = [" + query.toSoql() + "];\n" +
            "for (SObject sObj : sObjList) {\n" +
            putFieldValues(query.getTable(), fieldValuesMap) +
            "}\n" +
            "update sObjList;";
        } else {
            Log.logger().warn("-> The query is wrong\n" + query.toSoql());
        }
        return apex;
    }

    /**
     * This method build an apex code to update and delete sobjects
     * @param query
     * @param fieldValuesMap
     * @return
     */
    public static String toUpdateAndDeleteSObjects(QueryBuilder query, Map<String, String> fieldValuesMap) {
        String apex = toUpdateSObjects(query, fieldValuesMap);
        if (apex != null) {
            apex += "\n" + "delete sObjList;";
        }
        return apex;
    }

    /**
     * This method build an apex code to update and delete sobjects
     * @param objectType
     * @param fieldToChange
     * @param value
     * @return
     */
    public static String toUpdateAndDeleteSObjects(String objectType, String fieldToChange, String value) {
        fieldToChange = SObjectUtils.addPrefix(fieldToChange);
        objectType = SObjectUtils.addPrefix(objectType);
        String apex = "List<SObject> sObjList = [SELECT Id, " + fieldToChange + " FROM " + objectType + "];\n" +
        "for (SObject sObj : sObjList) {\n" +
        "\t sObj.put('" + fieldToChange + "', " + SObjectUtils.getRealValueApex(objectType, fieldToChange, value) + ");\n" +
        "}\n" +
        "update sObjList;\n" +
        "delete sObjList;";
        return apex;
    }

    /**
     * This method build an apex code to update and delete sobjects
     * @param objectType
     * @param fieldValuesMap
     * @return
     */
    public static String toUpdateAndDeleteSObjects(String objectType, Map<String, String> fieldValuesMap) {
        String fieldsToSelect = joinFields(new ArrayList<String>(fieldValuesMap.keySet()), ",");
        objectType = SObjectUtils.addPrefix(objectType);
        String apex = "List<SObject> sObjList = [SELECT Id," + fieldsToSelect + " FROM " + objectType + "];\n" +
        "for (SObject sObj : sObjList) {\n" +
        putFieldValues(objectType, fieldValuesMap) +
        "}\n" +
        "update sObjList;\n" +
        "delete sObjList;";
        return apex;
    }

    /**
     * This method update and delete a single sobject in database executing anonymous apex
     * @param objectType The SObject api name.
     * @param id The record id.
     * @param fieldToChange The field to update.
     * @param value The value to set.
     * @return A apex execution status.
     */
    public static String toUpdateAndDeleteSObject(String objectType, String id, String fieldToChange, String value) {
        fieldToChange = SObjectUtils.addPrefix(fieldToChange);
        objectType = SObjectUtils.addPrefix(objectType);
        String apex = "SObject sObj = [SELECT Id, " + fieldToChange + " FROM " + objectType + " WHERE id='" + id + "' LIMIT 1];\n" +
        "sObj.put('" + fieldToChange + "', " + SObjectUtils.getRealValueApex(objectType, fieldToChange, value) + ");\n" +
        "update new List<SObject>{sObj};\n" +
        "delete new List<SObject>{sObj};";
        return apex;
    }

    /**
     * This method update and delete a single sobject in database executing anonymous apex
     * @param objectType The SObject api name.
     * @param id The record id.
     * @param fieldValuesMap The field value map to update.
     * @return A apex execution status.
     */
    public static String toUpdateAndDeleteSObject(String objectType, String id, Map<String, String> fieldValuesMap) {
        String fieldsToSelect = joinFields(new ArrayList<String>(fieldValuesMap.keySet()), ",");
        objectType = SObjectUtils.addPrefix(objectType);

        String apex = "SObject sObj = [SELECT Id, " + fieldsToSelect + " FROM " + objectType + " WHERE id='" + id + "' LIMIT 1];\n" +
        putFieldValues(objectType, fieldValuesMap) +
        "update new List<SObject>{sObj};\n" +
        "delete new List<SObject>{sObj};\n";
        return apex;
    }

    /**
     * This method generates the changes that will be execute in apex.
     * @param objectType The SObject api name.
     * @param fieldValuesMap The field value map to change
     * @return A apex code as string.
     */
    private static String putFieldValues(String objectType, Map<String, String> fieldValuesMap) {
        String changes = "";
        String fieldToChange;
        for (String field : fieldValuesMap.keySet()) {
            fieldToChange = SObjectUtils.addPrefix(field);
            changes += "\t sObj.put('" + fieldToChange + "', " + SObjectUtils.getRealValueApex(objectType, fieldToChange, fieldValuesMap.get(field)) + ");\n";
        }
        return changes;
    }

    /**
     * This method joins a list of strings with a specific separator.
     * @param valuesToJoin The values to join.
     * @param separator The join separator.
     * @return A string joined.
     */
    private static String joinFields(List<String> valuesToJoin, String separator) {
        SObjectUtils.addPrefix(valuesToJoin);
        String fieldsJoined = StringUtils.join(valuesToJoin, separator);
        return fieldsJoined;
    }
}
