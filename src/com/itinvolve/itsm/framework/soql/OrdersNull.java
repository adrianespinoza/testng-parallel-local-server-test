/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

/**
 * This enum is to use with SOQL ORDER BY clause.
 * @author Adrian Espinoza
 *
 */
public enum OrdersNull {
    NULLS_FIRST("NULLS FIRST"),
    NULLS_LAST("NULLS LAST");

    private String code;

    private OrdersNull(String ordersNull) {
        code = ordersNull;
    }

    public String getCode() {
        return code;
    }
}
