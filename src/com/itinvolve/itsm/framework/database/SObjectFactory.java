/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.util.ArrayList;
import java.util.List;

import com.itinvolve.itsm.framework.logs.Log;
import com.sforce.soap.partner.sobject.SObject;

/**
 * This class contains method to create SObjects with data from csv file.
 * @author Adrian Espinoza
 *
 */
public class SObjectFactory {
    /**
     * This method builds SObjects with data from csv file.
     * @param objectType The SObject api name.
     * @param csvFileName The csv file name.
     * @return A record list without save them.
     */
    public static SObject[] buildSObjects(String objectType, String csvFileName) {
        SObject[] objsResultList = null;
        //SObjectWrapper objWrapper = SObjectWrapperHandler.getSObject(sObjectApiName);
        List<List<String>> dataFromCsvFile = CsvFileReader.readFile(csvFileName);

        List<String> fields = MetadataUtils.getFieldNamesList(objectType);

        if (dataFromCsvFile != null && !dataFromCsvFile.isEmpty() && fields != null) {
            List<String> sobjecApiNamesList = valuesToLowercase(fields);//valuesToLowercase(new ArrayList<String>(objWrapper.getFieldApiNames()));
            SObjectUtils.addPrefix(dataFromCsvFile.get(0));
            List<String> csvApiNamesList = valuesToLowercase(dataFromCsvFile.get(0));

            boolean containsAll = sobjecApiNamesList.containsAll(csvApiNamesList);
            if (containsAll) {
                objsResultList = mappingDataToSObjects(objectType, dataFromCsvFile);
            } else {
                Log.logger().error("-> One or more fields from csv file do not belongs to SObject [" + objectType + "]\n" +
                        SObjectUtils.MSG_SEPARATOR + "\n" +
                        "-> Fields from csv >>> " + csvApiNamesList + "\n" +
                        "-> Fields from obj >>> " + sobjecApiNamesList + "\n" +
                        SObjectUtils.MSG_SEPARATOR);
            }
        } else {
            Log.logger().error("-> Problems on csv reader: " + dataFromCsvFile);
        }
        return objsResultList;
    }

    /**
    * This method mapping data with rows retrieved from csv files.
    * @param objectType The SObject api name.
    * @param dataFromCsvFile The list of rows with data from csv file.
    * @return a SObject list builded.
    */
    public static SObject[] mappingDataToSObjects(String objectType, List<List<String>> dataFromCsvFile) {
        List<String> fieldApiNamesList = dataFromCsvFile.get(0);//Always the first element are field api names
        SObjectUtils.addPrefix(fieldApiNamesList);// try to put the org prefix to field names

        int fieldValuesSize = fieldApiNamesList.size();
        int dataValuesSize = dataFromCsvFile.size();
        int startIndex = 1;
        int index = 0;

        SObject[] sobjectsResultList = new SObject[dataValuesSize - 1];
        String fieldName;
        Object value;
        for (int i = startIndex; i < dataValuesSize; i++) {
            List<String> fieldValuesList = dataFromCsvFile.get(i);//row data
            SObject obj;
            if (fieldValuesList.size() > 0) {// at least bring one value
                obj = SObjectUtils.createSObject(objectType);
                for (int j = 0; j < fieldValuesSize; j++) {//issue value types...
                    fieldName = fieldApiNamesList.get(j);
                    value = SObjectUtils.getRealValue(objectType, fieldName, fieldValuesList.get(j));
                    obj.setField(fieldName, value);
                }
                sobjectsResultList[index] = obj;
                index++;
            }
        }
        return sobjectsResultList;
    }

    /**
     * This method converts a string list values to lower case.
     * @param toConventList The string list to convert
     * @return A string list with values converted to lower case.
     */
    public static List<String> valuesToLowercase(List<String> toConventList) {
        List<String> lowercaseValues = new ArrayList<String>();
        for (String value : toConventList) {
            lowercaseValues.add(value.toLowerCase().replaceAll("\\s+",""));
        }
        return lowercaseValues;
    }
}
