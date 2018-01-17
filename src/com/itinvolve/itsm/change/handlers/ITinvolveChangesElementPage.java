package com.itinvolve.itsm.change.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class ITinvolveChangesElementPage {
    private By basicElementsLocator = By.cssSelector("div[class^='pbSubheader'] > h3");//By.xpath(".//*/h3");
    private By statusFieldLocator = By.xpath("//*/span[contains(text(),'New')]");

    /** This method look for basic tab sub sections  on change item**/
    public List<String> getLabelTextSections() {
        //PageUtils.waitForPageLoaded();
        Log.logger().info("Looking for label texts");

        try {
            List<WebElement> list = WebUtils.waitForListElementsPresent(basicElementsLocator);//.getWebElements(basicElementsLocator);
            List<String> listvalues = new ArrayList<String>();
            String label;
            for (WebElement i : list) {
                label = (i.getText()).trim();
                if (!label.isEmpty()) {
                    listvalues.add(i.getText());
                }
            }
            return listvalues;
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            System.out.println("problems to find  elements");
            Log.logger().error("Problems to find  elements", e);
            return null;
        }
    }

    public String getChangeElementStatus() {
        TestCaseLogger.log("Get change element status");
        WebElement Field = WebUtils.waitForElement(statusFieldLocator);//WebUtils.getWebElement(statusFieldLocator); // this section looks for Status   field

        if (Field.isDisplayed()) {
            String elem = Field.getText();
            System.out.println("Status: " + elem);
            return elem;
        } else {
            return "";
        }
    }
}
