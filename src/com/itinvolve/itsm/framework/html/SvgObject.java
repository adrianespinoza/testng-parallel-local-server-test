/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.html;

import org.openqa.selenium.Point;

/**
 * Svg Object class. Provide a comparable object and contains methods to get class attributes.
 * @author Adrian Espinoza
 *
 */
public class SvgObject implements Comparable<SvgObject> {
    /** Store the svg text */
    private String text;

    /** Store the object type recovered from svg image */
    private String type;

    /** Store the object location */
    private Point location;

    /** Store the object width */
    private int width;

    /** Store the object height */
    private int height;

    /** Store a error margin for the image radio */
    private int errorMargin = 5;

    /**
     * Default constructor.
     */
    public SvgObject() {
    }

    /**
     * Custom constructor.
     * @param text The svg text.
     * @param type The object type.
     */
    public SvgObject(String text, String type) {
        this.text = text;
        this.type = type;
    }

    /**
     * This method returns the text value.
     * @return A string value.
     */
    public String getText() {
        return text;
    }

    /**
     * This method set the text value.
     * @param newText The new text value.
     */
    public void setText(String newText) {
        text = newText;
    }

    /**
     * This method returns the object type.
     * @return A string value.
     */
    public String getType() {
        return type;
    }

    /**
     * This method set the object type.
     * @param newType The new object type.
     */
    public void setType(String newType) {
        type = newType;
    }

    /**
     * This method returns the object location.
     * @return A point.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * This method set the object location.
     * @param newLocation The new location point.
     */
    public void setLocation(Point newLocation) {
        location = newLocation;
    }

    /**
     * This method returns the object width.
     * @return A width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * This method set the width value.
     * @param newWidth The new width.
     */
    public void setWidth(int newWidth) {
        width = newWidth;
    }

    /**
     * This method returns the object height.
     * @return A height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * This method set the height value.
     * @param newHeight The new height.
     */
    public void setHeight(int newHeight) {
        height = newHeight;
    }

    /**
     * This method shows the object info.
     */
    public void info() {
        System.out.println("Text: " + text);
        System.out.println("Type: " + type);
        System.out.println("Location: x = " + location.getX() + " y = " + location.getY());
        System.out.println("Widht: " + width);
        System.out.println("Height: " + height);
    }

    /**
     * This method verifies partially if the objects are equals.
     * @param obj The object to compare.
     * @return A true/false value.
     */
    public boolean isPartialEquals(Object obj) {
        SvgObject cmp = (SvgObject) obj;
        if (this.getText().equals(cmp.getText()) && this.getType().equals(cmp.getType())) {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        SvgObject cmp = (SvgObject) obj;
        int result = this.compareTo(cmp);
        if (result == 0) {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SvgObject o) {
        int result = -1;
        if (isInRadio(o.getLocation())) {
            result = this.text.compareTo(o.text);
        }
        return result;
    }

    /**
     * This method verifies if the point is in the object radio.
     * @param p The point to verify.
     * @return A true/false value.
     */
    public boolean isInRadio(Point p) {
        if ((p.getX() > (location.getX() - errorMargin)) && (p.getX() < (location.getX() + (width + errorMargin)))) {
            if ((p.getY() > (location.getY() - errorMargin)) && (p.getY() < (location.getY() + (height + errorMargin)))) {
                return true;
            }
        }
        return false;
    }
}
