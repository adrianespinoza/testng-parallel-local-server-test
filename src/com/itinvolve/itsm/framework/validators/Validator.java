/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.validators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;

import com.itinvolve.itsm.framework.database.DatabaseOperation;
import com.itinvolve.itsm.framework.database.MetadataUtils;
import com.itinvolve.itsm.framework.database.SObjectUtils;
import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.soql.AggregateFunction;
import com.itinvolve.itsm.framework.soql.Function;
import com.itinvolve.itsm.framework.soql.QueryBuilder;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * Validator class. Provide method to validate interface data with database data.
 * @author Adrian Espinoza
 *
 */
public class Validator {
    /**
     * This method compare a string value with value from database.
     * @param value The value to compare.
     * @return A ValueComparer instance.
     */
    public static ValueComparer compare(String value) {
        ValueComparer valueComp = new ValueComparer();
        valueComp.valueTo = value;
        return valueComp;
    }

    /**
     * This method verifies if the sent query has not records.
     * @param query The query instance.
     * @return A true/false value.
     */
    public static boolean isEmpty(QueryBuilder query) {
        String q = query.toSoql();
        QueryResult queryResults = DatabaseOperation.query(q);

        boolean isEmpty = true;
        if (queryResults.getSize() > 0) {
            isEmpty = false;
        }

        return isEmpty;
    }

    /**
     * This method verifies if the sent query has records.
     * @param query The query instance.
     * @return A true/false value.
     */
    public static boolean isNotEmpty(QueryBuilder query) {
        return !isEmpty(query);
    }

    /**
     * This method use the COUNT function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean countCompareTo(QueryBuilder query, Comparator comparator, int expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.COUNT, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     * This method use the COUNT function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean countCompareTo(QueryBuilder query, Comparator comparator, String expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.COUNT, query, comparator, expectedValue);
        return compareResult;
    }

    /**
     * This method use the COUNT function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean countCompareTo(QueryBuilder query, Comparator comparator, float expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.COUNT, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     * This method use the COUNT function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean countCompareTo(QueryBuilder query, Comparator comparator, double expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.COUNT, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the COUNT_DISTINCT function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean countDistincCompareTo(QueryBuilder query, Comparator comparator, int expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.COUNT_DISTINCT, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the COUNT_DISTINCT function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean countDistincCompareTo(QueryBuilder query, Comparator comparator, String expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.COUNT_DISTINCT, query, comparator, expectedValue);
        return compareResult;
    }

    /**
     *  This method use the COUNT_DISTINCT function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean countDistincCompareTo(QueryBuilder query, Comparator comparator, float expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.COUNT_DISTINCT, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the COUNT_DISTINCT function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean countDistincCompareTo(QueryBuilder query, Comparator comparator, double expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.COUNT_DISTINCT, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the MAX function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean maxCompareTo(QueryBuilder query, Comparator comparator, int expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.MAX, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the MAX function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean maxCompareTo(QueryBuilder query, Comparator comparator, String expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.MAX, query, comparator, expectedValue);
        return compareResult;
    }

    /**
     *  This method use the MAX function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean maxCompareTo(QueryBuilder query, Comparator comparator, float expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.MAX, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the MAX function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean maxCompareTo(QueryBuilder query, Comparator comparator, double expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.MAX, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the MIN function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean minCompareTo(QueryBuilder query, Comparator comparator, int expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.MIN, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the MIN function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean minCompareTo(QueryBuilder query, Comparator comparator, String expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.MIN, query, comparator, expectedValue);
        return compareResult;
    }

    /**
     *  This method use the MIN function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean minCompareTo(QueryBuilder query, Comparator comparator, float expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.MIN, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the MIN function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean minCompareTo(QueryBuilder query, Comparator comparator, double expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.MIN, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the SUM function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean sumCompareTo(QueryBuilder query, Comparator comparator, int expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.SUM, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the SUM function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean sumCompareTo(QueryBuilder query, Comparator comparator, String expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.SUM, query, comparator, expectedValue);
        return compareResult;
    }

    /**
     *  This method use the SUM function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean sumCompareTo(QueryBuilder query, Comparator comparator, float expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.SUM, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the SUM function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean sumCompareTo(QueryBuilder query, Comparator comparator, double expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.SUM, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the AVG function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean avgCompareTo(QueryBuilder query, Comparator comparator, int expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.AVG, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     *  This method use the AVG function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean avgCompareTo(QueryBuilder query, Comparator comparator, String expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.AVG, query, comparator, expectedValue);
        return compareResult;
    }

    /**
     * This method use the AVG function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean avgCompareTo(QueryBuilder query, Comparator comparator, float expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.AVG, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     * This method use the AVG function to compare the query value result with expected value.
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean avgCompareTo(QueryBuilder query, Comparator comparator, double expectedValue) {
        boolean compareResult = aggregateFunctionCompareTo(Function.AVG, query, comparator, String.valueOf(expectedValue));
        return compareResult;
    }

    /**
     * This method compares two images
     * @param firstImagePath The first image location.
     * @param secondImagePath The second image location.
     * @return A true/false value.
     */
    public static boolean imageCompareTo(String firstImagePath, String secondImagePath) {
        int score = -1;
        ImagePHash p = new ImagePHash();
        try {
            String image1 = p.getHash(new FileInputStream(new File(firstImagePath)));
            String image2 = p.getHash(new FileInputStream(new File(secondImagePath)));
            score = p.distance(image1, image2);
            System.out.println("-> Score is " + score);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (score == 0);
    }

    /**
     * This method use the any aggregate function to compare the query value result with expected value.
     * @param function
     * @param query The query instance to execute.
     * @param comparator The comparator type.
     * @param expectedValue The expected value.
     * @return A true/false value.
     */
    public static boolean aggregateFunctionCompareTo(Function function, QueryBuilder query, Comparator comparator, String expectedValue) {
        boolean compareResult = false;
        AggregateFunction afCount = query.hasAggregateFunction(function);
        String message = "-> No message";
        if (afCount != null) {
            QueryResult queryResults = DatabaseOperation.query(query.toSoql());
            int records = queryResults.getSize();
            if (afCount.hasColumn() && records > 0) {
                SObject[] objs = queryResults.getRecords();
                if (objs.length == 1) { // only one record
                    String dbValue = String.valueOf(objs[0].getField(afCount.getAlias()));
                    boolean isNumericField = MetadataUtils.isNumericField(query.getTable(), afCount.getColumn());
                    // SUM() AVG() for numeric fields
                    if (((function.equals(Function.AVG) || function.equals(Function.SUM)) && isNumericField) || isNumericField) { // only numeric values.
                        BigDecimal dbValueBigDec = new BigDecimal(dbValue);
                        int temExpectedValue = Integer.valueOf(expectedValue);
                        message = "-> [NUMERIC] COUNT RESULT VALUE [" + dbValue + "] IS " + comparator + " TO EXPECTED VALUE [" + temExpectedValue + "]?";
                        compareResult = comparator.compare(dbValueBigDec, new BigDecimal(temExpectedValue));
                    } else  { // when is not numeric
                        message = "-> [NOT NUMERIC] COUNT RESULT VALUE [" + dbValue + "] IS " + comparator + " TO EXPECTED VALUE [" + expectedValue + "]?";
                        compareResult = comparator.compare(dbValue, expectedValue);
                    }
                } else {// many records currently is returning the number of records
                    int temExpectedValue = Integer.valueOf(expectedValue);
                    compareResult = comparator.compare(new BigDecimal(records), new BigDecimal(temExpectedValue));
                    message = "-> Many records recovered, to compare was taken the total number of records.";
                }
            } else {
                int temExpectedValue = Integer.valueOf(expectedValue);
                compareResult = comparator.compare(new BigDecimal(records), new BigDecimal(temExpectedValue));
                message = "-> [NUMERIC] COUNT RESULT VALUE [" + records + "] IS " + comparator + " TO EXPECTED VALUE [" + temExpectedValue + "]?";
            }
        } else {
            message = "-> The query do not contains the required aggregate function [" + function + "] >>> Query:\n" + SObjectUtils.MSG_SEPARATOR + "\n" + query.toSoql() + "\n" + SObjectUtils.MSG_SEPARATOR;
        }
        Log.logger().info(message);
        return compareResult;
    }
}
