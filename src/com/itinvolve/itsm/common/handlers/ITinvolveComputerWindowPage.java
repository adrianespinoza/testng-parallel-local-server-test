package com.itinvolve.itsm.common.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.itinvolve.itsm.framework.utils.WebUtils;

public class ITinvolveComputerWindowPage {
    private By cancelBtnLocator = By.cssSelector("input[id='cancelBtn']");

    public void cancelSelectComputer() {
        WebUtils.waitAndClickOn(cancelBtnLocator);
    }
}
