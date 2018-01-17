/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.env;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Properties file reader class. Provide methods to read a config.properties file.
 * @author Adrian Espinoza
 *
 */
public class PropertiesFileReader
{
    /** Store the properties utils */
    private Properties properties;

    /** Store the file input stream reader */
    private FileInputStream file;


    /**
     * Custom constructor
     * @param fileName The properties file name.
     */
    public PropertiesFileReader(String fileName) {
        properties = new Properties();
        try {
            file = new FileInputStream(System.getProperty("user.dir")
                    + "/" + fileName);
            properties.load(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method retrieve the properties defined into file.
     * @param property The specific definition.
     * @return A definition value.
     */
    public String getPropertyConfig(String property) {
        String res = "";
        res = properties.getProperty(property);
        return res;
    }
}
