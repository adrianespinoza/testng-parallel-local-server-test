/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.soql.Columns;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;
import com.sforce.ws.ConnectionException;

/**
 * Metadata Utils class. Provide methods for describing sObject
 * @author Adrian Espinoza
 *
 */
public class MetadataUtils {
    /** Store the SObject describes */
    private static Map<String, DescribeSObjectResult> sObjectDescribeResults;

    /** Store the field types by object */
    private static Map<String, Map<String, Field>> sObjectDescribeFieldResults;

    /**
     * This method obtains a SObject field descriptions.
     * @param objectType The SObject api name.
     * @return A list of fields
     */
    public static Field[] getSObjectFieldsDescribe(String objectType) {
        Field[] fields = null;
        // Make the describe call
        DescribeSObjectResult describeSObjectResult = getSObjectDescribe(objectType);
        // Get sObject metadata
        if (describeSObjectResult != null) {
            // Get the fields
            fields = describeSObjectResult.getFields();
        }
        return fields;
    }

    /**
     * This method obtains a SObject descriptions from salesforce org.
     * @param objectType The SObject api name.
     * @return A describe SObejct result.
     */
    public static DescribeSObjectResult getSObjectDescribe(String objectType) {
        DescribeSObjectResult describeSObjectResult = null;
        objectType = SObjectUtils.addPrefix(objectType);
        try {
            if (!getSObjectDescribeResults().containsKey(objectType)) {
                describeSObjectResult = DatabaseConnector.getConnection().describeSObject(objectType);
                sObjectDescribeResults.put(objectType, describeSObjectResult);
            } else {
                describeSObjectResult = sObjectDescribeResults.get(objectType);
            }
        } catch (ConnectionException ce) {
            Log.logger().error("-> Error in connection in to get describe sObject >>> [" + objectType + "]", ce);
            ce.printStackTrace();
        }
        return describeSObjectResult;
    }

    /**
     * This method obtains all field api names of a specific SObject.
     * @param objectType The SObject api name.
     * @return
     */
    public static List<String> getFieldNamesList(String objectType) {
        objectType = SObjectUtils.addPrefix(objectType).toLowerCase();
        if (!getSObjectDescribeFieldResults().containsKey(objectType)) {
            loadFieldDescribeMaps(objectType);
        }
        List<String> fieldResul = new ArrayList<String>(sObjectDescribeFieldResults.get(objectType).keySet());
        return fieldResul;
    }

    /**
     * This method obtains all field api names of a specific SObject.
     * @param objectType The SObject api name.
     * @return
     */
    public static List<String> getRealFieldNamesList(String objectType) {
        objectType = SObjectUtils.addPrefix(objectType).toLowerCase();
        List<String> fieldResul = new ArrayList<String>();
        Field[] fields = getSObjectFieldsDescribe(objectType);
        for (int i = 0; i < fields.length; i++) {
            fieldResul.add(fields[i].getName());
        }
        return fieldResul;
    }

    /**
     * This method obtains all field api names of a specific SObject and field group.
     * @param fieldOpc The group to the belong the fields
     * @param objectType The SObject api name.
     * @return A Set of field names. [convert in list]
     */
    public static List<String> getFieldNamesList(Columns fieldOpc, String objectType) {
        Field[] fields = getSObjectFieldsDescribe(objectType);
        List<String> resultFields = new ArrayList<String>();
        for (Field field : fields) {
            if (fieldOpc.equals(Columns.ALL)) {
                resultFields.add(field.getName());
            } else if (fieldOpc.equals(Columns.CUSTOM) && field.isCustom()) {
                resultFields.add(field.getName());
            } else if (fieldOpc.equals(Columns.STANDARD) && !field.isCustom()) { // is standard
                resultFields.add(field.getName());
            }
        }
        return resultFields;
    }

    /**
     * This method obtains all field describe of a specific object type
     * @param objectType The SObject api name.
     * @return A map with field types
     */
    public static Map<String, FieldType> getFieldTypesMap(String objectType) {
        objectType = SObjectUtils.addPrefix(objectType).toLowerCase();
        if (!getSObjectDescribeFieldResults().containsKey(objectType)){
            loadFieldDescribeMaps(objectType);
        }
        Map<String, Field> fields = sObjectDescribeFieldResults.get(objectType);
        Map<String, FieldType> fieldTypes = null;
        if ((fields != null) && !fields.isEmpty()) {
            fieldTypes = new HashMap<String, FieldType>();
            for (String fieldName : fields.keySet()) {
                fieldTypes.put(fieldName, fields.get(fieldName).getType());
            }
        }
        return fieldTypes;
    }

    /**
     * This method obtains the field type describe of a specific object type and field.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A field type
     */
    public static FieldType getFieldType(String objectType, String fieldName) {
        FieldType fType = null;
        objectType = SObjectUtils.addPrefix(objectType).toLowerCase();
        fieldName = SObjectUtils.addPrefix(fieldName).toLowerCase();
        if (!getSObjectDescribeFieldResults().containsKey(objectType)){
            loadFieldDescribeMaps(objectType);
        }
        fType = sObjectDescribeFieldResults.get(objectType).get(fieldName).getType();
        return fType;
    }



    /**
     * This method fill a map with SObject describe metadata.
     * @return A SObejct describe map.
     */
    public static Map<String, DescribeSObjectResult> getSObjectDescribeResults() {
        if (sObjectDescribeResults == null) {
            sObjectDescribeResults = new HashMap<String, DescribeSObjectResult>();
        }
        return sObjectDescribeResults;
    }

    /**
     * This method fill a map with SObject field types.
     * @return A field type map
     */
    public static Map<String, Map<String, Field>> getSObjectDescribeFieldResults() {
        if (sObjectDescribeFieldResults == null) {
            sObjectDescribeFieldResults = new HashMap<String, Map<String, Field>>();
        }
        return sObjectDescribeFieldResults;
    }

    /**
     * This method verifies if a specific field is a numeric type
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isNumericField(String objectType, String fieldName) {
        boolean isNumeric = false;
        FieldType fieldType = getFieldType(objectType, fieldName);
        if (fieldType != null) {
            if (fieldType.equals(FieldType._double) || fieldType.equals(FieldType._int) || fieldType.equals(FieldType.currency) || fieldType.equals(FieldType.percent)) {
                isNumeric = true;
            }
        } else {
            Log.logger().warn("-> Metadata - The " + fieldName + " field do not belons to " + objectType + " sObject >>> Ask if is numeric field");
        }
        return isNumeric;
    }

    /**
     * This method verifies if a SObject field is double type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isDoubleType(String objectType, String fieldName) {
        boolean isDouble = areEqualTypes(objectType, fieldName, FieldType._double);
        return isDouble;
    }

    /**
     * This method verifies if a SObject field is int type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isIntType(String objectType, String fieldName) {
        boolean isInt = areEqualTypes(objectType, fieldName, FieldType._int);
        return isInt;
    }

    /**
     * This method verifies if a SObject field is boolean type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isBooleanType(String objectType, String fieldName) {
        boolean isInt = areEqualTypes(objectType, fieldName, FieldType._boolean);
        return isInt;
    }

    /**
     * This method verifies if a SObject field is anyType type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isAnyType(String objectType, String fieldName) {
        boolean isInt = areEqualTypes(objectType, fieldName, FieldType.anyType);
        return isInt;
    }

    /**
     * This method verifies if a SObject field is combobox type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isComboboxType(String objectType, String fieldName) {
        boolean isCombobox = areEqualTypes(objectType, fieldName, FieldType.combobox);
        return isCombobox;
    }

    /**
     * This method verifies if a SObject field is currency type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isCurrencyType(String objectType, String fieldName) {
        boolean isCurrency = areEqualTypes(objectType, fieldName, FieldType.currency);
        return isCurrency;
    }

    /**
     * This method verifies if a SObject field is date type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isDateType(String objectType, String fieldName) {
        boolean isDate = areEqualTypes(objectType, fieldName, FieldType.date);
        return isDate;
    }

    /**
     * This method verifies if a SObject field is datetime type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isDatetimeType(String objectType, String fieldName) {
        boolean isDatetime = areEqualTypes(objectType, fieldName, FieldType.datetime);
        return isDatetime;
    }

    /**
     * This method verifies if a SObject field is email type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isEmailType(String objectType, String fieldName) {
        boolean isEmail = areEqualTypes(objectType, fieldName, FieldType.email);
        return isEmail;
    }

    /**
     * This method verifies if a SObject field is id type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isIdType(String objectType, String fieldName) {
        boolean isId = areEqualTypes(objectType, fieldName, FieldType.id);
        return isId;
    }

    /**
     * This method verifies if a SObject field is location type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isLocationType(String objectType, String fieldName) {
        boolean isLocation = areEqualTypes(objectType, fieldName, FieldType.location);
        return isLocation;
    }

    /**
     * This method verifies if a SObject field is multipicklist type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isMultipicklistType(String objectType, String fieldName) {
        boolean isMultipicklist = areEqualTypes(objectType, fieldName, FieldType.multipicklist);
        return isMultipicklist;
    }

    /**
     * This method verifies if a SObject field is percent type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isPercentType(String objectType, String fieldName) {
        boolean isPercent = areEqualTypes(objectType, fieldName, FieldType.percent);
        return isPercent;
    }

    /**
     * This method verifies if a SObject field is phone type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isPhoneType(String objectType, String fieldName) {
        boolean isPhone = areEqualTypes(objectType, fieldName, FieldType.phone);
        return isPhone;
    }

    /**
     * This method verifies if a SObject field is picklist type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isPicklistType(String objectType, String fieldName) {
        boolean isPicklist = areEqualTypes(objectType, fieldName, FieldType.picklist);
        return isPicklist;
    }

    /**
     * This method verifies if a SObject field is reference type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isReferenceType(String objectType, String fieldName) {
        boolean isReference = areEqualTypes(objectType, fieldName, FieldType.reference);
        return isReference;
    }

    /**
     * This method verifies if a SObject field is string type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isStringType(String objectType, String fieldName) {
        boolean isString = areEqualTypes(objectType, fieldName, FieldType.string);
        return isString;
    }

    /**
     * This method verifies if a SObject field is textarea type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isTextareaType(String objectType, String fieldName) {
        boolean isTextarea = areEqualTypes(objectType, fieldName, FieldType.textarea);
        return isTextarea;
    }

    /**
     * This method verifies if a SObject field is time type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isTimeType(String objectType, String fieldName) {
        boolean isTime = areEqualTypes(objectType, fieldName, FieldType.time);
        return isTime;
    }

    /**
     * This method verifies if a SObject field is url type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isUrlType(String objectType, String fieldName) {
        boolean isUrl = areEqualTypes(objectType, fieldName, FieldType.url);
        return isUrl;
    }

    /**
     * This method verifies if a SObject field is base64 type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isBase64Type(String objectType, String fieldName) {
        boolean isBase64 = areEqualTypes(objectType, fieldName, FieldType.base64);
        return isBase64;
    }

    /**
     * This method verifies if a SObject field is datacategorygroupreference type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isDatacategorygroupreferenceType(String objectType, String fieldName) {
        boolean isDatacategorygroupreference = areEqualTypes(objectType, fieldName, FieldType.datacategorygroupreference);
        return isDatacategorygroupreference;
    }

    /**
     * This method returns the describe field
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A describe field.
     */
    public static Field getDescribeField(String objectType, String fieldName) {
        objectType = SObjectUtils.addPrefix(objectType);
        fieldName = SObjectUtils.addPrefix(fieldName);
        if (!getSObjectDescribeFieldResults().containsKey(objectType)) {
            loadFieldDescribeMaps(objectType);
        }
        Field decribeField = sObjectDescribeFieldResults.get(objectType).get(fieldName);
        return decribeField;
    }

    /**
     * This method returns a list of object field describe.
     * @param objectType The SObject api name.
     * @return A list object field describes.
     */
    public static List<Field> getDescribeFieldsList(String objectType) {
        objectType = SObjectUtils.addPrefix(objectType);
        if (!getSObjectDescribeFieldResults().containsKey(objectType)) {
            loadFieldDescribeMaps(objectType);
        }
        List<Field> fieldDescribeResult = new ArrayList<Field>(sObjectDescribeFieldResults.get(objectType).values());
        return fieldDescribeResult;
    }

    /**
     * This method verifies if a field is createable in database.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isCreateableField(String objectType, String fieldName) {
        boolean isCreatable = false;
        Field decribeField = getDescribeField(objectType, fieldName);
        if (decribeField.isCreateable()) {
            isCreatable = true;
        }
        return isCreatable;
    }

    /**
     * This method verifies if a field can be updateable in database.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @return A true/false value.
     */
    public static boolean isUpdateableField(String objectType, String fieldName) {
        boolean isUpdateable = false;
        Field decribeField = getDescribeField(objectType, fieldName);
        if (decribeField.isUpdateable()) {
            isUpdateable = true;
        }
        return isUpdateable;
    }

    /**
     * This method write in log all object fields that are not updateable in database
     * @param objectType The SObject api name.
     */
    public static void writeLogForNotUpdateableFields(String objectType) {
        objectType = SObjectUtils.addPrefix(objectType);
        List<Field> describeFields = getDescribeFieldsList(objectType);
        String message = "";
        for (Field field : describeFields) {
            if (!field.isUpdateable()) {
                message = message + "\t[" + field.getName() + "]\n";
            }
        }
        if (!message.isEmpty()) {
            message = SObjectUtils.MSG_SEPARATOR + "\n-> In the [" + objectType + "] the following fields are not updateables:\n" + message + SObjectUtils.MSG_SEPARATOR;
            Log.logger().info(message);
        }
    }

    /**
     * This method verifies if a SObject field type is equals to a specific type.
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @param typecomparer The field type to compare.
     * @return A true/false value.
     */
    private static boolean areEqualTypes(String objectType, String fieldName, FieldType typecomparer) {
        boolean areEquals = false;
        FieldType fieldType = getFieldType(objectType, fieldName);
        if (fieldType != null) {
            if (fieldType.equals(typecomparer)) {
                areEquals = true;
            }
        } else {
            Log.logger().warn("-> The " + fieldName + " field do not belons to " + objectType + " sObject");
        }
        return areEquals;
    }

    /**
     * This method loads the field describes into map.
     * @param objectType objectType The SObject api name.
     */
    private static void loadFieldDescribeMaps(String objectType) {
        objectType = SObjectUtils.addPrefix(objectType);
        Field[] fields = getSObjectFieldsDescribe(objectType);
        if (fields != null) {
            Map<String, Field> fieldTypesMap = new HashMap<String, Field>();
            for (int i = 0; i < fields.length; i++) {
                String field = fields[i].getName().toLowerCase();
                if(StringUtils.isNotBlank(field)) {
                    fieldTypesMap.put(field, fields[i]);
                }
            }
            sObjectDescribeFieldResults.put(objectType, fieldTypesMap);
        }
    }
}
