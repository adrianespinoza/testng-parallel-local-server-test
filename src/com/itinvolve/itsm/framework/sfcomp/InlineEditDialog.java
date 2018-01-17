package com.itinvolve.itsm.framework.sfcomp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public abstract class InlineEditDialog {
    protected By titleLocator = By.cssSelector("div[class='topLeft']>h2[id='InlineEditDialogTitle']");

    public void closePopup() {
        By xLocator = By.cssSelector("div[class='topLeft']>a[id='InlineEditDialogX']");
        WebUtils.waitAndClickOn(xLocator);
    }

    public <T extends Object> T clickOkButton(Class<? extends Object> expectedClass) {
        By buttonOkLocator = By.cssSelector("div[id='InlineEditDialogContent']>div[id='InlineEditDialog_buttons']>input[value='OK']");
        WebUtils.waitAndClickOn(buttonOkLocator);
        return HandlerFactory.getInstance(expectedClass);
    }

    public <T extends Object> T clickCancelButton(Class<? extends Object> expectedClass) {
        By buttonCancelLocator = By.cssSelector("div[id='InlineEditDialogContent']>div[id='InlineEditDialog_buttons']>input[value='Cancel']");
        WebUtils.waitAndClickOn(buttonCancelLocator);
        return HandlerFactory.getInstance(expectedClass);
    }

    public String getTitle() {
        titleLocator = By.cssSelector("div[class='topLeft']>h2[id='InlineEditDialogTitle']");
        WebElement element = WebUtils.waitForElement(titleLocator);
        return element.getText();
    }
}
