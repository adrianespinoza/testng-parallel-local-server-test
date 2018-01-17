package com.itinvolve.itsm.change.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.itinvolve.itsm.framework.utils.WebUtils;

public class IssueWinAlertChooseTemplatePage {

    public void chooseTemplate() {
        By templateLocator = By.xpath(".//*[@id='j_id0:lookupForm:j_id8:j_id48:0:j_id49:0:j_id50']");
        WebUtils.waitAndClickOn(templateLocator);
    }
}
