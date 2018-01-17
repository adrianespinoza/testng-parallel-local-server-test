/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.html;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.utils.WebUtils;

/**
 * Html Scanner class. Provide methods to scan html text.
 * @author Adrian Espinoza
 *
 */
public class HtmlScanner {
    /**
     * This method scan a svg element.
     * @param locator  The selector to find the element.
     * @return A SvgImage object
     */
    public static SvgImage scanSvg(By locator) {
        WebElement element = WebUtils.waitForElement(locator);
        String html = WebUtils.getInnerHtml(element);
        return scanSvg(html);
    }

    /**
     * This method scan a svg element.
     * @param html The html text.
     * @return A SvgImage object
     */
    public static SvgImage scanSvg(String html) {
        Document document = Jsoup.parse(html);
        Elements selectedElements = document.select("svg > g > g > g > *");

        List<SvgObject> svgObjects = scanSvgObjects(selectedElements);
        List<SvgRelationship> relationships = scanSvgRelationships(selectedElements, svgObjects);

        SvgImage image = new SvgImage(relationships, svgObjects);

        return image;
    }

    /**
     * This method set the relationships between image objects
     * @param value The value to set.
     * @param valueList The relationships built.
     */
    private static void setRelationValueInList(String value, List<SvgRelationship> valueList) {
        for (SvgRelationship svgObject : valueList) {
            if (svgObject.getText() == null) {
                svgObject.setText(value);
                break;
            }
        }
    }

    /**
     * This method get the object type from a url.
     * @param url The direction that contain the object type.
     * @return A object type.
     */
    private static String getObjectType(String url) {
        String[] parts = StringUtils.split(url, "/");
        String subresult = parts[parts.length-1];
        String result = StringUtils.substring(subresult, 0, (subresult.indexOf("c_") + 1));
        return result;
    }

    /**
     * This method build a svg image object.
     * @param element The svg element.
     * @return A SvgObject object.
     */
    private static SvgObject buildSvgImageObject(Element element) {
        SvgObject value = new SvgObject();
        value.setType(getObjectType(element.attr("xlink:href")));
        value.setHeight(Integer.valueOf(element.attr("height")));
        value.setWidth(Integer.valueOf(element.attr("width")));

        int x = Integer.valueOf(element.attr("x"));
        int y = Integer.valueOf(element.attr("y"));
        Point p = new Point(x, y);

        value.setLocation(p);

        return value;
    }

    /**
     * This method scan the relationships that exist between svg objects.
     * @param selectedElements The elements selected by a special criteria.
     * @param svgObjects The svg object to relate each other.
     * @return A relationship list.
     */
    private static List<SvgRelationship> scanSvgRelationships(Elements selectedElements, List<SvgObject> svgObjects) {
        List<SvgRelationship> relationshipResult = new ArrayList<SvgRelationship>();
        String elementTagName;
        for (Iterator<Element> iter = selectedElements.iterator(); iter.hasNext();) {
            Element element = iter.next();
            elementTagName = element.tagName();
            if (!elementTagName.equals("rect") && !elementTagName.equals("img")) {
                if (elementTagName.equals("path")) {
                    SvgRelationship value = buildSvgRelationshipObject(element, svgObjects);
                    if (value != null) {
                        relationshipResult.add(value);
                    }
                    iter.next();
                    iter.next();
                } else if (elementTagName.equals("text")) {
                    String text = element.text();
                    setRelationValueInList(text, relationshipResult);
                }
            }
        }
        return relationshipResult;
    }

    /**
     * This method build the svg objects.
     * @param selectedElements The selected web elements
     * @return A svg object list.
     */
    private static List<SvgObject> scanSvgObjects(Elements selectedElements) {
        List<SvgObject> svgObjectResult = new ArrayList<SvgObject>();
        String elementTagName;

        for (Iterator<Element> iter = selectedElements.iterator(); iter.hasNext();) {
            Element element = iter.next();
            elementTagName = element.tagName();
            if (!elementTagName.equals("rect") && !elementTagName.equals("path")) {
                if (elementTagName.equals("img")) {
                    SvgObject value = buildSvgImageObject(element);
                    svgObjectResult.add(value);
                } else if (elementTagName.equals("text")) {
                    String text = element.text();
                    if (svgObjectResult.size() > 0) {
                        SvgObject objTmp = svgObjectResult.get(svgObjectResult.size() - 1);
                        if (objTmp.getText() == null) {
                            objTmp.setText(text);
                        }
                    }
                }
            }
        }
        return svgObjectResult;
    }

    /**
     * This method build a single relationship.
     * @param element The element to build as object.
     * @param svgObjects The svg objects.
     * @return A relationship object.
     */
    private static SvgRelationship buildSvgRelationshipObject(Element element, List<SvgObject> svgObjects) {
        String d = element.attr("d");
        String[] splited = StringUtils.split(d, " ");

        SvgRelationship rsRes = null;

        if (splited.length > 5) {
            rsRes = new SvgRelationship();
            int x, y;
            x = Integer.valueOf(splited[1]);
            y = Integer.valueOf(splited[2]);

            Point left =  new Point(x, y);

            x = Integer.valueOf(splited[4]);
            y = Integer.valueOf(splited[5]);

            Point right = new Point(x, y);

            rsRes.setLeftPoint(left);
            rsRes.setRightPoint(right);

            SvgObject rightObject = getSvgObjectToPoint(right, svgObjects);
            SvgObject leftObject = getSvgObjectToPoint(left, svgObjects);

            rsRes.setLeftObject(leftObject);
            rsRes.setRightObject(rightObject);
        } else {
            System.out.println("splited error: " + splited.toString());
        }

        return rsRes;
    }

    /**
     * This method recover an object for a specific point in the image.
     * @param p The point to search.
     * @param svgObjects The svg object list.
     * @return A svg object.
     */
    private static SvgObject getSvgObjectToPoint(Point p, List<SvgObject> svgObjects) {
        for (SvgObject svgObject : svgObjects) {
            if (svgObject.isInRadio(p)) {
                return svgObject;
            }
        }
        return null;
    }
}