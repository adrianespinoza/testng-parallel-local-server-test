package com.itinvolve.itsm.framework.sfcomp;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.itinvolve.itsm.framework.logs.Log;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class DatepickerCmp {
    public void selectMonthByLabel(Month month) {
        By calMonthPickerLocator = By.cssSelector("div[id='datePicker']>*>select[id='calMonthPicker']");
        WebUtils.setSelectByVisibleText(calMonthPickerLocator, month.name());
    }

    public void selectYearByLabel(String year) {
        By calYearPickerLocator = By.cssSelector("div[id='datePicker']>*>select[id='calYearPicker']");
        List<String> years = WebUtils.getSelectLabels(calYearPickerLocator);
        if (years.contains(year)) {
            WebUtils.setSelectByVisibleText(calYearPickerLocator, year);
        } else {
            Log.logger().warn("-> The year picked only contains this values to select: " + years.toString() + " >>> Your value was [" + year + "]");
        }
    }

    public void selectYearByLabel(int year) {
        selectYearByLabel(String.valueOf(year));
    }

    public void clickOnNextMonthButton() {
        By nextMonthButtonLocator = By.cssSelector("div[id='datePicker']>*>img[class='calRight']");
        WebUtils.waitAndClickOn(nextMonthButtonLocator);
    }

    public void clickOnPreviousMonthButton() {
        By nextMonthButtonLocator = By.cssSelector("div[id='datePicker']>*>img[class='calLeft']");
        WebUtils.waitAndClickOn(nextMonthButtonLocator);
    }

    public void clickOnTodayLink() {
        By todayLinkLocator = By.cssSelector("div[id='datePicker']>*>div[class='buttonBar']>a[class='calToday']");
        WebUtils.waitAndClickOn(todayLinkLocator);
    }

    public void clickOnDay(int day) {
        String classLogical = "(@class='weekday' or " +
                "@class='weekend' or " +
                "@class='weekday todayDate' or " +
                "@class='weekend todayDate' or " +
                "@class='weekday selectedDate' or " +
                "@class='weekend selectedDate') and " +
                "text()='" + day + "'";
        By dayLocator = By.xpath("//*/table[@id='datePickerCalendar']/*/tr/td[" + classLogical + "]");
        WebUtils.waitAndClickOn(dayLocator);
    }

    public String getSelectedMonth() {
        By calMonthPickerLocator = By.cssSelector("div[id='datePicker']>*>select[id='calMonthPicker']");
        return WebUtils.getFirstSelectedLabel(calMonthPickerLocator);
    }

    public String getSelectedYear() {
        By calYearPickerLocator = By.cssSelector("div[id='datePicker']>*>select[id='calYearPicker']");
        return WebUtils.getFirstSelectedLabel(calYearPickerLocator);
    }

    public String getSelectedDay() {
        By dayLocator = By.xpath("//*/table[@id='datePickerCalendar']/*/tr/td[@class='weekday selectedDate' or @class='weekend selectedDate']");
        WebElement element = WebUtils.waitForElement(dayLocator);
        String selectedDay = element.getText();
        return selectedDay;
    }
}
