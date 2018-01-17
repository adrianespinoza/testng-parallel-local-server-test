package com.itinvolve.itsm.change.handlers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class ITinvolveChangesPage {
    private By firstelemLocator = By.cssSelector("tr[class^='dataRow even even first'] > td[class^='dataCell'] > a[class='linkeableField']");//By.xpath("//*/tr[1]/td[4]/a");

    public ITinvolveChangesElementPage selectFirstChangeElement() {
        TestCaseLogger.log("Select first change element.");
        //WebUtils.waitForElementPresent(firstelemLocator);
        List<WebElement> firstElementLinks = WebUtils.waitForListElementsPresent(firstelemLocator);//WebUtils.getWebElements(firstelemLocator);
        WebElement change = firstElementLinks.get(0);

        //wait_clickable_button (By.xpath(path),5);
        if (change.isDisplayed()) {
            //Assert.assertTrue("Non change Item Exits",change.isDisplayed());
            WebUtils.clickOn(change);
        }
        //WebUtils.waitForPageLoaded();
        return (ITinvolveChangesElementPage) HandlerFactory.getInstance(ITinvolveChangesElementPage.class);
    }
}
