package com.itinvolve.itsm.framework.sfcomp;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.utils.WebUtils;

public class MultiSelectPicklistCmp extends InlineEditDialog {
    private By multiselectLeftLocator = By.cssSelector("div[id='InlineEditDialogContent']>div[class='activeField']>div>table>tbody>tr>td:nth-child(1)>span>select");
    private By multiselectRightLocator = By.cssSelector("div[id='InlineEditDialogContent']>div[class='activeField']>div>table>tbody>tr>td:nth-child(3)>span>select");

    public void selectLeftAll() {
        WebUtils.setMultiSelectAll(multiselectLeftLocator);
    }

    public void selectRightAll() {
        WebUtils.setMultiSelectAll(multiselectRightLocator);
    }

    public void deselectLeftAll() {
        WebUtils.setMultiDeselectAll(multiselectLeftLocator);
    }

    public void deselectRightAll() {
        WebUtils.setMultiDeselectAll(multiselectRightLocator);
    }

    public void selectLeftByLabel(String... label) {
        WebUtils.setSelectByVisibleText(multiselectLeftLocator, label);
    }

    public void selectRightByLabel(String... label) {
        WebUtils.setSelectByVisibleText(multiselectRightLocator, label);
    }

    public void clickArrowRightButton() {
        By ArrowRightlocator = By.cssSelector("div[id='InlineEditDialogContent']>div[class='activeField']>div>table>tbody>tr>td:nth-child(2)>a>img[class='picklistArrowRight']");
        WebUtils.waitAndClickOn(ArrowRightlocator);
    }

    public void clickArrowLeftButton() {
        By ArrowRightlocator = By.cssSelector("div[id='InlineEditDialogContent']>div[class='activeField']>div>table>tbody>tr>td:nth-child(2)>a>img[class='picklistArrowLeft']");
        WebUtils.waitAndClickOn(ArrowRightlocator);
    }

    public void dobleClickLeftSelectLabel(String label) {
        WebElement element = WebUtils.getSelectElementByLabel(multiselectLeftLocator, label);
        if (element != null) {
            element.click();
            clickArrowRightButton();
        }
    }

    public void dobleClickRightSelectLabel(String label) {
        WebElement element = WebUtils.getSelectElementByLabel(multiselectRightLocator, label);
        if (element != null) {
            element.click();
            clickArrowLeftButton();
        }
    }

    public List<String> getLeftSelectLabels() {
        return WebUtils.getSelectLabels(multiselectLeftLocator);
    }

    public List<String> getRightSelectLabels() {

        return WebUtils.getSelectLabels(multiselectRightLocator);
    }
}
