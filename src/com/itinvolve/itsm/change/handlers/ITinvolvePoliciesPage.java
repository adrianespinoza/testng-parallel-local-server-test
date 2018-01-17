package com.itinvolve.itsm.change.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.sfcomp.DatepickerCmp;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class ITinvolvePoliciesPage {
    public void selectFirtsPolicies() {
        By policiesLocator = By.xpath(".//*[@id='j_id0:SiteTemplate:j_id10:j_id75:customList:tableContainer:blockTable:0:j_id156:0:j_id164']");
        WebUtils.waitAndClickOn(policiesLocator);
    }

    public DatepickerCmp clickOnEffectiveFrom() {
        //Category
        return doubleClickFieldByLabel("Effective From", DatepickerCmp.class);
    }

    public <T extends Object> T doubleClickFieldByLabel(String label, Class<? extends Object> expectedClass) {
        TestCaseLogger.log("Double click " + label + " field");
        By labelFieldLocator = By.xpath("//th[contains(., '" + label + "')]/../td//span[@class='inlineEditWrite']");
        WebUtils.dobleClickOn(WebUtils.waitForElement(labelFieldLocator));

        return HandlerFactory.getInstance(expectedClass);
    }
}
