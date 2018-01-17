/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.itinvolve.itsm.framework.database.SObjectUtils;

/**
 * Basic condition class. Provide SOQL comparison operator methods for WHERE clause.
 * @author Adrian Espinoza
 *
 */
public class BasicCondition extends Condition {
    /** Store the column to compare */
    private String column;

    /** Store the comparison operator */
    private String matchType;

    /** Store the value to match */
    private String value;


    /**
     * Custom constructor.
     * @param column The column to be compare.
     * @param matchType The comparison operator.
     * @param value The value to be compared.
     */
    private BasicCondition(String column, String matchType, boolean value) {
        setup(SObjectUtils.addPrefix(column), matchType, String.valueOf(value));
    }

    /**
     * Custom constructor.
     * @param column The column to be compare.
     * @param matchType The comparison operator.
     * @param operand The value to be compared.
     */
    private BasicCondition(String column, String operator, Date operand) {
        this(column, operator, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").format(operand));
    }

    /**
     * Custom constructor.
     * @param column The column to be compare.
     * @param matchType The comparison operator.
     * @param value The value to be compared.
     */
    private BasicCondition(String column, String matchType, float value) {
        setup(SObjectUtils.addPrefix(column), matchType, String.valueOf(value));
    }

    /**
     * Custom constructor.
     * @param column The column to be compare.
     * @param matchType The comparison operator.
     * @param value The value to be compared.
     */
    private BasicCondition(String column, String matchType, int value) {
        setup(SObjectUtils.addPrefix(column), matchType, String.valueOf(value));
    }

    /**
     * Custom constructor.
     * @param column The column to be compare.
     * @param matchType The comparison operator.
     * @param value The value to be compared.
     */
    private BasicCondition(String column, String matchType, String value) {
        setup(SObjectUtils.addPrefix(column), matchType, quote(value));
    }

    /**
     * This method return the column to be compared.
     * @return column name.
     */
    public String getColumn() {
        return column;
    }

    /**
     * This method builds the EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition equals(String column, String value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.EQUALS.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition equals(String column, boolean value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.EQUALS.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition equals(String column, Date value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.EQUALS.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition equals(String column, float value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.EQUALS.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition equals(String column, int value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.EQUALS.getOperator(), value);
        return basicCondition;
    }

    public void write(Output out) {
        out.print(column).print(' ').print(matchType).print(' ').print(value);
    }

    /**
     * This method builds the NO EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition notEquals(String column, String value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.NOT_EQUALS.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition notEquals(String column, boolean value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.NOT_EQUALS.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition notEquals(String column, Date value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.NOT_EQUALS.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition notEquals(String column, float value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.NOT_EQUALS.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO EQUALS comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition notEquals(String column, int value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.NOT_EQUALS.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greaterThan(String column, String value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greaterThan(String column, boolean value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greaterThan(String column, Date value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greaterThan(String column, float value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greatherThan(String column, int value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greaterOrEqual(String column, String value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greaterOrEqual(String column, boolean value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greaterOrEqual(String column, Date value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greaterOrEqual(String column, float value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO GREATER OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition greaterOrEqual(String column, int value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.GREATER_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO LESS THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessThan(String column, String value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO LESS THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessThan(String column, boolean value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO LESS THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessThan(String column, Date value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO LESS THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessThan(String column, float value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the NO LESS THAN comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessThan(String column, int value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_THAN.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LESS OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessOrEqual(String column, String value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LESS OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessOrEqual(String column, boolean value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LESS OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessOrEqual(String column, Date value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LESS OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessOrEqual(String column, float value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LESS OR EQUAL comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition lessOrEqual(String column, int value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LESS_OR_EQUAL.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LIKE comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition like(String column, String value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LIKE.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LIKE comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition like(String column, boolean value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LIKE.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LIKE comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition like(String column, Date value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LIKE.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LIKE comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition like(String column, float value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LIKE.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method builds the LIKE comparison operator for WHERE clause.
     * @param column The column to be compare.
     * @param value The value to be compared.
     * @return BasicCondition
     */
    public static BasicCondition like(String column, int value) {
        BasicCondition basicCondition = new BasicCondition(column, Operator.LIKE.getOperator(), value);
        return basicCondition;
    }

    /**
     * This method setups the global variables
     * @param column
     * @param matchType
     * @param value
     */
    private void setup(String column, String matchType, String value) {
        this.column = column;
        this.matchType = matchType;
        this.value = value;
    }
}
