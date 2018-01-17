package com.itinvolve.itsm.framework.sfcomp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class TextareaCmp extends InlineEditDialog {
    public void typeTextarea(String value) {
        TestCaseLogger.log("Type in Description Field: " + value);
        By descTextareaLocator = By.cssSelector("div#InlineEditDialog.overlayDialog.inlineEditDialog textarea");
        WebElement weTextarea = WebUtils.waitForElement(descTextareaLocator);
        weTextarea.clear();
        weTextarea.sendKeys(value);
    }
}
