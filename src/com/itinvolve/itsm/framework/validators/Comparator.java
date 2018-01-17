package com.itinvolve.itsm.framework.validators;

import java.math.BigDecimal;

import com.itinvolve.itsm.framework.logs.Log;

public enum Comparator {
    EQUALS, // 0
    GREATER_THAN, // 1
    GREATER_OR_EQUAL, // 2
    LESS_THAN, // 3
    LESS_OR_EQUAL, // 4
    NOT_EQUALS; // 5

    public boolean compare(int valueToCompare, int expectedValue) {
        int ordinal = this.ordinal();
        boolean comareResult = false;
        switch (ordinal) {
        case 0:
            comareResult = valueToCompare == expectedValue;
            break;
        case 1:
            comareResult = valueToCompare > expectedValue;
            break;
        case 2:
            comareResult = valueToCompare >= expectedValue;
            break;
        case 3:
            comareResult = valueToCompare < expectedValue;
            break;
        case 4:
            comareResult = valueToCompare <= expectedValue;
            break;
        case 5:
            comareResult = valueToCompare != expectedValue;
            break;
        }
        return comareResult;
    }

    public boolean compare(BigDecimal valueToCompare, BigDecimal expectedValue) {
        int ordinal = this.ordinal();
        boolean comareResult = false;
        int compareBigDecimalResult = valueToCompare.compareTo(expectedValue);
        switch (ordinal) {
        case 0:
            comareResult = compareBigDecimalResult == 0;
            break;
        case 1:
            comareResult = compareBigDecimalResult > 0;
            break;
        case 2:
            comareResult = compareBigDecimalResult >= 0;
            break;
        case 3:
            comareResult = compareBigDecimalResult < 0;
            break;
        case 4:
            comareResult = compareBigDecimalResult <= 0;
            break;
        case 5:
            comareResult = valueToCompare != expectedValue;
            break;
        }
        return comareResult;
    }

    public boolean compare(String valueToCompare, String expectedValue) {
        int ordinal = this.ordinal();
        boolean comareResult = false;
        switch (ordinal) {
        case 0:
            comareResult = valueToCompare.equals(expectedValue);
            break;
        case 5:
            comareResult = !valueToCompare.equals(expectedValue);
            break;
        default:
            Log.logger().warn("-> Invalid comparator [" + this.name() + "] for String value. use only [EQUALS and NOT_EQUALS]");
            break;
        }
        return comareResult;
    }
}
