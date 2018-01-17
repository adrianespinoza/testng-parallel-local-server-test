package com.itinvolve.itsm.common.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.env.Setup;
import com.itinvolve.itsm.framework.env.Setup.Page;
import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.sfusers.Profile;
import com.itinvolve.itsm.framework.sfusers.User;
import com.itinvolve.itsm.framework.sfusers.UserHandler;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class SalesforceMainBarPageComponent {
    private By userNavLabelLocator = By.cssSelector("span[id='userNavLabel']");//By.id("userNavLabel");
    public By logoutLocator = By.cssSelector("a[title='Logout'][class='menuButtonMenuLink']");//By.linkText("Logout");
    private By tsidLabelLocator = By.cssSelector("span[id='tsidLabel']");//By.id("tsidLabel");
    private By tsidButtonLocator = By.cssSelector("div[id='tsidButton']");//By.id("tsidButton");
    private By itAppLabelLocator = By.xpath("//a[contains(text(),'ITinvolve')]");

    public SalesforceLoginPage logout() {
        WebElement userNavElement = WebUtils.waitForElement(userNavLabelLocator);
        if (userNavElement != null) {
            if (!WebUtils.isElementPresent(logoutLocator)) {
                userNavElement.click();
            }
            WebElement logoutElement = WebUtils.waitForElement(logoutLocator);
            if (logoutElement != null) {
                logoutElement.click();
            }
        }
        return (SalesforceLoginPage) HandlerFactory.getInstance(SalesforceLoginPage.class);
    }

    public ITinvolveAppPage goToITinvolveAppPage() {
        TestCaseLogger.log("Go to ITinvolve App Page.");
        //WebUtils.waitForElementPresent(tsidLabelLocator);

        WebElement labelElement = WebUtils.waitForElement(tsidLabelLocator);

        String menuSelected = labelElement == null ? null : labelElement.getText();
        if (menuSelected != null && !menuSelected.contains("ITinvolve") ) {
            WebElement tsidElement = WebUtils.waitForElement(tsidButtonLocator);
            if (tsidElement != null) {
                tsidElement.click();

                WebElement appElement = WebUtils.waitForElement(itAppLabelLocator);
                if (appElement != null) {
                    appElement.click();
                }
            }
            //WebUtils.waitForPageLoaded();
        }
        return (ITinvolveAppPage) HandlerFactory.getInstance(ITinvolveAppPage.class);
    }

    public SalesforceHomePage switchUser(Profile userProfile) {
        String page = getPageNameToLogout();
        if (UserHandler.getCurrentUser() == null || page.equals(Page.LOGIN.name())) {
            loginWithProfile(userProfile);
        } else {
            if (UserHandler.currentIsTheSameProfile(userProfile)){
                if (page.equals(Page.ITINVOLVE.name())) {
                    ((ITinvolveMainBarPageComponent) HandlerFactory.getInstance(ITinvolveMainBarPageComponent.class)).goToSettingsPage();
                } else if (page.equals(Page.SALESFORCE.name())) {
                    //foo
                } else {
                    //last case when page logout not found
                }
            } else { //different profile
                boolean logoutWasSuccessful = logoutFromPage(page);
                if (logoutWasSuccessful) {
                    loginWithProfile(userProfile);
                }
            }
        }
        return (SalesforceHomePage) HandlerFactory.getInstance(SalesforceHomePage.class);
    }
    private boolean logoutFromPage(String page) {
        boolean logoutWasSuccessful = false;
        if (!page.isEmpty()) {
            if (page.equals(Page.ITINVOLVE.name())) {
                ((ITinvolveMainBarPageComponent) HandlerFactory.getInstance(ITinvolveMainBarPageComponent.class)).logout();
                logoutWasSuccessful = true;
            } else if (page.equals(Page.SALESFORCE.name())) {
                ((SalesforceMainBarPageComponent) HandlerFactory.getInstance(SalesforceMainBarPageComponent.class)).logout();//return a SalesforceLoginPage
                logoutWasSuccessful = true;
            } else {
                Log.logger().error("Error occurred when trying to logout with " + page + " page");
            }
        } else {
            Log.logger().error("Error occurred when trying to logout");
        }
        return logoutWasSuccessful;
    }
    private SalesforceMainBarPageComponent loginWithProfile(Profile userProfile) {
        User user = UserHandler.setupCurrentUser(userProfile);
        ((SalesforceLoginPage) HandlerFactory.getInstance(SalesforceLoginPage.class)).loginAs(user.name, user.password);//return a SalesforceHomePage
        return this;
    }
    private String getPageNameToLogout() {//pages [salesforce,itinvolve]
        String page = "";
        ITinvolveMainBarPageComponent itMainBarComponent = (ITinvolveMainBarPageComponent) HandlerFactory.getInstance(ITinvolveMainBarPageComponent.class);
        boolean isElemPresent = WebUtils.isElementPresent(itMainBarComponent.logoutLocator);
        if (isElemPresent) {
            page = Page.ITINVOLVE.name();
        } else {
            SalesforceMainBarPageComponent sMainBarComponent = (SalesforceMainBarPageComponent) HandlerFactory.getInstance(SalesforceMainBarPageComponent.class);
            isElemPresent = WebUtils.isElementPresent(sMainBarComponent.logoutLocator);
            if (isElemPresent) {
                page = Page.SALESFORCE.name();
            } else {
                String salesforceUrl = Setup.START_URL;
                String currentUrl = WebUtils.getCurrentUrl();
                if (currentUrl.equals(salesforceUrl)) {
                    page = Page.LOGIN.name();
                }
            }
        }
        return page;
    }
}
