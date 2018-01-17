/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */

package com.itinvolve.itsm.framework.database;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itinvolve.itsm.framework.env.Setup;
import com.sforce.async.CSVReader;

/**
 * Class that allow read csv files to build sobejct records
 *
 * @author Adrian Espinoza
 *
 */
public class CsvFileReader {
    private static Map<String, List<List<String>>> scvFileRows;
    /**
     * Read csv file according file name only
     * @param csvFile The csv file name
     * @return A List with all csv file rows
     */
    public static List<List<String>> readFile(String csvFile) {
        return read(csvFile, true);
    }

    public static List<String> getColumnNamesFromCSV(String csvFile) {
        List<List<String>> csvDataList = read(csvFile, true);
        if ((csvDataList != null) && !csvDataList.isEmpty()) {
            return csvDataList.get(0);
        }
        return null;
    }

    /**
     * This method verifies if the cache contains the file.
     * @param csvFile The csv file full path
     * @param useDefaultPath The condition to use a default path
     * @return A List with all csv file rows
     */
    private static List<List<String>> read(String csvFile, boolean useDefaultPath) {
        List<List<String>> csvDataList = null;
        if (scvFileRows == null) {
            scvFileRows = new HashMap<String, List<List<String>>>();
        }
        if (!scvFileRows.containsKey(csvFile)) {
            csvDataList = readFile(csvFile, useDefaultPath);
            if ((csvDataList != null) && !csvDataList.isEmpty()) {
                scvFileRows.put(csvFile, csvDataList);
            }
        } else {
            csvDataList = scvFileRows.get(csvFile);
        }
        return csvDataList;
    }

    /**
     * Read csv file
     * @param csvFile The csv file full path
     * @param useDefaultPath The condition to use a default path
     * @return A List with all csv file rows
     */
    private static List<List<String>> readFile(String csvFile, boolean useDefaultPath) {
        List<List<String>> csvDataList = null;
        String csvFilePathFull = useDefaultPath == true ? Setup.INPUT_FILE_PATH + csvFile : csvFile;
        try {
            CSVReader reader = new CSVReader(new FileReader(csvFilePathFull));
            List<String> nextLine;
            csvDataList = new ArrayList<List<String>>();
            while ((nextLine = reader.nextRecord()) != null) {
                // nextLine[] is an array of values from the line
                csvDataList.add(nextLine);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvDataList;
    }
}
