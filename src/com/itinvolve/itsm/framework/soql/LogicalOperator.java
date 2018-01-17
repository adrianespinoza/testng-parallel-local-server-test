/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Logical operator class. Provide SOQL logical operator methods for WHERE clause.
 * @author Adrian Espinoza
 *
 */
public class LogicalOperator extends Condition {
    /** Store the logical operator */
    private String operator;

    /** Store the condition list */
    private List<Condition> criterias;

    /**
     * Custom constructor.
     * @param operator The logical operator.
     * @param nextCriterias The condition criterias.
     */
    private LogicalOperator(String operator, Condition... nextCriterias) {
        this.operator = operator;
        this.criterias = new ArrayList<Condition>();
        for (int i = 0; i < nextCriterias.length; i++) {
            this.criterias.add(nextCriterias[i]);
        }
    }

    /**
     * This method builds the AND logical operator for SELECT clause.
     * @param criterias The logical criterias to execute.
     * @return A LogicalOperator instance.
     */
    public static LogicalOperator and(Condition...criterias) {
        LogicalOperator logicalOperator = new LogicalOperator("AND", criterias);
        return logicalOperator;
    }

    /**
     * This method builds the OR logical operator for SELECT clause.
     * @param criterias The logical criterias to execute.
     * @return A LogicalOperator instance.
     */
    public static LogicalOperator or(Condition...criterias) {
        LogicalOperator logicalOperator = new LogicalOperator("OR", criterias);
        return logicalOperator;
    }

    public void write(Output out) {
        appendList(out, criterias, operator);
    }

    /**
     * This method appends the logical operators in a output.
     * @param out The output to append the logical operator.
     * @param collection The object list of any type.
     * @param seperator The condition separator to join the collection.
     */
    @SuppressWarnings("rawtypes")
    private void appendList(Output out, Collection collection, String seperator) {
        Iterator i = collection.iterator();
        boolean hasNext = i.hasNext();
        boolean hasElements = false;

        if (hasNext) {
            out.print("( ");
            hasElements = true;
        }
        while (hasNext) {
            Soqlable curr = (Soqlable) i.next();
            hasNext = i.hasNext();
            curr.write(out);
            if (hasNext) {
                out.print(' ')
                .print(seperator)
                .print(' ');
            }
        }
        if (hasElements) {
            out.print(" )");
        }
    }
}
