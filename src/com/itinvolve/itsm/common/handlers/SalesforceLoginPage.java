package com.itinvolve.itsm.common.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.sfusers.Profile;
import com.itinvolve.itsm.framework.sfusers.User;
import com.itinvolve.itsm.framework.sfusers.UserHandler;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class SalesforceLoginPage {
    private By usernameLocator = By.cssSelector("input[id='username']");//By.id("username");
    private By passwordLocator = By.cssSelector("input[id='password']");//By.id("password");
    private By loginButtonLocator = By.cssSelector("button[id='Login']");//By.id("Login");

    public SalesforceHomePage loginAs(String username, String password) {
        //WebUtils.waitForPageLoaded();
        //WebUtils.waitForElementPresent(usernameLocator);
        //WebUtils.waitForElementPresent(passwordLocator);
        typeUsername(username);
        typePassword(password);

        return submitLogin();
    }

    public SalesforceHomePage loginAs(Profile userProfile, WebDriver driver) {
        User user = UserHandler.setupCurrentUser(userProfile);
        return loginAs(user.name, user.password);
    }

    public SalesforceLoginPage typeUsername(String username) {
        WebElement weUsername = WebUtils.waitForElement(usernameLocator);//WebUtils.getWebElement(usernameLocator);
        weUsername.clear();
        Log.logger().info("Enteirng user name...");
        weUsername.sendKeys(username);
        return this;
    }

    public SalesforceLoginPage typePassword(String password) {
        WebElement wePassword = WebUtils.waitForElement(passwordLocator);//WebUtils.getWebElement(passwordLocator);
        wePassword.clear();
        Log.logger().info("Enteirng password...");
        wePassword.sendKeys(password);
        return this;
    }

    public SalesforceHomePage submitLogin() {
        WebElement element = WebUtils.waitForElement(loginButtonLocator);
        if (element != null) {
            Log.logger().info("Clicking on login button...");
            element.submit();
        }
        return (SalesforceHomePage) HandlerFactory.getInstance(SalesforceHomePage.class);
    }

    public SalesforceLoginPage submitLoginExpectingFailure() {
        WebElement element = WebUtils.waitForElement(loginButtonLocator);
        if (element != null) {
            Log.logger().info("Clicking on login button...");
            element.submit();
        }
        return this;
    }
}
