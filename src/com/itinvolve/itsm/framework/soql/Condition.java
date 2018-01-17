/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

/**
 * Condition class. Provides generic methods to use in the child classes.
 * @author Adrian Espinoza
 *
 */
public abstract class Condition implements Soqlable {

    /**
     * This method puts quotes to string values.
     * @param value The value to put the quotes.
     * @return A string value with quotes.
     */
    protected String quote(String value) {
        if (value == null) {
            return "null";
        }
        StringBuffer str = new StringBuffer();
        str.append('\'');
        for (int i = 0; i < value.length(); i++) {
            if ((value.charAt(i) == '\\') || (value.charAt(i) == '\"') || (value.charAt(i) == '\'')) {
                str.append('\\');
            }
            str.append(value.charAt(i));
        }
        str.append('\'');
        return str.toString();
    }

    /**
     * This method have to be implemented in child classes to write SOQL statements.
     */
    public abstract void write(Output out);
}
