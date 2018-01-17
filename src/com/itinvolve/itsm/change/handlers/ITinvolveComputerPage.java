package com.itinvolve.itsm.change.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.itinvolve.itsm.framework.utils.WebUtils;

public class ITinvolveComputerPage {
    public void seletcComputer() {
        By computerLocator = By.xpath(".//*[@id='j_id0:SiteTemplate:j_id10:j_id75:customList:tableContainer:blockTable:0:j_id156:0:j_id164']");
        WebUtils.waitAndClickOn(computerLocator);
    }

    public void selectRelationship() {
        By relocator = By.xpath(".//*[@id='j_id0:SiteTemplate:j_id11:j_id144:j_id145:j_id151:2:j_id156']");
        WebUtils.waitAndClickOn(relocator);
    }
}
