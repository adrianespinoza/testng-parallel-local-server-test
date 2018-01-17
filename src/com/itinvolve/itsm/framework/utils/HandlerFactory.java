/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Handler Factory class. Provide methods to create instances of any page class.
 * @author Adrian Espinoza
 *
 */
public class HandlerFactory {
    /** Store the page instances */
    private static Map<String, Object> classesMap;

    static {
        classesMap = new HashMap<String, Object>();
    }

    /**
     * This method get the page instance.
     * @param clazz The class to be instantiate.
     * @return A page instance.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Object> T getInstance(Class<? extends Object> clazz) {
        String key = clazz.getName();
        return (T)getInstance(clazz, key);
    }
    /**
     * This method clean the instances map.
     */
    public static void clear() {
        classesMap.clear();
    }

    /**
     * This method get the page instance.
     * @param clazz The class to be instantiate.
     * @param key The unique map key.
     * @return A page instance.
     */
    @SuppressWarnings("unchecked")
    private static <T extends Object> T getInstance(Class<? extends Object> clazz, String key) {
        if (!classesMap.containsKey(key)) {
            try {
                Object page = clazz.newInstance();
                classesMap.put(key, page);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (T) classesMap.get(key);
    }
}
