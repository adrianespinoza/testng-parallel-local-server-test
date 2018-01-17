package com.itinvolve.itsm.change.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class IssueWinAlertCreateNewIncidentPopup {

    public IssueWinAlertChooseTemplatePage ApplyTemplate() {
        By locatorApplyTemplateButton = By.xpath(".//*[@id='applyTemplateButton']");
        WebUtils.clickAndSwitchToSecondWindow(locatorApplyTemplateButton);

        return HandlerFactory.getInstance(IssueWinAlertChooseTemplatePage.class);
    }

}
