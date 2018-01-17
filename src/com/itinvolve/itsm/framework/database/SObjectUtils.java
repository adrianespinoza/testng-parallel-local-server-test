/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.database;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

import com.itinvolve.itsm.framework.env.Setup;
import com.itinvolve.itsm.framework.logs.Log;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.sobject.SObject;

/**
 * SObject Utils class. Provide methods to work with Salesforce SObject
 * @author Adrian Espinoza
 *
 */
public class SObjectUtils {

    /** Store a output message separator */
    public static final String MSG_SEPARATOR = StringUtils.repeat("-", 80);

    /** Store the date format */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /** Store the date time format */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";

    /**
     * This method creates an SObject.
     * @param type The SObject api name.
     * @return A SObject created.
     */
    public static SObject createSObject(String type) {
        String realType = addPrefix(type);
        SObject sObj = new SObject();
        sObj.setType(realType);
        return sObj;
    }

    /**
     * This method creates a new SObject to update
     * @param objBase The SObject base to update that contains Id
     * @return A new SObject to update
     */
    public static SObject createSObjectToUpdate(SObject objBase) {
        String type = objBase.getType();
        String id = objBase.getId();
        SObject sObj = new SObject();
        sObj.setType(type);
        sObj.setId(id);
        return sObj;
    }


    /**
     * This method adds the org prefix to the field list.
     * @param fieldApiNamesList The SObjec field api names list.
     */
    public static void addPrefix(List<String> fieldApiNamesList) {
        if (!Setup.ORG_PREFIX.isEmpty()) {
            String field;
            for (int i = 0; i < fieldApiNamesList.size(); i++) {
                field = fieldApiNamesList.get(i);
                String element = addPrefix(field);
                fieldApiNamesList.set(i, element);
            }
        }
    }

    /**
     * This method adds the org prefix to the field list.
     * @param fieldApiNamesList The SObjec field api names list.
     */
    public static void addPrefix(String[] fieldApiNamesList) {
        if (!Setup.ORG_PREFIX.isEmpty()) {
            String field;
            for (int i = 0; i < fieldApiNamesList.length; i++) {
                field = fieldApiNamesList[i];
                String element = addPrefix(field);
                fieldApiNamesList[i] = element;
            }
        }
    }

    /**
     * This method adds prefix to either SObject api name or SObject field.
     * @param type The value to add prefix.
     * @return A value with prefix.
     */
    public static String addPrefix(String type) {
        String withPrefix = type;
        String[] fields = StringUtils.split(type, ".");
        for (int i = 0; i < fields.length; i++) {
            fields[i] = putPrefix(fields[i]);
        }

        withPrefix = StringUtils.join(fields, ".");
        return withPrefix;
    }

    /**
     * This method adds prefix to either SObject api name or SObject field.
     * @param type The value to add prefix.
     * @return A value with prefix.
     */
    public static String putPrefix(String type) {
        String withPrefix = type;
        type = type.toLowerCase();
        if (StringUtils.isNotBlank(Setup.ORG_PREFIX) && !StringUtils.contains(type, Setup.ORG_PREFIX.toLowerCase()) && (StringUtils.contains(type, Setup.SOBJECT_STANDARD_POSTFIX.toLowerCase()) || StringUtils.contains(type, Setup.SOBJECT_REFERENCE_POSTFIX.toLowerCase()))) {
            withPrefix = Setup.ORG_PREFIX + type;
        }
        return withPrefix.toLowerCase();
    }

    /**
     * This method removes the prefix either of SObject api name or SObject field.
     * @param type The value to remove prefix.
     * @return A values without prefix.
     */
    public static String removePrefix(String type) {
        String withoutPrefix = type;
        if (!Setup.ORG_PREFIX.isEmpty()) {
            withoutPrefix = withoutPrefix.replace(Setup.ORG_PREFIX, "");
        }
        return withoutPrefix;
    }

    /**
     * This method converts a field to reference field.
     * @param fieldApiName The field api name.
     * @return A field api name as reference.
     */
    public static String convertToReferenceField(String fieldApiName) {
        fieldApiName = fieldApiName.replace(Setup.SOBJECT_STANDARD_POSTFIX, Setup.SOBJECT_REFERENCE_POSTFIX);
        return fieldApiName;
    }

    /**
     * This method chop a ArrayList into smaller parts.
     * @param <T> Generic type
     * @param list The ArrayList to chop.
     * @param lenght The size to chop.
     * @return A ArrayList chopped.
     */
    public static <T> List<List<T>> chopped(List<T> list, final int lenght) {
        List<List<T>> parts = new ArrayList<List<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += lenght) {
            parts.add(new ArrayList<T>(
                list.subList(i, Math.min(N, i + lenght)))
            );
        }
        return parts;
    }

    /**
     * This method chop a Array into smaller parts.
     * @param <T> Generic type
     * @param list The Array to chop.
     * @param lenght The size to chop.
     * @return A ArrayList chopped.
     */
    public static <T> List<List<T>> chopped(T[] list, final int lenght) {
        List<List<T>> parts = new ArrayList<List<T>>();
        List<T> listData = Arrays.asList(list);
        final int N = listData.size();
        for (int i = 0; i < N; i += lenght) {
            parts.add(new ArrayList<T>(
                    listData.subList(i, Math.min(N, i + lenght)))
            );
        }
        return parts;
    }

    /**
     * This method retrieves all ids from SObject list
     * @param records The SObject records
     * @return A ids Array
     */
    public static String[] getIdsAsArray(SObject[] records) {
        List<String> ids = getIdsAsArrayList(records);
        String[] resultIds = new String[ids.size()];
        ids.toArray(resultIds);
        return resultIds;
    }

    /**
     * This method retrieves all ids from SObject list
     * @param records The SObject records
     * @return A ids Array
     */
    public static String[] getIdsAsArray(List<SObject> records) {
        SObject[] rec = new SObject[records.size()];
        List<String> ids = getIdsAsArrayList(records.toArray(rec));
        String[] resultIds = new String[ids.size()];
        ids.toArray(resultIds);
        return resultIds;
    }

    /**
     * This method retrieves all ids from SObject list
     * @param records records The SObject records
     * @return A ids Array
     */
    public static List<String> getIdsAsArrayList(SObject[] records) {
        List<String> ids = new ArrayList<String>();
        for (int i = 0; i < records.length; i++) {
            if (records[i].getId() != null) {
                ids.add(records[i].getId());
            }
        }
        return ids;
    }

    /**
     * This method creates and fill data to a new SObject.
     * @param sObject The SObject record.
     * @param fields The real field names[e.g. in DB Cmp__CustomField__c do not convert to cmp__customfield__c]
     * @return A new SObject filled.
     */
    public static SObject createAndFillSObjectToUpdate(SObject sObject, List<String> fields) {
        SObject sObj = createSObjectToUpdate(sObject);
        fields.remove("Id");
        String objectType = sObject.getType();
        Object value;
        Object tempValue;

        boolean isUpdateableField;

        for (String field : fields) {
            isUpdateableField = MetadataUtils.isUpdateableField(objectType, field);
            if (isUpdateableField) {
                tempValue = sObject.getField(field);
                if (tempValue != null) {
                    value = getRealValue(objectType, field, String.valueOf(sObject.getField(field)));
                    sObj.setField(field, value);
                }
            }
        }
        return sObj;
    }

    /**
     * This method recover the real value to save as SObject
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @param value The value to convert to any type.
     * @return A real value.
     */
    public static Object getRealValue(String objectType, String fieldName, String value) {
        Object realValue = value;
        if (MetadataUtils.isNumericField(objectType, fieldName)) {
            realValue = Double.valueOf(value);
        } else if (MetadataUtils.isBooleanType(objectType, fieldName)){
            realValue = Boolean.valueOf(value);
        } else if (MetadataUtils.isDateType(objectType, fieldName)){// dates datetime
            realValue = getCalendarDate(value);
        } else if (MetadataUtils.isDatetimeType(objectType, fieldName)) {
            realValue = getCalendarDateTime(value);
        }
        return realValue;
    }

    /**
     * This method recover the real value to save as SObject
     * @param objectType The SObject api name.
     * @param fieldName The SObject field api name.
     * @param value The value to convert to any type.
     * @return A real value.
     */
    public static String getRealValueApex(String objectType, String fieldName, String value) {
        Object realValue = value;
        if (MetadataUtils.isNumericField(objectType, fieldName)) {
            realValue = Double.valueOf(value);
        } else if (MetadataUtils.isBooleanType(objectType, fieldName)){
            realValue = Boolean.valueOf(value);
        } else if (MetadataUtils.isDateType(objectType, fieldName)){// dates datetime
            realValue = quote(getCalendarDate(value).toString());
        } else if (MetadataUtils.isDatetimeType(objectType, fieldName)) {
            realValue = quote(getCalendarDateTime(value).toString());
        } else {
            realValue = quote(value);
        }
        return realValue.toString();
    }

    /**
     * This method puts quotes to string values.
     * @param value The value to put the quotes.
     * @return A string value with quotes.
     */
    public static String quote(String value) {
        if (value == null) {
            return "null";
        }
        StringBuffer str = new StringBuffer();
        str.append('\'');
        for (int i = 0; i < value.length(); i++) {
            if ((value.charAt(i) == '\\') || (value.charAt(i) == '\"') || (value.charAt(i) == '\'')) {
                str.append('\\');
            }
            str.append(value.charAt(i));
        }
        str.append('\'');
        return str.toString();
    }

    /**
     * @param stringDate The date as string [e.g. format expected: 2013-10-15]
     * @return A Calendar date.
     */
    public static Calendar getCalendarDate(String stringDate) {
        return getCalendar(stringDate, DATE_FORMAT);
    }

    /**
     * @param stringDate The date time as string [e.g. format expected: 2013-10-15 16:16:39]
     * @return A Calendar date.
     */
    public static Calendar getCalendarDateTime(String stringDate) {
        stringDate = convertSfDateTimeToLocalDateTime(stringDate);
        return getCalendar(stringDate, DATE_TIME_FORMAT);
    }

    /**
     * This method converts the Salesforce date time format to local date time format
     * @param sfDateTime The Salesforce date time.
     * @return A local date time format.
     */
    public static String convertSfDateTimeToLocalDateTime(String sfDateTime) {
        //"2013-10-15T16:16:38.000Z"
        String stringDateTime = sfDateTime;
        if (stringDateTime.indexOf("T") > 0) {
            stringDateTime = stringDateTime.replace("T", " ");
        }
        if (stringDateTime.indexOf(".") > 0) {
            stringDateTime = stringDateTime.substring(0, stringDateTime.indexOf("."));
        }
        if (stringDateTime.indexOf("Z") > 0) {
            stringDateTime = stringDateTime.substring(0, stringDateTime.indexOf("Z"));
        }
        return stringDateTime;
    }

    /**
     * This method convert a string date to calendar date.
     * @param stringDate The date as String
     * @return A Calendar date.
     */
    public static Calendar getCalendar(String stringDate, String stringFormat) {
        Calendar calendarDate = null;
        try {
            DateFormat format = new SimpleDateFormat(stringFormat);

            TimeZone zone = TimeZone.getTimeZone("GMT");
            format.setTimeZone(zone);
            Date dateFormat = format.parse(stringDate);

            calendarDate = Calendar.getInstance();
            calendarDate.setTime(dateFormat);
        } catch (ParseException e) {
            Log.logger().error("-> Unparseable date, expected format [" + stringFormat + "] for date [" + stringDate + "]>>> " + e.getMessage(), e);
            e.printStackTrace();
        }
        return calendarDate;
    }

    /**
     * This method return the a value of a specific field.
     * @param record The SObject record.
     * @param fieldName The SObject field name.
     * @return A record value.
     */
    public static Object getFieldValue(SObject record, String fieldName) {
        Field field = MetadataUtils.getDescribeField(record.getType(), fieldName);
        Object value = record.getField(field.getName());
        return value;
    }

    /**
     * This method set a specific record value.
     * @param record The SObject record.
     * @param fieldName The SObject field name.
     * @param value The value to set.
     */
    public static void setFieldValue(SObject record, String fieldName, Object value) {
        Field field = MetadataUtils.getDescribeField(record.getType(), fieldName);
        record.setField(field.getName(), value);
    }
}
