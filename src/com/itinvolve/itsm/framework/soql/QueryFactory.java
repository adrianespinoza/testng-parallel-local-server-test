/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.itinvolve.itsm.framework.database.MetadataUtils;
import com.itinvolve.itsm.framework.logs.Log;

/**
 * Query Factory class. provide methods to build queries more easily
 * @author Adrian Espinoza
 *
 */
public class QueryFactory {
    /**
     * This method build a query using a SOQL aggregation function, only COUNT function.
     * @param objectType The SObject api name.
     * @return A Query built as instance
     */
    public static QueryBuilder countFn(String objectType) {
        QueryBuilder query = QueryBuilder.select(AggregateFunction.count())
        .from(objectType);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder countFn(String objectType, Condition condition) {
        QueryBuilder query = QueryBuilder.select(AggregateFunction.count())
        .from(objectType)
        .where(condition);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder countDistinctFn(String objectType, String column) {
        QueryBuilder query = aggregateFunctionQuery(Function.COUNT_DISTINCT, column, null, objectType, null);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder minFn(String objectType, String column) {
        QueryBuilder query = aggregateFunctionQuery(Function.MIN, column, null, objectType, null);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder maxFn(String objectType, String column) {
        QueryBuilder query = aggregateFunctionQuery(Function.MAX, column, null, objectType, null);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder sumFn(String objectType, String column) {
        QueryBuilder query = aggregateFunctionQuery(Function.SUM, column, null, objectType, null);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder avgFn(String objectType, String column) {
        QueryBuilder query = aggregateFunctionQuery(Function.AVG, column, null, objectType, null);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder countDistinctFn(String objectType, String column, String alias) {
        QueryBuilder query = aggregateFunctionQuery(Function.COUNT_DISTINCT, column, alias, objectType, null);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder minFn(String objectType, String column, String alias) {
        QueryBuilder query = aggregateFunctionQuery(Function.MIN, column, alias, objectType, null);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder maxFn(String objectType, String column, String alias) {
        QueryBuilder query = aggregateFunctionQuery(Function.MAX, column, alias, objectType, null);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder sumFn(String objectType, String column, String alias) {
        QueryBuilder query = aggregateFunctionQuery(Function.SUM, column, alias, objectType, null);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder avgFn(String objectType, String column, String alias) {
        QueryBuilder query = aggregateFunctionQuery(Function.AVG, column, alias, objectType, null);
        return query;
    }

    /**
     * This method is generic that build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder countDistinctFn(String objectType, String column, String alias, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.COUNT_DISTINCT, column, alias, objectType, condition);
        return query;
    }

    /**
     * This method is generic that build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder minFn(String objectType, String column, String alias, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.MIN, column, alias, objectType, condition);
        return query;
    }

    /**
     * This method is generic that build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder maxFn(String objectType, String column, String alias, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.MAX, column, alias, objectType, condition);
        return query;
    }

    /**
     * This method is generic that build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder sumFn(String objectType, String column, String alias, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.SUM, column, alias, objectType, condition);
        return query;
    }

    /**
     * This method is generic that build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder avgFn(String objectType, String column, String alias, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.AVG, column, alias, objectType, condition);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder countDistinctFn(String objectType, String column, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.COUNT_DISTINCT, column, null, objectType, condition);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder minFn(String objectType, String column, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.MIN, column, null, objectType, condition);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder maxFn(String objectType, String column, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.MAX, column, null, objectType, condition);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder sumFn(String objectType, String column, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.SUM, column, null, objectType, condition);
        return query;
    }

    /**
     * This method build a query using any SOQL aggregation function.
     * @param column The SObject field name.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder avgFn(String objectType, String column, Condition condition) {
        QueryBuilder query = aggregateFunctionQuery(Function.AVG, column, null, objectType, condition);
        return query;
    }


    /**
     * This method build a SOQL query.
     * @param columns The SObejct field api names.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder buildQuery(String objectType, String[] columns) {
        QueryBuilder query = null;
        query = QueryBuilder.select(columns).from(objectType);
        return query;
    }

    /**
     * This method build a SOQL query.
     * @param fieldGpr The group to the belongs the fields.
     * @param objectType The SObject api name.
     * @return A Query built as instance.
     */
    public static QueryBuilder buildQuery(String objectType, Columns fieldGpr) {
        QueryBuilder query = null;
        List<String> fields = MetadataUtils.getFieldNamesList(fieldGpr, objectType);
        query = QueryBuilder.select(fields).from(objectType);
        return query;
    }

    /**
     * This method build a SOQL query.
     * @param fieldGpr The group to the belongs the fields.
     * @param objectType The SObject api name.
     * @param condition The query condition.
     * @return A Query built as instance.
     */
    public static QueryBuilder buildQuery(String objectType, Columns fieldGpr, Condition condition) {
        QueryBuilder query = null;
        List<String> fields = MetadataUtils.getFieldNamesList(fieldGpr, objectType);
        query = QueryBuilder.select(fields).from(objectType).where(condition);
        return query;
    }

    /**
     * This method is generic that build a query using any SOQL aggregation function.
     * @param functionType The SOQL function type.
     * @param column The SObject field name.
     * @param alias The alias to defined column.
     * @param objectType The SObject api name.
     * @param condition The SOQL condition.
     * @return A Query built as instance.
     */
    private static QueryBuilder aggregateFunctionQuery(Function functionType, String column, String alias, String objectType, Condition condition) {
        QueryBuilder query = null;
        int ordinal = functionType.ordinal();
        boolean isNumeric;

        AggregateFunction aggregateFunc = null;

        switch (ordinal) {
        case 0://COUNT
            if(StringUtils.isNotBlank(alias)) {
                aggregateFunc = AggregateFunction.count(column, alias);
            } else {
                aggregateFunc = AggregateFunction.count(column);
            }
            break;
        case 1://COUNT_DISTINCT
            if(StringUtils.isNotBlank(alias)) {
                aggregateFunc = AggregateFunction.countDistinct(column, alias);
            } else {
                aggregateFunc = AggregateFunction.countDistinct(column);
            }
            break;
        case 2://MIN
            if(StringUtils.isNotBlank(alias)) {
                aggregateFunc = AggregateFunction.min(column, alias);
            } else {
                aggregateFunc = AggregateFunction.min(column);
            }
            break;
        case 3://MAX
            if(StringUtils.isNotBlank(alias)) {
                aggregateFunc = AggregateFunction.max(column, alias);
            } else {
                aggregateFunc = AggregateFunction.max(column);
            }
            break;
        case 4://SUM Returns the total sum of a numeric field.
            isNumeric = MetadataUtils.isNumericField(objectType, column);
            if (isNumeric) {
                if(StringUtils.isNotBlank(alias)) {
                    aggregateFunc = AggregateFunction.sum(column, alias);
                } else {
                    aggregateFunc = AggregateFunction.sum(column);
                }
            }
            break;
        case 5://AVG Returns the average value of a numeric field.
            isNumeric = MetadataUtils.isNumericField(objectType, column);
            if (isNumeric) {
                if(StringUtils.isNotBlank(alias)) {
                    aggregateFunc = AggregateFunction.avg(column, alias);
                } else {
                    aggregateFunc = AggregateFunction.avg(column);
                }
            }
            break;

        default:
            Log.logger().error("-> Function don't specified in switch for " + functionType.name() + " function");
            break;
        }
        if (aggregateFunc != null) {
            query = QueryBuilder.select(aggregateFunc).from(objectType);
            if (condition != null) {
                query.where(condition);
            }
        }
        return query;
    }
}
