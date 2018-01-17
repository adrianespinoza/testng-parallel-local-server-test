/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.itinvolve.itsm.framework.env.ScreenshotHandler;
import com.itinvolve.itsm.framework.env.WebDriverHandler;
import com.itinvolve.itsm.framework.logs.Log;

/**
 * Web Utils class.  Provides Wait methods for an elements, and AJAX elements to load.
 * It uses WebDriverWait (explicit wait) for waiting an element or javaScript.
 * @author Adrian Espinoza
 *
 */
public class WebUtils {
    /** Store the the timeout status */
    private static boolean TIMER_EXPIRED = false;

    /** Store a timeout counter */
    private static int TIMER_COUNTER = 0;

    /** Store the timeout */
    private static final int TIMEOUT = 20000;

    /**
     * This method checks if the element is in the DOM, regardless of being displayed or not.
     * @param locator The selector to find the element.
     * @return boolean.
     */
    public static boolean isElementPresent(final By locator) {
        try {
            waitForPageLoaded();
            String threadId = String.valueOf(Thread.currentThread().getId());
            WebDriverHandler.getDriver(threadId).findElement(locator);//if it does not find the element throw NoSuchElementException, which calls "catch(Exception)" and returns false;
            return true;
        } catch (Exception e) {
            Log.logger().warn("-> No such element >>> " + locator);
            return false;
        }
    }

    /**
     * This method checks if the List<WebElement> are in the DOM, regardless of being displayed or not.
     * @param locator The selector to find the element.
     * @return boolean.
     */
    public static boolean areElementsPresent(final By locator) {
        try {
            waitForPageLoaded();
            String threadId = String.valueOf(Thread.currentThread().getId());
            List<WebElement> res = WebDriverHandler.getDriver(threadId).findElements(locator);
            if (res.size() > 0) {
                return true;
            } else {
                Log.logger().warn("-> No such elements >>> " + locator);
                return false;
            }
        } catch (Exception e) {
            Log.logger().warn("-> No such elements >>> " + locator);
            return false;
        }
    }

    /**
     * This method checks if the element is in the DOM and displayed.
     * @param locator The selector to find the element.
     * @return boolean.
     */
    public static boolean isElementPresentAndVisible(final By locator) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        try {
            waitForPageLoaded();
            WebElement element  = WebDriverHandler.getDriver(threadId).findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            try {
                WebElement element = WebDriverHandler.getWait(threadId).until(ExpectedConditions.visibilityOfElementLocated(locator));
                return element.isDisplayed();
            } catch (Exception e2) {
                Log.logger().warn("-> Element not displayed >>> " + locator);
                return false;
            }
        }
    }

    /**
     * This method checks if the text is present in the element.
     * @param locator The selector to find the element that should contain text.
     * @param text The Text element you are looking for.
     * @return true or false.
     */
    public static boolean isTextPresent(final By locator, String text) {
        try {
            waitForPageLoaded();
            String threadId = String.valueOf(Thread.currentThread().getId());
            boolean isPresent = WebDriverHandler.getDriver(threadId).findElement(locator).getText().contains(text);
            Log.logger().info("-> The Text [" + text + "] is " + (!isPresent ? "not" : "") + " present >>> " + locator);
            return isPresent;
        } catch (NullPointerException e) {
            Log.logger().warn("-> The Text [" + text + "] is not present >>> " + locator, e);
            return false;
        }
    }

    /**
     * This method allows clear web cookies.
     */
    public static void clearCookies() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriverHandler.getDriver(threadId).manage().deleteAllCookies();
        Log.logger().info("-> The web cookies has been CLEANED");
    }

    /**
     * This method allows wait until page is load for a time.
     * @param seconds The time to load page.
     */
    public static void waitForPageLoadTimeout(int seconds) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriverHandler.getDriver(threadId).manage().timeouts().pageLoadTimeout(seconds, TimeUnit.SECONDS);
        Log.logger().info("-> The page has waited with time of timeout of " + seconds + " seconds.");
    }

    /**
     * This method allows wait until page is load using javascript.
     */
    public static void waitForPageLoaded() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
            }
        };
        try {
            Log.logger().info("-> Waiting until page to be loaded");
            WebDriverHandler.getWait(threadId).until(expectation);
        } catch(Throwable error) {
            Log.logger().info("-> Timeout waiting for Page Load Request to complete.");
        }
    }

    /**
     * This method executes a java script instruction to wait for page load completely.
     */
    public static void waitForPageFullyLoaded() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        JavascriptExecutor js = (JavascriptExecutor)WebDriverHandler.getDriver(threadId);
        String command = "window.onload=function(){}";
        js.executeScript(command);
    }

    /**
     * This method allows wait for a time until page is loaded.
     * @param seconds The wait time.
     */
    public static void waitForNSeconds(double seconds) {
        try {
            int time = (int) (seconds * 1000);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Log.logger().error("-> Error in Thread sleep: ", e);
            e.printStackTrace();
        }
    }

    /**
     * This method execute the click  action on a web element.
     * @param element The element to execute action.
     */
    public static void clickOn2(WebElement element) {
        try {
            element.click();
            Log.logger().info("-> The click action was performed on web element >>> " + element);
        } catch (Exception e) {
            Log.logger().error("-> Error to try perform the click action on web element >>> " + element, e);
        }
    }

    /**
     * This method execute the click  action on a web element.
     * @param element The element to execute action.
     */
    public static void clickOn(WebElement element) {
        try {
            String threadId = String.valueOf(Thread.currentThread().getId());
            WebDriverHandler.getAction(threadId).moveToElement(element, 6, 6).click().build().perform();
            Log.logger().info("-> The click action was performed on web element >>> " + element);
        } catch (Exception e) {
            Log.logger().error("-> Error to try perform the click action on web element >>> " + element, e);
        }
    }

    /**
     * This method waits for element and execute the click action.
     * @param locator The selector to find the element.
     */
    public static WebElement waitAndClickOn(final By locator) {
        WebElement element = waitForElement(locator);
        if (element != null) {
            clickOn(element);
        }
        return element;
    }

    /**
     * This method waits for element and execute the click action.
     * @param locator The selector to find the element.
     */
    public static WebElement waitAndClickOn2(final By locator) {
        WebElement element = waitForElement(locator);
        if (element != null) {
            clickOn2(element);
        }
        return element;
    }

    /**
     * This method executes the double click action on an element.
     * @param element The element to execute action.
     */
    public static void dobleClickOn(WebElement element) {
        try {
            String threadId = String.valueOf(Thread.currentThread().getId());
            WebDriverHandler.getAction(threadId).moveToElement(element).doubleClick().build().perform();
            Log.logger().info("-> The double click action was performed on web element >>> " + element);
            //WebDriverHandler.getAction(threadId).doubleClick(element).build().perform();
        } catch (Exception e) {
            Log.logger().error("-> Error to try perform the double click action on web element >>> " + element, e);
        }
    }

    /**
     * This method waits for element and executes the double click action on an element.
     * @param locator The selector to find the element.
     */
    public static void waitAndDobleClickOn(final By locator) {
        WebElement element = waitForElement(locator);
        if (element != null) {
            dobleClickOn(element);
        }
    }

    /**
     * This method allows change focus (from window or iframe) to iframe.
     * @param locator The selector to find the element.
     */
    public static void switchToIFrame(final By locator) {
        WebElement element = waitForElement(locator);
        String threadId = String.valueOf(Thread.currentThread().getId());
        if (element != null) {
            WebDriverHandler.getDriver(threadId).switchTo().frame(element);
            Log.logger().info("-> The focus was switched to the iframe >>> " + locator);
        }
    }

    /**
     * This method allows return to main window from another window or iframe.
     */
    public static void switchToDefaultContent() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriverHandler.getDriver(threadId).switchTo().defaultContent();
        Log.logger().info("-> The focus was switched to main window");
    }

    /**
     * This method is used when clicking on some page element causes a new window to pop up.
     * @param element The element that opens a window.
     * @return A target locator which is the parent window of the new window opened.
     */
    public static String clickAndPopupWindow(WebElement element) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        String oldWindow = WebDriverHandler.getDriver(threadId).getWindowHandle();
        Set<String> oldSet = WebDriverHandler.getDriver(threadId).getWindowHandles();

        try {
            clickOn(element);
        } catch (Exception ex) {
            //missing some information in log file
            return oldWindow;
        }

        boolean wasSwitch = executeSwitchWindow(oldSet);

        if (!wasSwitch) {
            //some log
        }

        return oldWindow;
    }

    /**
     * This method is used when clicking on some page element causes a new window to pop up.
     * @param locator The selector to find the element.
     * @return A target locator which is the parent window of the new window opened.
     */
    public static String clickAndSwitchToSecondWindow(final By locator) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        String firstWinHandle = WebDriverHandler.getDriver(threadId).getWindowHandle();
        waitAndClickOn(locator);
        //wait till two windows are not opened
        waitForNumberOfWindowsToEquals(2);//this method is for wait
        Set<String> handles = WebDriverHandler.getDriver(threadId).getWindowHandles();
        handles.remove(firstWinHandle);
        String secondWinHandle = handles.iterator().next();
        if (secondWinHandle != firstWinHandle) {
            try {
                WebDriverHandler.getDriver(threadId).switchTo().window(secondWinHandle);
                Log.logger().info("-> The cofus was switched to window >>> " + secondWinHandle);
            } catch (Exception e) {
                Log.logger().error("-> Error to trying switch to second window >>> " + secondWinHandle);
            }

        }
        return firstWinHandle;
    }

    /**
     * This method is used to change of window.
     * @param targetLocator The target locator of the old window.
     */
    public static void switchToWindow(String targetLocator) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriverHandler.getDriver(threadId).switchTo().window(targetLocator);
        Log.logger().info("-> The cofus was switched to window >>> " + targetLocator);
    }

    /**
     * This method retrieve the url from current page.
     * @return The current url.
     */
    public static String getCurrentUrl() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        String currentUrl = WebDriverHandler.getDriver(threadId).getCurrentUrl();
        return currentUrl;
    }

    /**
     * This method is used to execute the switch window action from current window to new window.
     * @param oldSet The whole windows.
     * @return boolean.
     */
    private static boolean executeSwitchWindow(Set<String> oldSet) {
        String newWindow;
        Iterator<String> iterator;
        Set<String> newSet = oldSet;
        String threadId = String.valueOf(Thread.currentThread().getId());

        while (newSet.size() == oldSet.size()) { // Wait for the new window to be displayed
            if (TIMER_COUNTER == TIMEOUT) {
                TIMER_EXPIRED = true;
                break;
            } else {
                TIMER_COUNTER += 1000;
            }
            waitForNSeconds(1);// Wait .1 seconds
            newSet = WebDriverHandler.getDriver(threadId).getWindowHandles(); // See if the new window is up

        }
        if (TIMER_EXPIRED) {
            Log.logger().error("-> Timeout >>> The new window was not created after clicking");
            return false;
        }

        iterator = newSet.iterator();
        do {   // Find which one is the new window
            newWindow = iterator.next();
        } while (oldSet.contains(newWindow) && iterator.hasNext());

        switchToWindow(newWindow);
        resetTimerValues();
        waitForPageLoaded();
        Log.logger().info("-> Switching between windows was executed successfully");
        return true;
    }

    /**
     * This method reset the custom timer values.
     */
    private static void resetTimerValues() {
        TIMER_EXPIRED = false;
        TIMER_COUNTER = 0;
    }

    /**
     * This method waits for the element to be present in the DOM, and displayed on the page.
     * @param locator The selector to find the element.
     * @return The first WebElement using the given method, or null (if the timeout is reached).
     */
    public static WebElement waitForElement(final By locator) {
        WebElement element = null;
        String threadId = String.valueOf(Thread.currentThread().getId());
        try{
            nullifyImplicitWait(); //nullify implicitlyWait()
            Log.logger().info("-> Waiting for element to be present in the DOM, and displayed >>> " + locator);
            element = WebDriverHandler.getWait(threadId).until(ExpectedConditions.visibilityOfElementLocated(locator));
            setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
            Log.logger().info("-> Web element recovered >>> " + element);
            return element; //return the element
        } catch (Exception e) {
            Log.logger().error("-> Error occurred while trying to get the web element >>> " + locator, e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method waits for the element to be present in the DOM, regardless of being displayed or not.
     * @param locator The selector to find the element.
     * @return The first WebElement using the given method, or null (if the timeout is reached).
     */
    public static WebElement waitForElementPresent(final By locator) {
        WebElement element = null;
        String threadId = String.valueOf(Thread.currentThread().getId());
        try{
            nullifyImplicitWait(); //nullify implicitlyWait()
            Log.logger().info("-> Waiting for element to be present in the DOM >>> " + locator);
            element = WebDriverHandler.getWait(threadId).until(ExpectedConditions.presenceOfElementLocated(locator));
            setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
            Log.logger().info("-> Web element recovered >>> " + element);
            return element; //return the element
        } catch (Exception e) {
            Log.logger().error("-> Error occurred while trying to get the web element >>> " + locator, e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method waits for the element to be clickable in the DOM.
     * @param locator The selector to find the element.
     * @return The first WebElement using the given method, or null (if the timeout is reached).
     */
    public static WebElement waitForElementClickable(final By locator) {
        WebElement element = null;
        try{
            String threadId = String.valueOf(Thread.currentThread().getId());
            nullifyImplicitWait(); //nullify implicitlyWait()
            Log.logger().info("-> Waiting for web element to be clickable in the DOM >>> " + locator);
            element = WebDriverHandler.getWait(threadId).until(ExpectedConditions.elementToBeClickable(locator));
            setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
            Log.logger().info("-> Web element recovered >>> " + element);
            return element; //return the element
        } catch (Exception e) {
            Log.logger().error("-> Error occurred while trying to get the web element >>> " + locator, e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method waits for the elements to be presented in the DOM.
     * @param locator The selector to find the element.
     * @return The List WebElements using the given method, or null (if the timeout is reached).
     */
    public static List<WebElement> waitForListElementsPresent(final By locator) {
        List<WebElement> elements = null;
        try{
            String threadId = String.valueOf(Thread.currentThread().getId());
            nullifyImplicitWait(); //nullify implicitlyWait()
            Log.logger().info("-> Waiting for web elements to be presented in the DOM >>> " + locator);
            WebDriverHandler.getWait(threadId).until((new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return areElementsPresent(locator);
                }
            }));
            elements = WebDriverHandler.getDriver(threadId).findElements(locator);
            setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
            Log.logger().info("-> Web elements recovered [" + elements.size() + "] >>> " + locator);
            return elements; //return the element
        } catch (Exception e) {
            Log.logger().error("-> Error occurred while trying to get the web elements >>> " + locator, e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method waits for an element to appear on the refreshed web-page.
     * @param locator The selector to find the element.
     * @return The first WebElement using the given method, or null (if the timeout is reached).
     */
    public static WebElement waitForElementRefresh(final By locator) {
        WebElement element;
        try{
            String threadId = String.valueOf(Thread.currentThread().getId());
            nullifyImplicitWait(); //nullify implicitlyWait()
            Log.logger().info("-> Waiting for an element to appear on the refreshed web-page >>> " + locator);
            WebDriverHandler.getWait(threadId).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    driverObject.navigate().refresh(); //refresh the page ****************
                    return isElementPresentAndVisible(locator);
                }
            });
            element = WebDriverHandler.getDriver(threadId).findElement(locator);
            setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
            Log.logger().info("-> Web element recovered >>> " + element);
            return element; //return the element
        } catch (Exception e) {
            Log.logger().error("-> Error occurred while trying to get the web element >>> " + locator, e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method waits for the Text to be present in the given element, regardless of being displayed or not.
     * @param locator The selector of the given element, which should contain the text.
     * @param text The text we are looking.
     * @return boolean.
     */
    public static boolean waitForTextPresent(final By locator, final String text) {
        boolean isPresent = false;
        try{
            String threadId = String.valueOf(Thread.currentThread().getId());
            nullifyImplicitWait(); //nullify implicitlyWait()
            Log.logger().info("-> Waiting for the Text [" + text + "] to be present >>> " + locator);
            WebDriverHandler.getWait(threadId).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                        return isTextPresent(locator, text); //is the Text in the DOM
                }
            });
            isPresent = isTextPresent(locator, text);
            setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
            return isPresent;
        } catch (Exception e) {
            Log.logger().error("-> Error occurred while trying to verify Text [" + text + "] to be present >>> " + locator, e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method waits for the condition of JavaScript.
     * @param javaScript The javaScript condition we are waiting.
     * @return boolean true or false(condition fail, or if the timeout is reached).
     */
    public static boolean waitForJavaScriptCondition(final String javaScript) {
        boolean jscondition = false;
        try{
            String threadId = String.valueOf(Thread.currentThread().getId());
            nullifyImplicitWait(); //nullify implicitlyWait()
            Log.logger().info("-> Waiting for the condition of JavaScript >>> " + javaScript);
            WebDriverHandler.getWait(threadId).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
                }
            });
            jscondition =  (Boolean) ((JavascriptExecutor) WebDriverHandler.getDriver(threadId)).executeScript(javaScript);
            setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
            Log.logger().info("-> JavaScript condition result >>> " + jscondition);
            return jscondition;
        } catch (Exception e) {
            Log.logger().error("-> Error occurred while trying to execute the JavaScript condition >>> [" + javaScript + "]", e);
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method execute javascript.
     * @param javaScript The javascript code to execute.
     * @return
     */
    public static void executeJavaScript(String javaScript) {
        try{
            String threadId = String.valueOf(Thread.currentThread().getId());
            nullifyImplicitWait(); //nullify implicitlyWait()
            JavaScriptUtils.execute(javaScript, WebDriverHandler.getDriver(threadId));
            setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
            Log.logger().info("-> JavaScript executed >>> [ " + javaScript + " ]");
        } catch (Exception e) {
            Log.logger().error("-> Error occurred while trying to execute the JavaScript >>> [" + javaScript + "]", e);
            e.printStackTrace();
        }
    }

    /**
     * This method waits for the completion of Ajax jQuery processing by checking "return jQuery.active == 0" condition.
     * @return boolean true or false(condition fail, or if the timeout is reached);
     */
    public static boolean waitForJQueryProcessing(){
        boolean jQcondition = false;
        try{
            String threadId = String.valueOf(Thread.currentThread().getId());
            nullifyImplicitWait(); //nullify implicitlyWait()
            Log.logger().info("-> Waiting for the completion of Ajax jQuery");
            WebDriverHandler.getWait(threadId).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                        return (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0");
                }
            });
            jQcondition = (Boolean) ((JavascriptExecutor) WebDriverHandler.getDriver(threadId)).executeScript("return jQuery.active == 0");
            setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
            Log.logger().info("-> Ajax jQuery condition result >>> " + jQcondition);
            return jQcondition;
        } catch (Exception e) {
            Log.logger().error("-> Error occurred while trying to execute the JavaScript condition >>> [return jQuery.active == 0]", e);
            e.printStackTrace();
        }
        return jQcondition;
    }

    /**
     *
     */
    public static void waitForExtJSProcessing() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriverHandler.getWait(threadId).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return (Boolean) ((JavascriptExecutor) d)
                .executeScript("return !(ExtJSUtil.hasActiveAjaxCalls(Ext.Ajax.requests && !_.isEmpty(Ext.Ajax.requests));");
            }
        });
    }

    /**
     * This method wait for
     * @param locator
     * @return
     */
    public static boolean waitForInvisibilityOfElement(final By locator) {
        boolean isInvisible = false;
        try {
            String threadId = String.valueOf(Thread.currentThread().getId());
            WebDriverHandler.getWait(threadId).until((new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return areElementsPresent(locator);
                }
            }));
            List<WebElement> elements = WebDriverHandler.getDriver(threadId).findElements(locator);
            for (int i = 0; ((i < elements.size()) && !isInvisible); i++) {
                if (elements.get(i).isDisplayed()) {
                    while (elements.get(i).isDisplayed()) {
                        waitForNSeconds(1);
                    }
                    isInvisible = true;
                }
            }
            return isInvisible;
            //WebElement element = WebDriverHandler.getWait(threadId).until(ExpectedConditions.visibilityOfElementLocated(locator));
            //boolean result = WebDriverHandler.getWait(threadId).until(ExpectedConditions.invisibilityOfElementLocated(locator));
            //return result;
        } catch (Exception e) {
            return isInvisible;
        }
    }

    public static void nullifyImplicitWait() {
        //WebDriverHandler.getDriver(threadId).manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
    }

    public static void setImplicitWait(int waitTimeInSeconds) {
        //WebDriverHandler.getDriver(threadId).manage().timeouts().implicitlyWait(waitTimeInSeconds, TimeUnit.SECONDS);
    }

    public static void resetImplicitWait() {
        nullifyImplicitWait(); //nullify implicitlyWait()
        setImplicitWait(WebDriverHandler.DEFAULT_WAIT_PAGE); //reset implicitlyWait
    }

    public static void resetImplicitWait(int newWaittimeInSeconds) {
        nullifyImplicitWait(); //nullify implicitlyWait()
        setImplicitWait(newWaittimeInSeconds); //reset implicitlyWait
    }

    /**
     * This method get the number of windows or tabs [browser]
     * @return Tha quantity windows present
     */
    public static int getNumberWindows() {
        return getWindows().size();
    }

    /**
     * This method recover all windows managed with the current driver.
     * @return The whole windows
     */
    public static List<String> getWindows() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        ArrayList<String> windows = new ArrayList<String>(WebDriverHandler.getDriver(threadId).getWindowHandles());
        Log.logger().info("-> Windows gotten [" + windows.size() + "] >>> " + windows);
        return windows;
    }

    /**
     * This method recovers all select option labels.
     * @param locator The selector to find the element.
     * @return A list of select option labels.
     */
    public static List<String> getSelectLabels(final By locator) {
        List<WebElement> options = getSelectElements(locator);
        List<String> labelsList = new ArrayList<String>();
        for (WebElement element : options) {
            labelsList.add(element.getText());
        }
        return labelsList;
    }

    /**
     * This method recovers all select option values.
     * @param locator The selector to find the element.
     * @return A list of select option values.
     */
    public static List<String> getSelectValues(final By locator) {
        List<WebElement> options = getSelectElements(locator);
        List<String> valuesList = new ArrayList<String>();
        for (WebElement element : options) {
            valuesList.add(element.getAttribute("value"));
        }
        return valuesList;
    }

    /**
     * This method recovers all Web Element from select option element.
     * @param locator The selector to find the element.
     * @return A list of Web Elements.
     */
    public static List<WebElement> getSelectElements(final By locator) {
        WebElement optionElement = waitForElement(locator);
        Select select = new Select(optionElement);
        List<WebElement> options = select.getOptions();
        return options;
    }

    /**
     * This method select an element by label into a select option and return a web element
     * @param locator The selector to find the element.
     * @param label The label to match.
     * @return A web element.
     */
    public static WebElement getSelectElementByLabel(final By locator, String label) {
        WebElement selectElement = WebUtils.waitForElement(locator);
        Select multiSelect = new Select(selectElement);
        List<WebElement> elements = multiSelect.getOptions();
        for (WebElement webElement : elements) {
            if(label.equals(webElement.getText())) {
                return webElement;
            }
        }
        return null;
    }

    /**
     * This method retrieve the first label from the select option element.
     * @param locator The selector to find the element.
     * @return A select option label.
     */
    public static String getFirstSelectedLabel(final By locator) {
        WebElement selectElement = WebUtils.waitForElement(locator);
        Select select = new Select(selectElement);
        return select.getFirstSelectedOption().getText();
    }

    /**
     * This method recover the first element from the select option
     * @param locator The selector to find the element.
     * @return A web element.
     */
    public static WebElement getFirstSelectedOption(final By locator) {
        WebElement selectElement = WebUtils.waitForElement(locator);
        Select select = new Select(selectElement);
        return select.getFirstSelectedOption();
    }

    /**
     * This method select all elements in a multiselect option.
     * @param locator The selector to find the element.
     */
    public static void setMultiSelectAll(final By locator) {
        WebElement selectElement = WebUtils.waitForElement(locator);
        Select multiSelect = new Select(selectElement);
        List<WebElement> elements = multiSelect.getOptions();
        for (WebElement webElement : elements) {
            multiSelect.selectByVisibleText(webElement.getText());
        }
    }

    /**
     * This method unselect all elements in a multiselect option.
     * @param locator The selector to find the element.
     */
    public static void setMultiDeselectAll(final By locator) {
        WebElement selectElement = WebUtils.waitForElement(locator);
        Select multiSelect = new Select(selectElement);
        multiSelect.deselectAll();
    }

    /**
     * This method select an option according text.
     * @param locator The selector to find the element.
     * @param text The text to select
     */
    public static void setSelectByVisibleText(final By locator, String... label) {
        WebElement selectElement = WebUtils.waitForElement(locator);
        Select multiSelect = new Select(selectElement);
        for (String lbl : label) {
            multiSelect.selectByVisibleText(lbl);
        }
    }

    /**
     * This method select an option according index.
     * @param locator The selector to find the element.
     * @param index The index option.
     */
    public static void setSelectByIndex(final By locator, int index) {
        WebElement optionElement = waitForElement(locator);
        Select select = new Select(optionElement);
        select.selectByIndex(index);
    }

    /**
     * This method select an option according value.
     * @param locator The selector to find the element.
     * @param value The option value.
     */
    public static void setSelectByValue(final By locator, String value) {
        WebElement optionElement = waitForElement(locator);
        Select select = new Select(optionElement);
        select.selectByValue(value);
    }

    /**
     * This method capture a screen region.
     * @param locator The selector to find the element.
     * @param imageFileName The file name that will be used to write the file.
     * @return A image file location.
     */
    public static String shootWebElement(By locator, String imageFileName) {
        WebElement element = waitForElement(locator);
        return shootWebElement(element, imageFileName);
    }

    /**
     * This method capture a screen region.
     * @param element The region as web element.
     * @param imageFileName The file name that will be used to write the file.
     * @return A image file location.
     */
    public static String shootWebElement(WebElement element, String imageFileName) {
        Point p = element.getLocation();
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();

        return ScreenshotHandler.captureScreenRegion(p, width, height, imageFileName);
    }

    /**
     * This method allows to drag a web element and then drop in another location.
     * @param dragElementLocator The locator to drag.
     * @param dropElementLocator The locator to drop.
     */
    public static void dragAndDrop(final By dragElementLocator, By dropElementLocator) {
        WebElement source = waitForElement(dragElementLocator);
        WebElement target = waitForElement(dropElementLocator);
        dragAndDrop(source, target);
    }

    /**
     * This method allows to drag a web element and then drop in another location.
     * @param dragElement The element to drag.
     * @param dropElement The element to drop.
     */
    public static void dragAndDrop(WebElement dragElement, WebElement dropElement) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriverHandler.getAction(threadId).dragAndDrop(dragElement, dropElement).perform();
    }

    /**
     * This method allows to drag a web element and then drop in another location.
     * @param dragElementLocator The locator to drag.
     * @param dropElementLocator The locator to drop.
     */
    public static void dragAndDrop2(final By dragElementLocator, By dropElementLocator) {
        WebElement source = waitForElement(dragElementLocator);
        WebElement target = waitForElement(dropElementLocator);
        dragAndDrop2(source, target);
    }

    /**
     * This method allows to drag a web element and then drop in another location.
     * @param dragElement The element to drag.
     * @param dropElement The element to drop.
     */
    public static void dragAndDrop2(WebElement dragElement, WebElement dropElement) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        Action dragAndDrop = WebDriverHandler.getAction(threadId).clickAndHold(dragElement)
        .moveToElement(dropElement)
        .release(dropElement)
        .build();  // Get the action
        dragAndDrop.perform(); // Execute the Action
    }

    /**
     * This method recover the html text using javascript.
     * @param element The web element from which will be recovered the html text.
     * @return A html plain text.
     */
    public static String getInnerHtml(WebElement element) {
        // another option to get html text from web element is the following [element.getAttribute("innerHTML")]
        String threadId = String.valueOf(Thread.currentThread().getId());
        return JavaScriptUtils.execute("return arguments[0].innerHTML", WebDriverHandler.getDriver(threadId), element);
    }

    /**
     * This method recover the html text using javascript.
     * @param locator The selector to find an element.
     * @return A html plain text.
     */
    public static String getInnerHtml(By locator) {
        WebElement element = waitForElement(locator);
        String threadId = String.valueOf(Thread.currentThread().getId());
        return JavaScriptUtils.execute("return arguments[0].innerHTML", WebDriverHandler.getDriver(threadId), element);
    }

    /**
     * execute javascript method to scroll to top page
     */
    public static void windowScrollUp() {
        executeJavaScript("window.scroll(0,0)");
        waitForNSeconds(0.5);
    }

    /**
     * execute javascript method to scroll to bottom page
     */
    public static void windowScrollDown() {
        executeJavaScript("window.scroll(0, document.documentElement.scrollHeight)");
        waitForNSeconds(0.5);
    }

    /**
     * This method move up the scroll bar subtract the header size
     * @param element The web element to be find.
     */
    public static void scrollIntoViewHeaderOffset(WebElement element) {
        scrollIntoView(element, 0, -150);
    }

    /**
     * This method move the scroll bar according web element location.
     * @param element The web element to be find.
     */
    public static void scrollIntoView(WebElement element) {
        scrollIntoView(element, 0, 0);
    }

    /**
     * This method move the scroll bar according web element location.
     * @param element The web element to be find.
     * @param xOffset The x offset to move.
     * @param yOffset The y offset to move.
     */
    public static void scrollIntoView(WebElement element, int xOffset, int yOffset) {
        Locatable hoverItem = (Locatable) element;
        Point p = hoverItem.getCoordinates().inViewPort();
        String js = "window.scrollBy(" + (p.getX() + xOffset) + "," + (p.getY() + yOffset) +");";
        executeJavaScript(js);
        waitForNSeconds(0.5);
    }

    /**
     * This method move the scroll bar according web element location.
     * @param element The web element to be find.
     */
    public static void scrollIntoView2(WebElement element) {
        String threadId = String.valueOf(Thread.currentThread().getId());
        JavaScriptUtils.execute("arguments[0].scrollIntoView();", WebDriverHandler.getDriver(threadId), element);
        waitForNSeconds(0.5);
    }

    /**
     * This method verifies if the element is displayed or not in DOM
     * @param element The web element.
     * @return A true/false value.
     */
    public static boolean isElementDisplayed(WebElement element) {
        try {
            if (element.isDisplayed()) {
                return true;
            }
        } catch (Exception e) {
            Log.logger().warn("-> Error while trying to ask for element displayed >>> web element [" + element + "]", e);
        }
        return false;
    }

    /**
     * This method waits for N windows opened.
     * @param numberOfWindows The number of windows to wait.
     */
    private static void waitForNumberOfWindowsToEquals(final int numberOfWindows) {
        //Making a new expected condition
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriverHandler.getWait(threadId).until(new ExpectedCondition<Boolean>() {
            @Override public Boolean apply(WebDriver driver) {
                return (driver.getWindowHandles().size() == numberOfWindows);
              }
        });
    }
}
