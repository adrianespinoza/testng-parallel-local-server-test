/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.util.Map;

import com.itinvolve.itsm.framework.soql.QueryBuilder;


/**
 * Apex Database, Provide methods to execute SOQL DML operations.
 * @author Adrian Espinoza
 *
 */
public class ApexDatabase {
    /**
     * This method update a single sobject in database executing anonymous apex
     * @param objectType
     * @param id
     * @param fieldToChange
     * @param value
     * @return
     */
    public static boolean update(String objectType, String id, String fieldToChange, String value) {
        String apex = ApexFactory.toUpdateSObject(objectType, id, fieldToChange, value);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method update a single sobject in database executing anonymous apex
     * @param objectType
     * @param id
     * @param fieldValuesMap
     * @return
     */
    public static boolean update(String objectType, String id, Map<String, String> fieldValuesMap) {
        String apex = ApexFactory.toUpdateSObject(objectType, id, fieldValuesMap);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method update sobjects in database executing anonymous apex
     * @param query
     * @param fieldValuesMap
     * @return
     */
    public static boolean update(QueryBuilder query, Map<String, String> fieldValuesMap) {
        String apex = ApexFactory.toUpdateSObjects(query, fieldValuesMap);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method delete a single sobject in database executing anonymous apex
     * @param query The query to execute.
     * @return A apex execution status.
     */
    public static boolean delete(QueryBuilder query) {
        String apex = ApexFactory.toDeleteSObjects(query);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method update a whole of sobjects in database executing anonymous apex.
     * @param objectType The SObject api name.
     * @param fieldToChange The field to update.
     * @param value The value to set.
     * @return A apex execution status.
     */
    public static boolean update(String objectType, String fieldToChange, String value) {
        String apex = ApexFactory.toUpdateSObjects(objectType, fieldToChange, value);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method update a whole of sobjects in database executing anonymous apex.
     * @param objectType The SObject api name.
     * @param fieldValuesMap The field value map to change.
     * @return A apex execution status.
     */
    public static boolean update(String objectType, Map<String, String> fieldValuesMap) {
        String apex = ApexFactory.toUpdateSObjects(objectType, fieldValuesMap);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method update and delete a whole of sobject in database executing anonymous apex
     * @param objectType The SObject api name.
     * @param fieldToChange The field to update.
     * @param value The value to set.
     * @return A apex execution status.
     */
    public static boolean updateAndDelete(String objectType, String fieldToChange, String value) {
        String apex = ApexFactory.toUpdateAndDeleteSObjects(objectType, fieldToChange, value);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method update and delete a whole of sobject in database executing anonymous apex
     * @param objectType The SObject api name.
     * @param fieldValuesMap The field value map to change.
     * @return A apex execution status.
     */
    public static boolean updateAndDelete(String objectType, Map<String, String> fieldValuesMap) {
        String apex = ApexFactory.toUpdateAndDeleteSObjects(objectType, fieldValuesMap);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method update and delete a whole of sobject in database executing anonymous apex
     * @param query
     * @param fieldValuesMap
     * @return
     */
    public static boolean updateAndDelete(QueryBuilder query, Map<String, String> fieldValuesMap) {
        String apex = ApexFactory.toUpdateAndDeleteSObjects(query, fieldValuesMap);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method update and delete a single sobject in database executing anonymous apex
     * @param objectType The SObject api name.
     * @param id The record id.
     * @param fieldToChange The field to update.
     * @param value The value to set.
     * @return A apex execution status.
     */
    public static boolean updateAndDelete(String objectType, String id, String fieldToChange, String value) {
        String apex = ApexFactory.toUpdateAndDeleteSObject(objectType, id, fieldToChange, value);
        return ApexExecutor.executeApex(apex);
    }

    /**
     * This method update and delete a single sobject in database executing anonymous apex
     * @param objectType The SObject api name.
     * @param id The record id.
     * @param fieldValuesMap The field value map to update.
     * @return A apex execution status.
     */
    public static boolean updateAndDelete(String objectType, String id, Map<String, String> fieldValuesMap) {
        String apex = ApexFactory.toUpdateAndDeleteSObject(objectType, id, fieldValuesMap);
        return ApexExecutor.executeApex(apex);
    }
}
