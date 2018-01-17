/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.soql.QueryBuilder;
import com.itinvolve.itsm.framework.soql.SetCondition;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;


/**
 * Class that contains methods to load data from csv files.
 * @author Adrian Espinoza
 *
 */
public class DataLoader {
    /**
     * This method loads the data from a csv file and save the data in Salesforce data base.
     * @param objectType The SObject api name.
     * @param csvfileName The csv file name.
     * @return A SObject ids list.
     */
    public static List<String> loadData(String objectType, String csvfileName) {
        List<String> idsList = null;
        SObject[] sObjectsRecordList = SObjectFactory.buildSObjects(objectType, csvfileName);
        if (sObjectsRecordList != null && sObjectsRecordList.length > 0) {
            SaveResult[] saveResults = DatabaseOperation.insert(sObjectsRecordList);
            if (saveResults != null) {
                idsList = new ArrayList<String>();
                for (int i=0; i< saveResults.length; i++) {
                    if (saveResults[i].isSuccess()) {
                        idsList.add(saveResults[i].getId());
                    }
                }
            }
        }
        return idsList;
    }

    /**
     * This method loads the data from a csv file and save the data in Salesforce data base.
     * @param objectType The SObject api name.
     * @param csvfileName The csv file name.
     * @return A SObject list.
     */
    public static List<SObject> loadSObjectData(String objectType, String csvfileName) {
        List<String> columnToSelect = MetadataUtils.getFieldNamesList(objectType);
        return loadSObjectData(objectType, csvfileName, columnToSelect);
    }

    /**
     * This method loads the data from a csv file and save the data in Salesforce data base.
     * @param objectType The SObject api name.
     * @param csvFileName The csv file name.
     * @param columnsToSelect The column names list
     * @return A SObject list.
     */
    public static List<SObject> loadSObjectData(String objectType, String csvFileName, List<String> columnsToSelect) {
        List<SObject> sObjectResultList = null;
        List<String> idsList = loadData(objectType, csvFileName);
        if (columnsToSelect != null && !columnsToSelect.isEmpty()) {
            QueryBuilder query = QueryBuilder.select(columnsToSelect).from(objectType).where(SetCondition.in("Id", idsList)).orderBy("Id");
            query.addColumnToSelect("Id");
            QueryResult queryResult = DatabaseOperation.query(query.toSoql());
            SObject[] sObjects = queryResult.getRecords();
            sObjectResultList = new ArrayList<SObject>(Arrays.asList(sObjects));
        } else {
            Log.logger().error("-> Error to trying recover the data from Database after load data >>> column vales [" + columnsToSelect.toString() + "]");
        }
        return sObjectResultList;
    }
}
