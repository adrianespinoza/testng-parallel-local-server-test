/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.soql.QueryBuilder;
import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.Error;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;

/**
 * Class that allows to execute the DML statements and SOQL queries.
 * @author Adrian Espinoza
 *
 */
public class DatabaseOperation {
    /** Store the operation limit to insert */
    private static final int INSERT_LIMIT = 200;

    /** Store the operation limit to delete */
    private static final int DELETE_LIMIT = 200;

    /** Store the operation limit to update */
    private static final int UPDATE_LIMIT = 200;

    /**  */
    private static boolean LOG_SUCCESS_STATUS = false;

    /**
     * This method executes the SOQL query.
     * @param query The SOQL query as string.
     * @return A query result with the records recovered.
     */
    public static QueryResult query(String query) {
        QueryResult queryResults = null;
        Log.logger().info("-> Query to execute: \n" + SObjectUtils.MSG_SEPARATOR + "\n" + query + "\n" + SObjectUtils.MSG_SEPARATOR);
        try {
            queryResults = DatabaseConnector.getConnection().query(query);
            Log.logger().info("-> The query was executed successfully!");
        } catch (ConnectionException e) {
            Log.logger().error("-> Error in query execution >>> " + e.getMessage(), e);
            e.printStackTrace();
        }
        return queryResults;
    }

    /**
     * This method executes the SOQL query.
     * @param query The SOQL query as instance.
     * @return A query result with the records recovered.
     */
    public static QueryResult query(QueryBuilder query) {
        return query(query.toSoql());
    }

    /**
     * This method execute the insert DML operation.
     * @param records The records list to insert.
     * @return A save result of the  insert operation.
     */
    public static SaveResult[] insert(List<SObject> records) {
        SObject[] recordsToInsert = new SObject[records.size()];
        records.toArray(recordsToInsert);
        return insert(recordsToInsert);
    }

    /**
    * This method execute the insert DML operation.
    * @param records The records list to insert.
    * @return A save result of the  insert operation.
    */
    public static SaveResult[] insert(SObject[] records) {
        SaveResult[] saveResults = null;
        int cantRecordsToInsert = records.length;

        if (cantRecordsToInsert > INSERT_LIMIT) {
             Log.logger().warn("-> EXCEEDED ID LIMIT record limit reached. cannot submit more than " + INSERT_LIMIT +
                    " records into this call >>> records to insert [" + cantRecordsToInsert + "]" +
                    "\n-> EXCEEDED ID LIMIT the records will be inserted by parts");

            List<List<SObject>> sObjectRecordParts = SObjectUtils.chopped(records, INSERT_LIMIT);
            List<SaveResult> saveResultsList = new ArrayList<SaveResult>();
            SaveResult[] saveResultsTemp = null;
            for (List<SObject> list : sObjectRecordParts) {
                SObject[] objsRecordsList = new SObject[list.size()];
                list.toArray(objsRecordsList);
                saveResultsTemp = dbInsert(objsRecordsList);
                saveResultsList.addAll(Arrays.asList(saveResultsTemp));
            }
            saveResults = new SaveResult[saveResultsList.size()];
            saveResultsList.toArray(saveResults);
        } else {
            saveResults = dbInsert(records);
        }
        return saveResults;
    }

    /**
     * This method execute the delete DML operation.
     * @param ids The sobject ids list.
     * @return A delete result of the delete operation.
     */
    public static DeleteResult[] delete(List<String> ids) {
        String[] toDeleteIds = new String[ids.size()];
        ids.toArray(toDeleteIds);
        return delete(toDeleteIds);
    }

    /**
     * This method execute the delete DML operation.
     * @param ids The sobject ids list.
     * @return A delete result of the delete operation.
     */
    public static DeleteResult[] delete(String[] ids) {
        DeleteResult[] deleteResults = null;
        int cantRecordsToDelete = ids.length;

        if (cantRecordsToDelete > DELETE_LIMIT) {
            Log.logger().warn("-> EXCEEDED ID LIMIT delete id limit reached: " + DELETE_LIMIT +
                    " >>> records to delete [" + cantRecordsToDelete + "]" +
                    "\n-> EXCEEDED ID LIMIT the records will be deleted by parts");

            List<List<String>> sObjectIdParts = SObjectUtils.chopped(ids, DELETE_LIMIT);
            List<DeleteResult> deleteResultsList = new ArrayList<DeleteResult>();
            DeleteResult[] deleteResultsTemp = null;
            for (List<String> list : sObjectIdParts) {
                String[] idsList = new String[list.size()];
                list.toArray(idsList);
                deleteResultsTemp = dbDelete(idsList);
                deleteResultsList.addAll(Arrays.asList(deleteResultsTemp));
            }
            deleteResults = new DeleteResult[deleteResultsList.size()];
            deleteResultsList.toArray(deleteResults);
        } else {
            deleteResults = dbDelete(ids);
        }
        return deleteResults;
    }

    /**
     * This method execute the delete DML operation [warn] the SObjects had better have id defined.
     * @param records The SObject records to delete.
     * @return A delete result of the delete operation.
     */
    public static DeleteResult[] deleteSObjects(List<SObject> records) {
        String[] ids = SObjectUtils.getIdsAsArray(records);
        DeleteResult[] deleteResults = delete(ids);
        return deleteResults;
    }

    /**
     * This method execute the delete DML operation [warn] the SObjects had better have id defined.
     * @param records The SObject records to delete.
     * @return A delete result of the delete operation.
     */
    public static DeleteResult[] delete(SObject[] records) {
        String[] ids = SObjectUtils.getIdsAsArray(records);
        DeleteResult[] deleteResults = delete(ids);
        return deleteResults;
    }

    /**
     * This method execute the delete DML operation from a query.
     * @param query The query to be execute then delete the records.
     * @return A delete result of the delete operation.
     */
    public static DeleteResult[] delete(QueryBuilder query) { // no allowed [GROUP BY, AGGREGATE FUNCTION]
        DeleteResult[] deleteResults = null;
        if (!query.hasAggregateFunction() && !query.hasGroupBy() && query.hasSelectColumns()) {
            query.addColumnToSelect("Id");
            QueryResult queryResult = DatabaseOperation.query(query.toSoql());
            SObject[] recordsToDelete = queryResult.getRecords();
            deleteResults = DatabaseOperation.delete(recordsToDelete);
        } else { // DML operation DELETE not allowed on AggregateResult
            Log.logger().error("-> DML operation DELETE not allowed on AggregateResult >>> Unexpected query:\n" +
                    SObjectUtils.MSG_SEPARATOR + "\n" + query.toSoql() + "\n" + SObjectUtils.MSG_SEPARATOR);
        }
        return deleteResults;
    }

    /**
     * This method delete all records according object type.
     * @param objectType The SObject api name.
     * @return A delete result of the delete operation.
     */
    public static DeleteResult[] deleteAllByObjectType(String objectType) {
        DeleteResult[] deleteResults = null;
        QueryBuilder query = QueryBuilder.select("Id").from(objectType);
        QueryResult queryResult = DatabaseOperation.query(query.toSoql());
        SObject[] recordsToDelete = queryResult.getRecords();
        deleteResults = DatabaseOperation.delete(recordsToDelete);
        return deleteResults;
    }

    /**
     * This method executes the update DML operation.
     * @param records The SObject records to update.
     * @return A save result of the update operation.
     */
    public static SaveResult[] update(List<SObject> records) {
        SObject[] toUpdate = new SObject[records.size()];
        records.toArray(toUpdate);
        return update(toUpdate);
    }

    /**
     * This method executes the update DML operation.
     * @param records The SObject records to update.
     * @return A save result of the update operation.
     */
    public static SaveResult[] update(SObject[] records) {
        SaveResult[] saveResults = null;
        SObject[] recordsToUpdate = convertToUpdate(records);
        int cantRecordsToUpdate = recordsToUpdate.length;

        if (cantRecordsToUpdate > UPDATE_LIMIT) {
            Log.logger().warn("-> EXCEEDED ID LIMIT record limit reached. cannot submit more than " + UPDATE_LIMIT +
                    " records into this call >>> records to update [" + cantRecordsToUpdate + "]" +
                    "\n-> EXCEEDED ID LIMIT the records will be updated by parts");

            List<List<SObject>> sObjectRecordParts = SObjectUtils.chopped(recordsToUpdate, UPDATE_LIMIT);
            List<SaveResult> saveResultsList = new ArrayList<SaveResult>();
            SaveResult[] saveResultsTemp = null;
            for (List<SObject> list : sObjectRecordParts) {
                SObject[] objsRecordsList = new SObject[list.size()];
                list.toArray(objsRecordsList);
                saveResultsTemp = dbUpdate(objsRecordsList);
                saveResultsList.addAll(Arrays.asList(saveResultsTemp));
            }
            saveResults = new SaveResult[saveResultsList.size()];
            saveResultsList.toArray(saveResults);
        } else {
            saveResults = dbUpdate(recordsToUpdate);
        }
        return saveResults;
    }

    /**
     * This method execute the insert DML operation.
     * @param records The records list to insert.
     * @return A save result of the  insert operation.
     */
     private static SaveResult[] dbInsert(SObject[] records) {
        SaveResult[] saveResults = null;
        String objectType = records.length > 0 ? records[0].getType() : "Not defined";
        String message = "-> Creating " + records.length + " records in database of type [" + objectType + "]\n" + SObjectUtils.MSG_SEPARATOR + "\n";
        try {// create the records in Salesforce.com
            saveResults = DatabaseConnector.getConnection().create(records);
            int cantCreated = 0;
            int cantNotCreated = 0;
            // check the returned results for any errors
            for (int i=0; i< saveResults.length; i++) {
                if (saveResults[i].isSuccess()) {
                    cantCreated++;
                    if (LOG_SUCCESS_STATUS) {
                        message += "-> " + i + ". Successfully created record - Id: " + saveResults[i].getId() + "\n";
                    }
                } else {
                    cantNotCreated++;
                    Error[] errors = saveResults[i].getErrors();
                    for (int j=0; j< errors.length; j++) {
                        message += "-> ERROR creating record: " + errors[j].getMessage() + "\n";
                    }
                }
            }
            if (cantNotCreated == 0) {
                message += "-> All records were created successfully >>> created [" + cantCreated + "]";
            } else {
                message += "-> Not all records were created successfully >>> created [" + cantCreated + "] not created [" + cantNotCreated + "]";
            }
            message += "\n" + SObjectUtils.MSG_SEPARATOR;
            Log.logger().info(message);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return saveResults;
     }

     /**
      * This method execute the delete DML operation.
      * @param ids The sobject ids list.
      * @return A delete result of the delete operation.
      */
     private static DeleteResult[] dbDelete(String[] ids) {
        DeleteResult[] deleteResults = null;
        String message = "-> Deleting " + ids.length + " records from database\n" + SObjectUtils.MSG_SEPARATOR + "\n";
        try {
            // delete the records in Salesforce.com by passing an array of Ids
            deleteResults = DatabaseConnector.getConnection().delete(ids);
            int cantDeleted = 0;
            int cantNotDeleted = 0;
            // check the results for any errors
            for (int i=0; i< deleteResults.length; i++) {
                if (deleteResults[i].isSuccess()) {
                    cantDeleted++;
                    if (LOG_SUCCESS_STATUS) {
                        message += "-> " + i + ". Successfully deleted record - Id: " + deleteResults[i].getId() + "\n";
                    }
                } else {
                    cantNotDeleted++;
                    Error[] errors = deleteResults[i].getErrors();
                    for (int j=0; j< errors.length; j++) {
                        message += "-> ERROR deleting record: " + errors[j].getMessage() + "\n";
                    }
                }
            }
            if (cantNotDeleted == 0) {
                message += "-> All records were deleted successfully >>> deleted [" + cantDeleted + "]";
            } else {
                message += "-> Not all records were deleted successfully >>> deleted [" + cantDeleted + "] not deleted [" + cantNotDeleted + "]";
            }
            message += "\n" + SObjectUtils.MSG_SEPARATOR;
            Log.logger().info(message);
        } catch (ConnectionException e) {
            message = "-> Web Service Connector Error: Error to trying to delete data";
            Log.logger().error(message, e);
            e.printStackTrace();
        }
        return deleteResults;
     }

     /**
      * This method executes the update DML operation.
      * @param records The SObject records to update.
      * @return A save result of the update operation.
      */
     private static SaveResult[] dbUpdate(SObject[] records) {
        SaveResult[] saveResults = null;
        String objectType = records.length > 0 ? records[0].getType() : "Not defined";
        String message = "-> Updating " + records.length + " records in database of type [" + objectType + "]\n" + SObjectUtils.MSG_SEPARATOR + "\n";
        try {
            // update the records in Salesforce.com
            saveResults = DatabaseConnector.getConnection().update(records);
            int cantUpdated = 0;
            int cantNotUpdated = 0;
            // check the returned results for any errors
            for (int i=0; i< saveResults.length; i++) {
                if (saveResults[i].isSuccess()) {
                    cantUpdated++;
                    if (LOG_SUCCESS_STATUS) {
                        message += "-> " + i + ". Successfully updated record - Id: " + saveResults[i].getId() + "\n";
                    }
                } else {
                    cantNotUpdated++;
                    Error[] errors = saveResults[i].getErrors();
                    for (int j=0; j< errors.length; j++) {
                        message += "-> ERROR updating record: " + errors[j].getMessage() + "\n";
                    }
                }
            }
            if (cantNotUpdated == 0) {
                message += "-> All records were updated successfully >>> updated [" + cantUpdated + "]";
            } else {
                message += "-> Not all records were updated successfully >>> updated [" + cantUpdated + "] not updated [" + cantNotUpdated + "]";
            }
            message += "\n" + SObjectUtils.MSG_SEPARATOR;
            Log.logger().info(message);
        } catch (ConnectionException e) {
            Log.logger().error("-> Web Service Connector Error: Error to trying to update data", e);
            e.printStackTrace();
        }
        return saveResults;
     }

     /**
      * This method convert a SObject list to allows execute update operation.
      * @param records The SObject records to convert update able
      * @return A updatable list
      */
     private static SObject[] convertToUpdate(SObject[] records) {
         String objectType = records[0].getType();
         List<String> fields = MetadataUtils.getRealFieldNamesList(objectType);
         List<SObject> sObjToSave = new ArrayList<SObject>();

         MetadataUtils.writeLogForNotUpdateableFields(objectType);

         for (int i = 0; i < records.length; i++) {
             SObject newSObject = SObjectUtils.createAndFillSObjectToUpdate(records[i], fields);
             sObjToSave.add(newSObject);
         }
         SObject[] recordsToSave = new SObject[sObjToSave.size()];
         sObjToSave.toArray(recordsToSave);
         return recordsToSave;
     }
}
