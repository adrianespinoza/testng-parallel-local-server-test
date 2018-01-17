/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

import org.apache.commons.lang3.StringUtils;

import com.itinvolve.itsm.framework.database.SObjectUtils;


/**
 * Aggregate function class. Provide aggregate function methods for SELECT clause.
 * @author Adrian Espinoza
 *
 */
public class AggregateFunction implements Soqlable {
    /** Store the SObject field api name */
    private String column;

    /** Store the function to execute */
    private Function function;

    /** Store the column alias */
    private String alias; // if is not specified salesforce put expr0

    /**
     * Custom constructor.
     * @param function The aggregate function.
     */
    private AggregateFunction(Function function) {
        this.function = function;
    }

    /**
     * Custom constructor.
     * @param function The aggregate function.
     * @param fieldName The SObject field api name.
     */
    private AggregateFunction(Function function, String fieldName) {
        this.function = function;
        this.column = SObjectUtils.addPrefix(fieldName);
        this.alias = "expr0"; // by default first column take careful with many columns
    }

    /**
     * Custom constructor.
     * @param function The aggregate function.
     * @param fieldName The SObject field api name.
     * @param alias
     */
    private AggregateFunction(Function function, String fieldName, String alias) {
        this.function = function;
        this.column = SObjectUtils.addPrefix(fieldName);
        this.alias = alias;
    }

    /**
     * This method returns the SObject field api name.
     * @return The field name.
     */
    public String getColumn() {
        return column;
    }

    /**
     * This method verifies if exist a column name.
     * @return A true/false value.
     */
    public boolean hasColumn() {
        return column != null;
    }

    /**
     * This method return the SOQL aggregate function.
     * @return The aggregate function.
     */
    public Function getFunction() {
        return function;
    }

    /**
     * This method return the column's alias.
     * @return The alias.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * This method verifies if alias exist in the aggregate function.
     * @return A true/false value.
     */
    public boolean hasAlias() {
        return alias != null;
    }

    /**
     * This method build a SOQL COUNT aggregate function.
     * @param fieldName The SObject field api name.
     * @return An aggregate function.
     */
    public static AggregateFunction count() {
        AggregateFunction aggFunction = new AggregateFunction(Function.COUNT);
        return aggFunction;
    }

    /**
     * This method build a SOQL COUNT aggregate function with a SObejct field api name.
     * @param fieldName The SObject field api name.
     * @return An aggregate function.
     */
    public static AggregateFunction count(String fieldName) {
        AggregateFunction aggFunction = new AggregateFunction(Function.COUNT, fieldName);
        return aggFunction;
    }

    /**
     * This method build a SOQL COUNT aggregate function with a SObejct field api name.
     * @param fieldName The SObject field api name.
     * @return An aggregate function.
     */
    public static AggregateFunction count(String fieldName, String alias) {
        AggregateFunction aggFunction = new AggregateFunction(Function.COUNT, fieldName, alias);
        return aggFunction;
    }

    /**
     * This method build a SOQL AVG aggregate function with a SObejct field api name.
     * @param fieldName The SObject field api name.
     * @return An aggregate function.
     */
    public static AggregateFunction avg(String fieldName) {
        AggregateFunction aggFunction = new AggregateFunction(Function.AVG, fieldName);
        return aggFunction;
    }

    /**
     * This method build a SOQL AVG aggregate function with a SObejct field api name.
     * @param fieldName The SObject field api name.
     * @param alias The alias that belongs to field name.
     * @return An AVG aggregate function.
     */
    public static AggregateFunction avg(String fieldName, String alias) {
        AggregateFunction aggFunction = new AggregateFunction(Function.AVG, fieldName, alias);
        return aggFunction;
    }

    /**
     * This method build a SOQL COUNT_DISTINCT aggregate function with a SObejct field api name.
     * @param fieldName The SObject field api name.
     * @return A COUNT_DISTINCT aggregate function.
     */
    public static AggregateFunction countDistinct(String fieldName) {
        AggregateFunction aggFunction = new AggregateFunction(Function.COUNT_DISTINCT, fieldName);
        return aggFunction;
    }

    /**
     * This method build a SOQL COUNT_DISTINCT aggregate function with a SObejct field api name and alias.
     * @param fieldName The SObject field api name.
     * @param alias The alias that belongs to field name.
     * @return A COUNT_DISTINCT aggregate function.
     */
    public static AggregateFunction countDistinct(String fieldName, String alias) {
        AggregateFunction aggFunction = new AggregateFunction(Function.COUNT_DISTINCT, fieldName, alias);
        return aggFunction;
    }

    /**
     * This method build a SOQL MIN aggregate function with a SObejct field api name.
     * @param fieldName The SObject field api name.
     * @return A MIN aggregate function.
     */
    public static AggregateFunction min(String fieldName) {
        AggregateFunction aggFunction = new AggregateFunction(Function.MIN, fieldName);
        return aggFunction;
    }

    /**
     * This method build a SOQL MIN aggregate function with a SObejct field api name and alias.
     * @param fieldName The SObject field api name.
     * @param alias The alias that belongs to field name.
     * @return A MIN aggregate function.
     */
    public static AggregateFunction min(String fieldName, String alias) {
        AggregateFunction aggFunction = new AggregateFunction(Function.MIN, fieldName, alias);
        return aggFunction;
    }

    /**
     * This method build a SOQL MAX aggregate function with a SObejct field api name.
     * @param fieldName The SObject field api name.
     * @return A MAX aggregate function.
     */
    public static AggregateFunction max(String fieldName) {
        AggregateFunction aggFunction = new AggregateFunction(Function.MAX, fieldName);
        return aggFunction;
    }

    /**
     * This method build a SOQL MAX aggregate function with a SObejct field api name and alias.
     * @param fieldName The SObject field api name.
     * @param alias The alias that belongs to field name.
     * @return A MAX aggregate function.
     */
    public static AggregateFunction max(String fieldName, String alias) {
        AggregateFunction aggFunction = new AggregateFunction(Function.MAX, fieldName, alias);
        return aggFunction;
    }

    /**
     * This method build a SOQL SUM aggregate function with a SObejct field api name.
     * @param fieldName The SObject field api name.
     * @return A SUM aggregate function.
     */
    public static AggregateFunction sum(String fieldName) {
        AggregateFunction aggFunction = new AggregateFunction(Function.SUM, fieldName);
        return aggFunction;
    }

    /**
     * This method build a SOQL SUM aggregate function with a SObejct field api name.
     * @param fieldName The SObject field api name.
     * @param alias The alias that belongs to field name.
     * @return A SUM aggregate function.
     */
    public static AggregateFunction sum(String fieldName, String alias) {
        AggregateFunction aggFunction = new AggregateFunction(Function.SUM, fieldName, alias);
        return aggFunction;
    }

    /* (non-Javadoc)
     * @see com.itinvolve.itsm.framework.soql.Soqlable#write(com.itinvolve.itsm.framework.soql.Output)
     */
    @Override
    public void write(Output out) {
        if (StringUtils.isNotBlank(column)) {
            if (StringUtils.isNotBlank(alias) && !alias.equals("expr0")) {
                out.print(function + "( " + column + " ) " + alias);
            } else {
                out.print(function + "( " + column + " )");
            }
        } else {
            out.print(function + "()");
        }
    }
}
