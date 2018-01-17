/*
 * Copyright (c) ITinvolve, Inc. All Rights Reserved.
 */
package com.itinvolve.itsm.framework.env;

import static com.itinvolve.itsm.framework.env.Setup.BROWSER;
import static com.itinvolve.itsm.framework.env.Setup.PATH_DRIVER_CHROME;
import static com.itinvolve.itsm.framework.env.Setup.PATH_DRIVER_IE;
import static com.itinvolve.itsm.framework.env.Setup.START_URL;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.opera.core.systems.OperaDriver;

/**
 * Web driver handler class. Provide methods to manage the browser window.
 * @author Adrian Espinoza
 *
 */
public class WebDriverHandler {
    /** Default wait time for an element. 15  seconds. */
    public static final int DEFAULT_WAIT_ELEMENT = 20;

    /** Default wait time for a page to be displayed.  10 seconds. */
    public static final int DEFAULT_WAIT_PAGE = 15;

    /** Store drivers to each thread */
    private static Map<String, WebDriver> driverCollector;

    /** Store actions to each thread */
    private static Map<String, Actions> actionCollector;

    /** Store waits to each thread */
    private static Map<String, WebDriverWait> waitCollector;

    /** Store browser names */
    private static Map<String, String> browserNameCollector;

    /**
     * This method open the initial browser window according type browser configured in the file configuration.
     * @throws IOException
     */
    public static WebDriver openWindows(boolean isParallel, String browser, String version, String platform){
        return selectBrowser(isParallel, browser, version, platform);
    }

    /**
     * This method closes the current focused window.
     */
    public static void closeWindows() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriver driver = getDriver(threadId);
        if (driver != null) {
            driver.quit();
            driver = null;
            System.out.println("-> After closing driver: " + driver);
        }
    }

    /**
     * This method allows navigate to start page.
     */
    public static void navigateToStartUrl() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriver driver = getDriver(threadId);
        driver.navigate().to(START_URL);
    }

    /**
     * This method clear all Internet cookies.
     */
    public static void clearCookies() {
        String threadId = String.valueOf(Thread.currentThread().getId());
        WebDriver driver = getDriver(threadId);
        driver.manage().deleteAllCookies();
    }

    /**
     * This method restart the web driver.
     */
    public static WebDriver restartWebDriver() {
        closeWindows();
        WebDriver driver = openWindows(Setup.EXECUTE_IN_PARALLEL, Setup.BROWSER, Setup.VERSION, Setup.PLATFORM);
        if (driver != null) {
            navigateToStartUrl();
        }
        return driver;
    }

    /**
     * This method chooses a browser according parameter sent by command line.
     * @throws IOException
     */
    private static WebDriver selectBrowser(boolean isParallel, String browser, String version, String platform) {
        WebDriver driver = null;
        if (isParallel) {
            driver = buildRemoteWebDriver(browser, version, platform);
        } else {
            driver = buildWebDriver(browser);
        }

        if (driver != null) {
            System.out.println("Open WebDriver(" + browser + ", " + driver.getClass().getCanonicalName() + ")");
            Log.logger().info("-> Open WebDriver(" + browser + ", " + driver.getClass().getCanonicalName() + ")");
            driver.manage().window().maximize();
            //driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_PAGE, TimeUnit.SECONDS);
            //this.driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);

            String threadId = String.valueOf(Thread.currentThread().getId());
            putDriver(threadId, driver);

            WebDriverWait wait = new WebDriverWait(driver, DEFAULT_WAIT_ELEMENT);
            putWait(threadId, wait);

            Actions action = new Actions(driver);
            putAction(threadId, action);

            putBrowserName(threadId, browser);
        } else {
            System.out.println("Error browser value not supported -> " + browser + "  command line >ant runATC -Dbrowser=[chrome,firefox,ie,opera,safari]");
            Log.logger().error("-> Error browser value not supported >>> " + browser + "  command line >ant runATC -Dbrowser=[chrome,firefox,ie,opera,safari]");
        }
        return driver;
    }

    /**
     * This method selects the web driver when this is null;
     */
    private static WebDriver buildWebDriver(String browser) {
        WebDriver driver;
        if (browser.toLowerCase().equals(BrowserType.FIREFOX.getBrowser())) {
            //driver = new FirefoxDriver(generateDesiredCapabilities(BrowserType.FIREFOX, DesiredCapabilities.firefox()));
            driver = new FirefoxDriver();
        } else if (browser.toLowerCase().equals(BrowserType.CHROME.getBrowser())) {
            System.setProperty("webdriver.chrome.driver", PATH_DRIVER_CHROME);
            driver = new ChromeDriver(generateDesiredCapabilities(BrowserType.CHROME));
        } else if (browser.toLowerCase().equals(BrowserType.IE.getBrowser()) || BROWSER.toLowerCase().equals("ie")) {
            System.setProperty("webdriver.ie.driver", PATH_DRIVER_IE);
            driver = new InternetExplorerDriver(generateDesiredCapabilities(BrowserType.IE));
        } else if (browser.toLowerCase().equals(BrowserType.OPERA.getBrowser())) {
            driver = new OperaDriver(generateDesiredCapabilities(BrowserType.OPERA));
        } else if (browser.toLowerCase().equals(BrowserType.SAFARI.getBrowser())) {
            driver = new SafariDriver(generateDesiredCapabilities(BrowserType.SAFARI));
        } else {
            driver = new HtmlUnitDriver();
        }
        return driver;

    }

    /**
     * This method configures the desired capabilities for a browser.
     * @param capabilityType The browser type.
     * @return A desired capabilities.
     */
    private static DesiredCapabilities generateDesiredCapabilities(BrowserType capabilityType) {
        DesiredCapabilities capabilities;

        switch (capabilityType) {
          case IE:
            capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
            capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
            capabilities.setCapability("requireWindowFocus", false);
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            break;
          case SAFARI:
            capabilities = DesiredCapabilities.safari();
            capabilities.setCapability("safari.cleanSession", true);
            break;
          case OPERA:
            capabilities = DesiredCapabilities.opera();
            capabilities.setCapability("opera.arguments", "-nowin -nomail");
            break;
          case CHROME:
            capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability("chrome.switches", Arrays.asList("--no-default-browser-check"));
            HashMap<String, String> chromePreferences = new HashMap<String, String>();
            chromePreferences.put("profile.password_manager_enabled", "false");
            capabilities.setCapability("chrome.prefs", chromePreferences);
            break;
          case FIREFOX:
            capabilities = DesiredCapabilities.firefox();
            break;
          default:
            capabilities = DesiredCapabilities.htmlUnit();
            capabilities.setCapability("javascriptEnabled", "true");
        }

        return capabilities;
    }

    /**
     * This method configures the desired capabilities for a browser.
     * @param capabilityType The browser type.
     * @return A desired capabilities.
     */
    private static DesiredCapabilities generateRemoteDesiredCapabilities(String browser, String version, String platform) {
        BrowserType capabilityType = getBrowserType(browser);

        DesiredCapabilities capabilities = generateDesiredCapabilities(capabilityType);

        capabilities.setCapability("platform",platform);
        capabilities.setCapability("browserName", browser);
        capabilities.setCapability("browserVersion", version);
        capabilities.setCapability("project", "P1");
        capabilities.setCapability("build", "1.0");

        return capabilities;
    }

    public static WebDriver buildRemoteWebDriver(String browser, String version, String platform) {
        System.out.println("-> Current thread id: " + Thread.currentThread().getId() + " name: " + Thread.currentThread().getName());
        DesiredCapabilities capability = generateRemoteDesiredCapabilities(browser, version, platform);
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL(Setup.SELENIUM_REMOTE_ADDRESS), capability);
            return driver;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return driver;
    }

    public static BrowserType getBrowserType(String browser) {
        BrowserType[] bTypes = BrowserType.values();
        for (int i = 0; i < bTypes.length; i++) {
            if (bTypes[i].getBrowser().equals(browser)) {
                return bTypes[i];
            }
        }
        return null;
    }

    public static void openStartPage(boolean inParallel, String browser, String version, String platform, String credentialId) {
        Log.logger().info("-> The tests will be execute in " + browser + " browser");
        WebDriver driver = WebDriverHandler.openWindows(inParallel, browser, version, platform);

        Log.logger().info("-> The " + browser + " browser was OPENED");

        if (driver != null) {
            boolean wasSetup = Setup.setupEnvironment(credentialId);//load user and configure the environment, urls auth end point
            Log.logger().info("-> The environment was CONFIGURED");

            if (wasSetup) {
                WebDriverHandler.navigateToStartUrl();
                Log.logger().info("-> The " + Setup.START_URL + " page has been OPEN");

                HandlerFactory.clear();//clear the factory cache by suite
                System.out.println("-> The " + browser + "browser was opened...");
            }
        }
    }

    public static WebDriver getDriver(String key) {
        if (driverCollector == null) {
            return null;
        }
        return driverCollector.get(key);
    }

    public static boolean containsDriver(String key) {
        if (driverCollector == null) {
            return false;
        }
        return driverCollector.containsKey(key);
    }

    public static void putDriver(String key, WebDriver driver) {
        if (driverCollector == null) {
            driverCollector = new HashMap<String, WebDriver>();
        }
        driverCollector.put(key, driver);
    }

    public static Actions getAction(String key) {
        if (actionCollector == null) {
            return null;
        }
        return actionCollector.get(key);
    }

    public static boolean containsAction(String key) {
        if (actionCollector == null) {
            return false;
        }
        return actionCollector.containsKey(key);
    }

    public static void putAction(String key, Actions action) {
        if (actionCollector == null) {
            actionCollector = new HashMap<String, Actions>();
        }
        actionCollector.put(key, action);
    }

    public static WebDriverWait getWait(String key) {
        if (waitCollector == null) {
            return null;
        }
        return waitCollector.get(key);
    }

    public static boolean containsWait(String key) {
        if (waitCollector == null) {
            return false;
        }
        return waitCollector.containsKey(key);
    }

    public static void putWait(String key, WebDriverWait wait) {
        if (waitCollector == null) {
            waitCollector = new HashMap<String, WebDriverWait>();
        }
        waitCollector.put(key, wait);
    }

    public static String getBrowserName(String key) {
        if (browserNameCollector == null) {
            return null;
        }
        return browserNameCollector.get(key);
    }

    public static boolean containsBrowserName(String key) {
        if (browserNameCollector == null) {
            return false;
        }
        return browserNameCollector.containsKey(key);
    }

    public static void putBrowserName(String key, String browserName) {
        if (browserNameCollector == null) {
            browserNameCollector = new HashMap<String, String>();
        }
        browserNameCollector.put(key, browserName);
    }
}
