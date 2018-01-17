/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

import java.util.Collection;
import java.util.Iterator;

import com.itinvolve.itsm.framework.database.SObjectUtils;

/**
 * Set condition class. Provide SOQL comparison operator methods for WHERE clause.
 * @author Adrian Espinoza
 *
 */
public class SetCondition extends Condition{
    /** Store the set condition operator */
    private String operator;

    /** Store the column to compare */
    private String column;

    /** Store the value to match */
    private String value;

    /** Store the subquery */
    private QueryBuilder subSelect;

    /**
     * Custom constructor.
     * @param operator The set condition operator.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as collection.
     */
    @SuppressWarnings("rawtypes")
    private SetCondition(String operator, String column, Collection values) {
        this.operator = operator;
        this.column = SObjectUtils.addPrefix(column);
        StringBuffer sBuffer = new StringBuffer();
        Iterator i = values.iterator();
        boolean hasNext = i.hasNext();
        while (hasNext) {
            Object curr = i.next();
            hasNext = i.hasNext();
            if (curr instanceof Number) {
                sBuffer.append(curr);
            } else {
                sBuffer.append(quote(curr.toString()));
            }
            if (hasNext) sBuffer.append(',');
        }
        this.value = sBuffer.toString();
    }

    /**
     * Custom constructor.
     * @param operator The set condition operator.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare (String[]).
     */
    private SetCondition(String operator, String column, String[] values) {
        this.operator = operator;
        this.column = SObjectUtils.addPrefix(column);
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            sBuffer.append(quote(values[i]));
            if (i < values.length - 1) {
                sBuffer.append(',');
            }
        }
        this.value = sBuffer.toString();
    }

    /**
     * Custom constructor.
     * @param operator The set condition operator.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare (int[]).
     */
    private SetCondition(String operator, String column, int[] values) {
        this.operator = operator;
        this.column = SObjectUtils.addPrefix(column);
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            sBuffer.append(values[i]);
            if (i < values.length - 1) {
                sBuffer.append(',');
            }
        }
        this.value = sBuffer.toString();
    }

    /**
     * Custom constructor.
     * @param operator The set condition operator.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare (float[]).
     */
    private SetCondition(String operator, String column, float[] values) {
        this.operator = operator;
        this.column = SObjectUtils.addPrefix(column);
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < values.length; i++) {
            sBuffer.append(values[i]);
            if (i < values.length - 1) {
                sBuffer.append(',');
            }
        }
        this.value = sBuffer.toString();
    }

    /**
     * Custom constructor.
     * @param operator The set condition operator.
     * @param column The column name (SObejct field api name).
     * @param subSelect The subquery builded as instance.
     */
    private SetCondition(String operator, String column, QueryBuilder subSelect) {
        this.operator = operator;
        this.column = SObjectUtils.addPrefix(column);
        this.subSelect = subSelect;
    }

    /**
     * Custom constructor.
     * @param operator The set condition operator.
     * @param column The column name (SObejct field api name).
     * @param subSelect The subquery builded as string.
     */
    private SetCondition(String operator, String column, String subSelect) {
        this.operator = operator;
        this.column = SObjectUtils.addPrefix(column);
        this.value = subSelect;
    }

    /**
     * This method builds the IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as Collection.
     */
    @SuppressWarnings("rawtypes")
    public static SetCondition in(String column, Collection values) {
        SetCondition setCond = new SetCondition(Operator.IN.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as String[].
     */
    public static SetCondition in(String column, String[] values) {
        SetCondition setCond = new SetCondition(Operator.IN.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as int[].
     */
    public static SetCondition in(String column, int[] values) {
        SetCondition setCond = new SetCondition(Operator.IN.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as float[].
     */
    public static SetCondition in(String column, float[] values) {
        SetCondition setCond = new SetCondition(Operator.IN.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param subSelect The values to compare as Query instance.
     */
    public static SetCondition in(String column, QueryBuilder subSelect) {
        SetCondition setCond = new SetCondition(Operator.IN.getOperator(), column, subSelect);
        return setCond;
    }

    /**
     * This method builds the IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param subSelect The values to compare as string.
     */
    public static SetCondition in(String column, String subSelect) {
        SetCondition setCond = new SetCondition(Operator.IN.getOperator(), column, subSelect);
        return setCond;
    }

    /**
     * This method builds the NOT IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as collection.
     */
    @SuppressWarnings("rawtypes")
    public static SetCondition notIn(String column, Collection values) {
        SetCondition setCond = new SetCondition(Operator.NO_IN.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the NOT IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as String[].
     */
    public static SetCondition notIn(String column, String[] values) {
        SetCondition setCond = new SetCondition(Operator.NO_IN.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the NOT IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as int[].
     */
    public static SetCondition notIn(String column, int[] values) {
        SetCondition setCond = new SetCondition(Operator.NO_IN.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the NOT IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as float[].
     */
    public static SetCondition notIn(String column, float[] values) {
        SetCondition setCond = new SetCondition(Operator.NO_IN.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the NOT IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param subSelect The values to compare as Query instance.
     */
    public static SetCondition notIn(String column, QueryBuilder subSelect) {
        SetCondition setCond = new SetCondition(Operator.NO_IN.getOperator(), column, subSelect);
        return setCond;
    }

    /**
     * This method builds the NOT IN comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param subSelect The values to compare as string.
     */
    public static SetCondition notIn(String column, String subSelect) {
        SetCondition setCond = new SetCondition(Operator.NO_IN.getOperator(), column, subSelect);
        return setCond;
    }

    /**
     * This method builds the EXCLUDES comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as collection.
     */
    @SuppressWarnings("rawtypes")
    public static SetCondition excludes(String column, Collection values) {
        SetCondition setCond = new SetCondition(Operator.EXCLUDES.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the EXCLUDES comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as String[].
     */
    public static SetCondition excludes(String column, String[] values) {
        SetCondition setCond = new SetCondition(Operator.EXCLUDES.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the INCLUDES comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as collection.
     */
    @SuppressWarnings("rawtypes")
    public static SetCondition includes(String column, Collection values) {
        SetCondition setCond = new SetCondition(Operator.INCLUDES.getOperator(), column, values);
        return setCond;
    }

    /**
     * This method builds the INCLUDES comparison operator for WHERE clause.
     * @param column The column name (SObejct field api name).
     * @param values The values to compare as String[].
     */
    public static SetCondition includes(String column, String[] values) {
        SetCondition setCond = new SetCondition(Operator.INCLUDES.getOperator(), column, values);
        return setCond;
    }

    public String getColumn() {
        return column;
    }

    /* (non-Javadoc)
     * @see com.itinvolve.itsm.framework.soql.Condition#write(com.itinvolve.itsm.framework.soql.Output)
     */
    @Override
    public void write(Output out) {
        out.print(column);
        out.println(" " + this.operator + " (");

        out.indent();
        if (subSelect != null) {
            subSelect.write(out);
        } else {
            out.print(value);
        }
        out.unindent();

        out.println();
        out.print(")");
    }
}
