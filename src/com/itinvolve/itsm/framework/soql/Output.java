/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.soql;

/**
 * Output class. Provide output method for build the SOQL queries.
 * @author Adrian Espinoza
 *
 */
public class Output {
    /** Store the string buffer result instance */
    private StringBuffer result = new StringBuffer();

    /** Store the current string buffer instance */
    private StringBuffer currentIndent = new StringBuffer();

    /** Option to know if the text will be in a new line */
    private boolean newLineComing;

    /** Store the text indent */
    private final String indent;

    /**
     * Custom constructor.
     * @param indent The initial text indent.
     */
    public Output(String indent) {
        this.indent = indent;
    }

    /**
     * This method return the toSring from string buffer result.
     */
    public String toString() {
        return result.toString();
    }

    /**
     * This method allows append an object value to string buffer result without jump to a new line.
     * @param o The object value to append.
     * @return Output instance.
     */
    public Output print(Object o) {
        writeNewLineIfNeeded();
        result.append(o);
        return this;
    }

    /**
     * This method allows append a char value to string buffer result without jump to a new line.
     * @param c The character value to append.
     * @return Output instance.
     */
    public Output print(char c) {
        writeNewLineIfNeeded();
        result.append(c);
        return this;
    }

    /**
     * This method allows append an object value to string buffer result with jump to a new line.
     * @param o The object value to append.
     * @return Output instance.
     */
    public Output println(Object o) {
        writeNewLineIfNeeded();
        result.append(o);
        newLineComing = true;
        return this;
    }

    /**
     * This method the newLineComing attribute to allow write in a new line.
     */
    public Output println() {
        newLineComing = true;
        return this;
    }

    /**
     * This method appends the indent to current string buffer.
     */
    public void indent() {
        currentIndent.append(indent);
    }

    /**
     * This method remove the indent from current string buffer.
     */
    public void unindent() {
        currentIndent.setLength(currentIndent.length() - indent.length());
    }

    /**
     * This method writes new lines if it is needed.
     */
    private void writeNewLineIfNeeded() {
        if (newLineComing) {
            result.append('\n').append(currentIndent);
            newLineComing = false;
        }
    }
}
