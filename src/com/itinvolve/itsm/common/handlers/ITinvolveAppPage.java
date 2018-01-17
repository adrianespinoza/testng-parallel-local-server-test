package com.itinvolve.itsm.common.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class ITinvolveAppPage {
    private By mainAppLocator = By.cssSelector("a[title='Main App Tab']");

    public ITinvolveHomePage goToITinvolveMainAppPage() {
        TestCaseLogger.log("Go to ITinvolve Main App Page.");
        //By mainAppLocator = By.xpath("//a[contains(text(),'Main App')]");
        WebElement element = WebUtils.waitForElement(mainAppLocator);
        if (element != null) {
            element.click();
        }
        //WebUtils.waitForPageLoaded();
        return (ITinvolveHomePage) HandlerFactory.getInstance(ITinvolveHomePage.class);
    }
}
