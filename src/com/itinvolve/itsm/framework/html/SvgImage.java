/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.html;

import java.util.List;

/**
 * Svg Image class. Provide a comparable object and contains methods to get class attributes.
 * @author Adrian Espinoza
 *
 */
public class SvgImage implements Comparable<SvgImage> {
    /** Store the relationship object */
    private List<SvgRelationship> imgRelations;

    /** Store the svg object */
    private List<SvgObject> imgComponent;

    /**
     * Custom constructor.
     * @param imgRel The relationship objects.
     * @param imgCmp The svg objects.
     */
    public SvgImage(List<SvgRelationship> imgRel, List<SvgObject> imgCmp) {
        imgRelations = imgRel;
        imgComponent = imgCmp;
    }

    /**
     * This method get the difference between two image object
     * @param image
     * @return
     */
    public SvgObject getDiference(SvgImage image) {
        SvgObject result = null;
        for (SvgObject imgcmp : imgComponent) {
            if (!image.imgComponent.contains(imgcmp)) {
                return imgcmp;
            }
        }
        return result;
    }

    /**
     * This method verifies if contain the expected object into image.
     * @param expectedCmp The expected object.
     * @return A true/false value.
     */
    public boolean containsExpectedCmp(SvgObject expectedCmp) {
        for (SvgObject cmp : imgComponent) {
            if (cmp.isPartialEquals(expectedCmp)) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        SvgImage image = (SvgImage) obj;
        int compare = this.compareTo(image);
        if (compare == 0) {
            return true;
        }
        return false;
    }

    /**
     * This method is to show information.
     */
    public void info() {
        for (SvgRelationship svgRelationship : imgRelations) {
            svgRelationship.info();
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SvgImage o) {
        int resCompare = -1;
        boolean contains = imgRelations.containsAll(o.imgRelations);
        if (contains) {
            resCompare = 0;
        }
        return resCompare;
    }
}
