/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

/**
 * This enum is to use with SOQL comparison operators
 * @author Adrian Espinoza
 *
 */
public enum Operator {
    EQUALS("="),
    GREATER_THAN(">"),
    GREATER_OR_EQUAL(">="),
    LESS_THAN("<"),
    LESS_OR_EQUAL("<="),
    LIKE("LIKE"),
    NOT_EQUALS("!="),
    INCLUDES("INCLUDES"),
    EXCLUDES("EXCLUDES"),
    IN("IN"),
    NO_IN("NOT IN");

    private String code;

    private Operator(String operator) {
        code = operator;
    }

    public String getOperator() {
        return code;
    }
}
