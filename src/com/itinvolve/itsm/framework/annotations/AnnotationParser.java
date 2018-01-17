/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.annotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Annotation Parser class. Provide methods parsers to parse the custom annotations
 * @author Adrian Espinoza
 *
 */
public class AnnotationParser {
    /** Store the current class name that use any custom annotation */
    private static String CURRENT_CLASS_NAME;

    /** Store a keyword to identify an annotation before */
    private static String KEY_BEFORE = "-before";

    /** Store a keyword to identify an annotation after */
    private static String KEY_AFTER = "-after";

    /** Store the current class methods */
    private static Map<String,Method> CURRENT_CLASS_METHODS_MAP = new HashMap<String, Method>();

    /**
     * Default constructor.
     */
    private AnnotationParser() {}

    /**
     * This method execute the Before annotation parser.
     * @param classInstance The class instance that contains any custom annotations.
     * @param testMethodName The test method name to which belongs the Before annotation.
     */
    public static void parseBeforeTestMethod(Object classInstance, String testMethodName) {
        String key = testMethodName + KEY_BEFORE;
        executeParser(classInstance, key);
    }

    /**
     * This method execute the After annotation parser.
     * @param classInstance The class instance that contains any custom annotations.
     * @param testMethodName The test method name to which belongs the Before annotation.
     */
    public static void parseAfterTestMethod(Object classInstance, String testMethodName) {
        String key = testMethodName + KEY_AFTER;
        executeParser(classInstance, key);
    }

    /**
     * This method execute the annotation parser according keyword.
     * @param classInstance The class instance that contains any custom annotations.
     * @param key The keyword before or after.
     */
    private static void executeParser(Object classInstance, String key) {
        String className = classInstance.getClass().getName();
        if (CURRENT_CLASS_NAME != null) {
            if (!(CURRENT_CLASS_NAME.equals(className))) {
                loadMethodsToMap(classInstance, CURRENT_CLASS_METHODS_MAP);
                CURRENT_CLASS_NAME = className;
            }
        } else {
            loadMethodsToMap(classInstance, CURRENT_CLASS_METHODS_MAP);
            CURRENT_CLASS_NAME = className;
        }
        invokeMethod(classInstance, CURRENT_CLASS_METHODS_MAP, key);
    }

    /**
     * This method executes the methods with custom annotations.
     * @param classInstance The class instance that contains any custom annotations.
     * @param methodsMap The methods to execute.
     * @param key The key to identify each method into class.
     */
    private static void invokeMethod(Object classInstance, Map<String,Method> methodsMap, String key) {
        if (methodsMap.containsKey(key)) {
            Method method = methodsMap.get(key);
            try {
                method.invoke(classInstance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method loads all methods from a class instance.
     * @param classInstance The class instance that contains any custom annotations.
     * @param allMethodsToLoadMap The map collection to load methods.
     */
    private static void loadMethodsToMap(Object classInstance, Map<String,Method> allMethodsToLoadMap) {
        allMethodsToLoadMap.clear();
        Method[] methods = classInstance.getClass().getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(BeforeTestMethod.class)) {
                BeforeTestMethod beforeTest = method.getAnnotation(BeforeTestMethod.class);
                String[] testMethods = beforeTest.methods();
                for (String mtd : testMethods) {
                    String key = mtd + KEY_BEFORE;
                    allMethodsToLoadMap.put(key, method);
                }
            } else if (method.isAnnotationPresent(AfterTestMethod.class)) {
                AfterTestMethod afterTest = method.getAnnotation(AfterTestMethod.class);
                String[] testMethods = afterTest.methods();
                for (String mtd : testMethods) {
                    String key = mtd + KEY_AFTER;
                    allMethodsToLoadMap.put(key, method);
                }
            }
        }
    }
}
