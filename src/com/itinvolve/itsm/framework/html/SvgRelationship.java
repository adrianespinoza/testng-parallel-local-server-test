/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.html;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Point;

/**
 * Svg Relationship class. Provide a comparable object and contains methods to get class attributes.
 * @author Adrian Espinoza
 *
 */
public class SvgRelationship implements Comparable<SvgRelationship> {
    /** Store the relationship text */
    private String text;

    /** Store an object in the right side */
    private SvgObject rightObject;

    /** Store an object in the left side */
    private SvgObject leftObject;

    /** Store the right point */
    private Point rightPoint;

    /** Store the left point */
    private Point leftPoint;

    /**
     * This method returns the relationship text.
     * @return A string value.
     */
    public String getText() {
        return text;
    }

    /**
     * This method set the text value.
     * @param newRelationshipType The new text value.
     */
    public void setText(String newRelationshipType) {
        text = newRelationshipType;
    }

    /**
     * This method returns the object from the right side.
     * @return An object.
     */
    public SvgObject getRightObject() {
        return rightObject;
    }

    /**
     * This method set the object in the right side.
     * @param newRight The new object.
     */
    public void setRightObject(SvgObject newRight) {
        rightObject = newRight;
    }

    /**
     * This method returns the object from the left side.
     * @return An object.
     */
    public SvgObject getLeftObject() {
        return leftObject;
    }

    /**
     * This method set the object in the left side.
     * @param newLeft The new object.
     */
    public void setLeftObject(SvgObject newLeft) {
        leftObject = newLeft;
    }

    /**
     * This method return the point from the right side.
     * @return A point.
     */
    public Point getRightPoint() {
        return rightPoint;
    }

    /**
     * This method set the point in the right side.
     * @param newRightPoint The new object.
     */
    public void setRightPoint(Point newRightPoint) {
        rightPoint = newRightPoint;
    }

    /**
     * This method return the point from the left side.
     * @return A point.
     */
    public Point getLeftPoint() {
        return leftPoint;
    }

    /**
     * This method set the point in the left side.
     * @param newLeftPoint The new object.
     */
    public void setLeftPoint(Point newLeftPoint) {
        leftPoint = newLeftPoint;
    }

    /**
     * This method show the object info.
     */
    public void info() {
        System.out.println(StringUtils.repeat("#", 20));
        System.out.println("Text: " + text);
        System.out.println("Left Object");
        System.out.println(StringUtils.repeat("-", 20));
        leftObject.info();
        System.out.println(StringUtils.repeat("-", 20));
        System.out.println("Right Object");
        rightObject.info();

    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        SvgRelationship o = (SvgRelationship) obj;

        int resCompare = rightObject.compareTo(o.rightObject);
        if (resCompare == 0) {
            resCompare = leftObject.compareTo(o.leftObject);
            if (resCompare == 0) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SvgRelationship o) {
        int resCompare = rightObject.compareTo(o.rightObject);
        if (resCompare == 0) {
            resCompare = leftObject.compareTo(o.leftObject);
        }
        return resCompare;
    }
}
