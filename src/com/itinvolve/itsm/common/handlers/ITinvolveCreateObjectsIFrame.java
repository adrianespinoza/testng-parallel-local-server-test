package com.itinvolve.itsm.common.handlers;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.itinvolve.itsm.framework.utils.WebUtils;

public class ITinvolveCreateObjectsIFrame {
    private By cancelBtnLocator = By.cssSelector("input[id='cancelButton']");
    private By lookupWindowLocator = By.cssSelector("div[title='Open Lookup Window']");
    private By selectLocator = By.id("j_id0:j_id10:newObjectPageBlock:pickTypeSection:pickTypeItem:selectObjectType");

    public void cancelCreateObjects() {
        WebUtils.waitAndClickOn(cancelBtnLocator);
        WebUtils.switchToDefaultContent();
    }

    public List<String> getObjectTypes() {
        List<String> ot = WebUtils.getSelectLabels(selectLocator);
        return ot;
    }

    public void setSelect() {
        WebUtils.setSelectByVisibleText(selectLocator, "Floor");
//        List<String> values = WebUtils.getSelectValues(selectLocator);
//        for (String value : values) {
//            System.out.println("----> value: " + value);
//        }
    }

    public String templateInstanceOpenLookupWindow() {
        //String oldWindow = WebUtils.clickAndPopupWindow(lookupWindowLocator);
        String oldWindow = WebUtils.clickAndSwitchToSecondWindow(lookupWindowLocator);
        return oldWindow;
    }

    public void typeTemplateInstance() {

    }
}
