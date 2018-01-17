/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.env;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.itinvolve.itsm.framework.logs.Log;

/**
 * Screen shot handler class. Provide methods to capture screen shots using the Selenium driver.
 * @author Adrian Espinoza
 *
 */
public class ScreenshotHandler {
    private static List<String> compareImageFilePaths;
    /**
     * This method takes the screen shots of any screen
     * @param nameScreenshot The file name to save the screen shot.
     * @return The location where was saved the screen shot.
     * @throws IOException
     */
    public static String takeScreenshot(String nameScreenshot) {
        String path = null;
        String threadId = String.valueOf(Thread.currentThread().getId());
        try {
            WebDriver shootDriver;
            if (Setup.EXECUTE_IN_PARALLEL) {
                shootDriver = new Augmenter().augment(WebDriverHandler.getDriver(threadId));
            } else {
                shootDriver = WebDriverHandler.getDriver(threadId);
            }

            File screenshot = ((TakesScreenshot) shootDriver).getScreenshotAs(OutputType.FILE);
            Log.logger().info("-> The screenshot was taken");
            path = Setup.PATH_SCREENSHOT_FILES + nameScreenshot + ".png";

            FileUtils.copyFile(screenshot, new File(path));
            Log.logger().info("-> The screenshot file has been saved >>> [" + path + "]");
        } catch (IOException e) {
            path = null;
            Log.logger().error("-> The screenshot file was not saved >>> [Input Output Exception]", e);
            e.printStackTrace();
        }
        return path;
    }

    /**
     * This method takes the screen shot and at the same time add the screen shot taken to report.
     * @param imageFileName The file name to save the screen shot.
     * @param testResult The test method which belongs at the screen shot.
     */
    public static void takeScreenshotAddToReport(String imageFileName, ITestResult testResult){
        Reporter.setCurrentTestResult(testResult);
        String path = takeScreenshot(imageFileName);
        if (path != null) {
            Reporter.log("<a href='./images/"+imageFileName+".png'> <img src='./images/"+imageFileName+".png' hight='100' width='100'/> </a> <br/>");
            Log.logger().info("-> The screenshot file was added successfully at report");
        }
    }

    public static String captureScreenRegion(Point p, int width, int height, String imageFileName) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        File screen = ((TakesScreenshot) WebDriverHandler.getDriver(threadId)).getScreenshotAs(OutputType.FILE);
        String path = null;
        try {
            BufferedImage img = ImageIO.read(screen);
            BufferedImage dest = img.getSubimage(p.getX(), p.getY(), width, height);
            ImageIO.write(dest, "png", screen);

            path = Setup.PATH_SCREENSHOT_FILES + "compare\\" + imageFileName + ".png";
            File f = null;
            f = new File(path);
            FileUtils.copyFile(screen, f);

            addCompareImageFileName(imageFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static void clearCompareImageFileNameList() {
        if (compareImageFilePaths != null) {
            compareImageFilePaths.clear();
        }
    }

    public static void addCompareImageFileName(String imageFileName) {
        if (compareImageFilePaths == null) {
            compareImageFilePaths = new ArrayList<String>();
        }
        compareImageFilePaths.add(imageFileName);
    }

    public static void compareImageFileToReport() {
        if ((compareImageFilePaths != null) && (compareImageFilePaths.size() > 0)) {
            System.out.println(compareImageFilePaths.size());
            for (String imageFileName : compareImageFilePaths) {
                Reporter.log("<a href='./images/compare/"+imageFileName+".png'> <img src='./images/compare/"+imageFileName+".png' hight='100' width='100'/> </a>");
            }
            Reporter.log("<br/>");
            Log.logger().info("-> The region screenshot file was added successfully at report");
        }
    }
}
