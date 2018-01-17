package com.itinvolve.itsm.change.testcases;

import org.testng.annotations.Test;

import com.itinvolve.itsm.common.handlers.ITinvolveAppPage;
import com.itinvolve.itsm.common.handlers.SalesforceMainBarPageComponent;
import com.itinvolve.itsm.framework.sfusers.Profile;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class TakeScreenshotWebElement {
    @Test(testName="ITI-20473", groups = {"Win"}, description = "put test description here")
    public void goToIFrameTest() {
        SalesforceMainBarPageComponent sMainBarComp = HandlerFactory.getInstance(SalesforceMainBarPageComponent.class);
        sMainBarComp.switchUser(Profile.SYSADMIN);// return SalesforceHomePage by default


        ITinvolveAppPage itAppPage = sMainBarComp.goToITinvolveAppPage();
        itAppPage.goToITinvolveMainAppPage();// return ITinvolveHomePage by default

        WebUtils.executeJavaScript("alert('Hello world from java script.')");
        WebUtils.waitForNSeconds(10);

//        ITinvolveMainBarPageComponent itMainBarComp = HandlerFactory.getInstance(ITinvolveMainBarPageComponent.class);
//        ITinvolveComputerPage itChangesPage = itMainBarComp.goToBrowseAllComputerPage();
//
//        itChangesPage.seletcComputer();
//        itChangesPage.selectRelationship();
//
//        //By locator = By.cssSelector("table[class^='rich-tabpanel-content-position']>tbody>tr>td[class^='rich-tabpanel-content sectiontabs_content']>*>div>div>div>div>div[id^='component']");
//        //By locator = By.cssSelector("table[class^='rich-tabpanel-content-position']>tbody>tr>td[class^='rich-tabpanel-content sectiontabs_content']>span div[id^='component']");
//        //By locator = By.cssSelector("div.perspectiveEditor.graphContainer div[id*='component'] > svg");
//        By locator = By.cssSelector("div.perspectiveEditor.graphContainer div[id*='component']");
//
//        SvgImage image1 = HtmlScanner.scanSvg(locator);
//        //image1.info();
//        SvgImage image2 = HtmlScanner.scanSvg(locator);
//        boolean res = image1.equals(image2);
//        System.out.println("Are equals: " + res);
//
//


//        String path1 = WebUtils.shootWebElement(locator, "image1");
//        String path2 = WebUtils.shootWebElement(locator, "image2");
//        boolean withoutChanges = ImageComparer.areEquals(path1, path2);
//        Assert.assertEquals(withoutChanges, true);
    }
}
