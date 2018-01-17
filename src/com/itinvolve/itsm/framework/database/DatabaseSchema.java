/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.util.Arrays;
import java.util.List;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.soql.AggregateFunction;
import com.itinvolve.itsm.framework.soql.BasicCondition;
import com.itinvolve.itsm.framework.soql.Columns;
import com.itinvolve.itsm.framework.soql.Order;
import com.itinvolve.itsm.framework.soql.QueryBuilder;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * Database Schema class. Provide methods to recover data form database.
 * @author Adrian Espinoza
 *
 */
public class DatabaseSchema {
    /**
     * This method return a record from a specific id
     * @param objectType The SObject api name.
     * @param id The unique identifier of the record.
     * @return A record.
     */
    public static SObject getRecordById(String objectType, String id) {
        List<String> columns = MetadataUtils.getFieldNamesList(Columns.ALL, objectType);
        return getRecordById(objectType, columns, id);
    }

    /**
     * This method return a record from a specific id
     * @param objectType The SObject api name.
     * @param columns The fields to select.
     * @param id The unique identifier of the record.
     * @return A record.
     */
    public static SObject getRecordById(String objectType, List<String> columns, String id) {
        Log.logger().info("-> Getting record of type [" + SObjectUtils.addPrefix(objectType) + "] with record Id [" + id + "]");
        QueryBuilder query = QueryBuilder.select(columns).from(objectType).where(BasicCondition.equals("Id", id)).limit(1);
        List<SObject> records = getRecords(query);
        SObject record = null;
        if ((records != null) && !records.isEmpty()) {
            record = records.get(0);
        }
        return record;
    }

    /**
     * This method returns a records list according the
     * @param objectType
     * @param nameValue
     * @return
     */
    public static List<SObject> getRecordsByMatchField(String objectType, String field, String value) {
        return QueryBuilder.select(MetadataUtils.getFieldNamesList(Columns.ALL, objectType))
        .from(objectType)
        .where(BasicCondition.equals(field, value))
        .toResultList();
    }

    /**
     * This method returns all records of a specific object type.
     * @param objectType The SObject api name.
     * @return A record list.
     */
    public static List<SObject> getAllRecords(String objectType) {
        List<String> columns = MetadataUtils.getFieldNamesList(Columns.ALL, objectType);
        QueryBuilder query = QueryBuilder.select(columns).from(objectType);
        return getRecords(query);
    }

    /**
     * This method returns records according query passed as argument.
     * @param query The soql query to execute.
     * @return A record list.
     */
    public static List<SObject> getRecords(QueryBuilder query) {
        Log.logger().info("-> Getting records from object type [" + query.getTable() + "]");
        QueryResult queryResult = DatabaseOperation.query(query);
        List<SObject> records = null;
        if ((queryResult != null) && (queryResult.getSize() > 0)) {
            SObject[] objectResult = queryResult.getRecords();
            records = Arrays.asList(objectResult);
        } else {
            Log.logger().warn("-> The database for this object type [" + query.getTable() + "] is empty >>> Records not found");
        }
        return records;
    }

    /**
     * This method returns the last record inserted in database.
     * @param objectType The SObject api name.
     * @return A record.
     */
    public static SObject getLastRecord(String objectType) {
        Log.logger().info("-> Getting the last record saved in database of type [" + SObjectUtils.addPrefix(objectType) + "]");
        return getSingleRecord(objectType, "Id", Order.DESC);
    }

    /**
     * This method returns the first record inserted in database.
     * @param objectType The SObject api name.
     * @return A record.
     */
    public static SObject getFirstRecord(String objectType) {
        Log.logger().info("-> Getting the first record saved in database of type [" + SObjectUtils.addPrefix(objectType) + "]");
        return getSingleRecord(objectType, "Id", Order.ASC);
    }

    /**
     * This method return the last record modified into database.
     * @param objectType objectType The SObject api name.
     * @return A record.
     */
    public static SObject getLastRecordModified(String objectType) {
        Log.logger().info("-> Getting the last modified record in database of type [" + SObjectUtils.addPrefix(objectType) + "]");
        return getSingleRecord(objectType, "LastModifiedDate", Order.DESC);
    }

    /**
     * This method counts all records in database of a specific object type.
     * @param objectType The SObject api name.
     * @return A number of records.
     */
    public static int countRecords(String objectType) {
        QueryBuilder query = QueryBuilder.select(AggregateFunction.count()).from(objectType);
        QueryResult queryResult = DatabaseOperation.query(query);
        return queryResult.getSize();
    }

    /**
     * This method counts records in database of a specific query.
     * @param query The soql query to execute.
     * @return A number of records.
     */
    public static int countRecords(QueryBuilder query) {
        QueryResult queryResult = DatabaseOperation.query(query);
        return queryResult.getSize();
    }

    /**
     * This method return a salesforce user according user name.
     * @param username The salesforce user name.
     * @return A record.
     */
    public static SObject getUser(String username) {
        SObject user = QueryBuilder.select(MetadataUtils.getFieldNamesList(Columns.ALL, "User"))
        .from("User")
        .where(BasicCondition.equals("username", username))
        .toSingleResult();
        return user;
    }

    /**
     * This method return a salesforce users list.
     * @return A record list.
     */
    public static List<SObject> getUsers() {
        List<SObject> users = QueryBuilder.select(MetadataUtils.getFieldNamesList(Columns.ALL, "User"))
        .from("User")
        .toResultList();
        return users;
    }

    /**
     * This method returns the first or last record inserted in database according arguments passed.
     * @param objectType The SObject api name.
     * @param orderByField The field name to order.
     * @param order The order type.
     * @return A record.
     */
    private static SObject getSingleRecord(String objectType, String orderByField, Order order) {
        List<String> columns = MetadataUtils.getFieldNamesList(Columns.ALL, objectType);
        QueryBuilder query = QueryBuilder.select(columns).from(objectType).orderBy(orderByField, order).limit(1);
        SObject lastRecord = null;
        List<SObject> queryResult = getRecords(query);
        if (queryResult != null) {
            lastRecord = queryResult.get(0);
        }
        return lastRecord;
    }
}
