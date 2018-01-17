/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.itinvolve.itsm.framework.database.DatabaseSchema;
import com.itinvolve.itsm.framework.database.MetadataUtils;
import com.itinvolve.itsm.framework.database.SObjectUtils;
import com.sforce.soap.partner.sobject.SObject;

/**
 * Query class. Provide SOQL methods for build soql queries as string
 * @author Adrian Espinoza
 *
 */
public class QueryBuilder implements Soqlable {
    /** Store the condition criterias */
    private List<Condition> criteria;

    /** Store the table name */
    private String table;

    /** Store the column names */
    private List<String> columns;

    private Columns selectColumns;

    /** Store the column name for order */
    private String orderByColumn;

    /** Store the order type */
    private Order order;

    /** Store the order null type to null fields */
    private OrdersNull ordersNull;

    /** Store the aggregate function list */
    private List<Soqlable> aggregateFunctions;

    /** Store the column names for group them */
    private List<String> groupByColumns;

    private Set<String> allQueryColumnsSet;

    private int limit = -1;

    /**
     * This method returns the table name.
     * @return A table name.
     */
    public String getTable() {
        return table;
    }

    /**
     * This method returns the criteria condition.
     * @return The criteria condition.
     */
    public List<Condition> getCriteria() {
        if (criteria == null) {
            criteria = new ArrayList<Condition>();
        }
        return criteria;
    }

    /**
     * This method returns a list of aggregate functions presented in query.
     * @return A list of aggregate function.
     */
    public List<Soqlable> getAggregateFunctions() {
        if (aggregateFunctions == null) {
            aggregateFunctions = new ArrayList<Soqlable>();
        }
        return aggregateFunctions;
    }

    /**
     * This method verifies if exist an aggregate function.
     * @return A true/false value.
     */
    public boolean hasAggregateFunction() {
        return ((aggregateFunctions != null) && !aggregateFunctions.isEmpty());
    }

    /**
     * This method verifies if the query has GROUP BY clause.
     * @return A true/false value.
     */
    public boolean hasGroupBy() {
        return ((groupByColumns != null) && !groupByColumns.isEmpty());
    }

    public List<String> getGroupByColumns() {
        if (groupByColumns == null) {
            groupByColumns = new ArrayList<String>();
        }
        return groupByColumns;
    }

    /**
     * This method verifies if exist columns in the current query.
     * @return A true/false value.
     */
    public boolean hasSelectColumns() {
        return ((columns != null) && !columns.isEmpty());
    }

    /**
     * This method returns the select values.
     * @return An array of columns
     */
    public List<String> getSelectColumns() {
        if (columns == null) {
            columns = new ArrayList<String>();
        }
        return columns;
    }

    /**
     * This method verifies if contains a column name.
     * @param columnName The column name.
     * @return A true/false value.
     */
    public boolean containsSelectColumn(String columnName) {
        boolean contains = false;
        columnName = SObjectUtils.addPrefix(columnName);
        if (hasSelectColumns()) {
            contains = columns.contains(columnName);
        }
        return contains;
    }

    /**
     * This method add a new column name to do query.
     * @param column A column name.
     */
    public void addColumnToSelect(String column) {
        if ((columns != null) && !containsSelectColumn(column)) {
            column = SObjectUtils.addPrefix(column);
            columns.add(column);
        }
    }

    /**
     * This method returns all column names presented in the query.
     * @return A set of column names.
     */
    public Set<String> getAllColumns() {
        if (allQueryColumnsSet == null) {
            allQueryColumnsSet = new HashSet<String>();
        }
        return allQueryColumnsSet;
    }

    public static QueryBuilder select(Columns columns) {
        QueryBuilder builder = new QueryBuilder();
        builder.selectColumns = columns;
        return builder;
    }

    /**
     * This method builds the SOQL SELECT clause.
     * @param columns The column name or names(separated by commas).
     * @return A query instance.
     */
    public static QueryBuilder select(String columns) {
        columns = StringUtils.deleteWhitespace(columns);
        String[] tempColumns = StringUtils.split(columns, ",");
        return select(Arrays.asList(tempColumns));
    }

    /**
     * This method builds the SOQL SELECT clause.
     * @param columns The column names as Array.
     * @return A query instance.
     */
    public static QueryBuilder select(String... columns) {
        return select(Arrays.asList(columns));
    }

    /**
     * This method builds the SOQL SELECT clause.
     * @param columns The column names as Set.
     * @return A query instance.
     */
    public static QueryBuilder select(Set<String> columns) {
        return select(new ArrayList<String>(columns));
    }

    /**
     * This method builds the SOQL SELECT clause.
     * @param columns The column names as Array.
     * @return A query instance.
     */
    public static QueryBuilder select(List<String> columns) {
        SObjectUtils.addPrefix(columns);
        QueryBuilder builder = new QueryBuilder();
        builder.getSelectColumns().addAll(columns);
        builder.getAllColumns().addAll(columns);
        return builder;
    }

    /**
     * This method builds the SOQL SELECT clause.
     * @param aggregateFunctions The aggregate functions
     * @return A query instance.
     */
    public static QueryBuilder select(Soqlable...aggregateFunctions) {
        return select("", aggregateFunctions);
    }

    /**
     * This method builds the SOQL SELECT clause.
     * @param summaryField The summary fields to aggregate function separate by commas.
     * @param aggregateFunctions The aggregate functions.
     * @return A query instance.
     */
    public static QueryBuilder select(String summaryField, Soqlable... aggregateFunctions) {
        String[] columns = StringUtils.split(summaryField, ",");
        return select(columns, aggregateFunctions);
    }

    /**
     * This method builds the SOQL SELECT clause.
     * @param summaryColumns The summary fields to aggregate function.
     * @param aggregateFunctions The aggregate function list.
     * @return A query instance.
     */
    public static QueryBuilder select(String[] summaryColumns, Soqlable... aggregateFunctions) {
        QueryBuilder builder = new QueryBuilder();
        SObjectUtils.addPrefix(summaryColumns);
        builder.getSelectColumns().addAll(Arrays.asList(summaryColumns));
        builder.getAggregateFunctions().addAll(Arrays.asList(aggregateFunctions));
        for (Soqlable aggrFunction : aggregateFunctions) {
            if (((AggregateFunction) aggrFunction).hasColumn()) {
                builder.getAllColumns().add(((AggregateFunction) aggrFunction).getColumn());
            }
        }
        return builder;
    }

    /**
     * This method builds the SOQL FROM clause.
     * @param entity The entity name (SObject api name).
     * @return A query instance.
     */
    public QueryBuilder from(String entity) {
        this.table = SObjectUtils.addPrefix(entity);
        return this;
    }

    /**
     * This method builds the SOQL WHERE clause.
     * @param criteria The criterias list.
     * @return A query instance.
     */
    public QueryBuilder where(Condition... criteria) {
        this.getCriteria().addAll(Arrays.asList(criteria));
        return this;
    }

    /**
     * This method builds the SOQL ORDER BY clause.
     * @param column The column name to order records.
     * @return A query instance.
     */
    public QueryBuilder orderBy(String column) {
        column = SObjectUtils.addPrefix(column);
        this.orderByColumn = column;
        this.allQueryColumnsSet.add(column);
        return this;
    }

    /**
     * This method builds the SOQL ORDER BY clause.
     * @param column The column name to order records.
     * @param order The order type (ASC or DESC).
     * @return A query instance.
     */
    public QueryBuilder orderBy(String column, Order order) {
        column = SObjectUtils.addPrefix(column);
        this.orderByColumn = column;
        this.order = order;
        this.allQueryColumnsSet.add(column);
        return this;
    }

    /**
     * This method builds the SOQL ORDER BY clause.
     * @param column The column name to order records.
     * @param ordersNull The null records order type (Last or First).
     * @return A query instance.
     */
    public QueryBuilder orderBy(String column, OrdersNull ordersNull) {
        column = SObjectUtils.addPrefix(column);
        this.orderByColumn = column;
        this.ordersNull = ordersNull;
        this.getAllColumns().add(column);
        return this;
    }

    /**
     * This method builds the SOQL ORDER BY clause.
     * @param column The column name to order records.
     * @param order The order type (ASC or DESC).
     * @param ordersNull The null records order type (Last or First).
     * @return A query instance.
     */
    public QueryBuilder orderBy(String column, Order order, OrdersNull ordersNull) {
        column = SObjectUtils.addPrefix(column);
        this.orderByColumn = column;
        this.order = order;
        this.ordersNull = ordersNull;
        this.getAllColumns().add(column);
        return this;
    }

    /**
     * This method builds the SOQL GROUP BY clause.
     * @param columns The column name(s) (separate by commas) to group records.
     * @return A query instance.
     */
    public QueryBuilder groupBy(String columns) {
        String[] groupByColumns = StringUtils.split(columns, ",");
        return groupBy(groupByColumns);
    }

    /**
     * This method builds the SOQL GROUP BY clause.
     * @param columns  The column names to group records.
     * @return
     */
    public QueryBuilder groupBy(String...columns) {
        SObjectUtils.addPrefix(columns);
        this.getGroupByColumns().addAll(Arrays.asList(columns));
        return this;
    }

    public QueryBuilder limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * This method convert all configuration SOQL to string value.
     * @return SOQL query as string.
     */
    public String toSoql() {
        Output out = new Output("    ");
        this.write(out);
        return out.toString();
    }

    /* (non-Javadoc)
     * @see com.itinvolve.itsm.framework.soql.Soqlable#write(com.itinvolve.itsm.framework.soql.Output)
     */
    @Override
    public void write(Output out) {
        out.println("SELECT");

        if (selectColumns != null && table != null) {
            List<String> selectCol = MetadataUtils.getFieldNamesList(selectColumns, table);
            columns = selectCol;
        }

        // Add columns to select
        if ((columns != null) && (columns.size() > 0)) {
            out.indent();
            appendColumns(out, columns, ",");
            out.unindent();
        }

        // or add functions
        if ((aggregateFunctions != null) && (aggregateFunctions.size() > 0)) {
            out.indent();
            if ((columns != null) && (columns.size() > 0)) {
                out.print(", ");
            }
            appendList(out, aggregateFunctions, ",");
            out.unindent();
        }

        // Add tables to select from
        out.println("FROM");

        // Determine all tables used in query
        out.indent();
        appendString(out, table);
        out.unindent();

        // Add criteria
        if ((criteria != null) && (criteria.size() > 0)) {
            out.println("WHERE");
            out.indent();
            appendList(out, criteria, "AND");
            out.unindent();
        }

        if ((groupByColumns != null) && (groupByColumns.size() > 0)) {
            out.println("GROUP BY");
            out.indent();
            appendColumns(out, groupByColumns, ",");
            out.unindent();
        }

        if (StringUtils.isNotBlank(orderByColumn)) {
            List<String> orderExpresion = new ArrayList<String>();
            orderExpresion.add(orderByColumn);
            out.println("ORDER BY");
            out.indent();
            if (order != null) {
                orderExpresion.add(order.name());
            }
            if (ordersNull != null) {
                orderExpresion.add(ordersNull.getCode());
            }
            appendColumns(out, orderExpresion.toArray(new String[0]), "");
            out.unindent();
        }

        if (limit > 0) {
            out.println("LIMIT");
            out.indent();
            appendString(out, String.valueOf(limit));
            out.unindent();
        }
    }

    /**
     * This method appends the value from list to output.
     * @param out The output appender.
     * @param collection The value list to append.
     * @param seperator The separator to value list.
     */
    @SuppressWarnings("rawtypes")
    private void appendList(Output out, Collection collection, String seperator) {
        Iterator i = collection.iterator();
        boolean hasNext = i.hasNext();

        while (hasNext) {
            Soqlable curr = (Soqlable) i.next();
            hasNext = i.hasNext();
            curr.write(out);
            out.print(' ');
            if (hasNext) {
                out.print(seperator);
            }
            out.println();
        }
    }

    /**
     * This method appends the values from list to output.
     * @param out The output appender.
     * @param collection The value list to append.
     * @param seperator The separator to value list.
     */
    private void appendColumns(Output out, String[] collection, String seperator) {
        appendColumns(out, Arrays.asList(collection), seperator);
    }

    /**
     * This method appends the values from list to output.
     * @param out The output appender.
     * @param collection The value list to append.
     * @param seperator The separator to value list.
     */
    private void appendColumns(Output out, List<String> collection, String seperator) {
        int collectionSize = collection.size();
        for (int i = 0; i < collectionSize; i++) {
            out.print(collection.get(i));
            if (i < (collectionSize - 1)) {
                out.print(seperator);
            }
            out.print(' ');
            //out.println(); for rows
        }
        out.println();// for columns
    }

    /**
     * This method appends the string value to output.
     * @param out The output appender.
     * @param value The string value to append.
     */
    private void appendString(Output out, String value) {
        appendString(out, value, null);
    }

    /**
     * This method appends the string value to output.
     * @param out The output appender.
     * @param value The string value to append.
     * @param separator The separator to append at the end of each string value.
     */
    private void appendString(Output out, String value, String separator) {
        out.print(value);
        if (StringUtils.isNotBlank(separator)) {
            out.print(",");
        }
        out.print(' ');
        out.println();
    }

    /**
     * This method verify if the query contains a aggregate function.
     * @param function The aggregate function type.
     * @return A true/false value.
     */
    public AggregateFunction hasAggregateFunction(Function function) {
        AggregateFunction aggregateFunc = null;
        if ((aggregateFunctions != null) && (aggregateFunctions.size() > 0)) {
            String func1 = function.name();
            String func2;
            for (int i = 0; (i < aggregateFunctions.size()) && aggregateFunc == null; i++) {
                func2 = ((AggregateFunction)aggregateFunctions.get(i)).getFunction().name();
                if (func1.equals(func2)) {
                    aggregateFunc = (AggregateFunction) aggregateFunctions.get(i);
                }
            }
        }
        return aggregateFunc;
    }

    /**
     * This method returns all records according query built.
     * @return A SObject list result.
     */
    public List<SObject> toResultList() {
        return DatabaseSchema.getRecords(this);
    }

    /**
     * This method returns a single record according query built.
     * @return A single SObject.
     */
    public SObject toSingleResult() {
        limit = 1;
        List<SObject> records = DatabaseSchema.getRecords(this);
        SObject record = null;
        if ((records != null) && !records.isEmpty()) {
            record = records.get(0);
        }
        return record;
    }
}
