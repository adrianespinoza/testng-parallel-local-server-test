package com.itinvolve.itsm.change.testcases;

import org.testng.annotations.Test;

import com.itinvolve.itsm.change.handlers.ITinvolvePoliciesPage;
import com.itinvolve.itsm.common.handlers.ITinvolveAppPage;
import com.itinvolve.itsm.common.handlers.ITinvolveMainBarPageComponent;
import com.itinvolve.itsm.common.handlers.SalesforceMainBarPageComponent;
import com.itinvolve.itsm.framework.sfcomp.DatepickerCmp;
import com.itinvolve.itsm.framework.sfcomp.Month;
import com.itinvolve.itsm.framework.sfusers.Profile;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class DatepickerTest {
    @Test(testName="ITI-20473", groups = {"Win"}, description = "put test description here")
    public void goToIFrameTest() {
        SalesforceMainBarPageComponent sMainBarComp = HandlerFactory.getInstance(SalesforceMainBarPageComponent.class);
        sMainBarComp.switchUser(Profile.SYSADMIN);// return SalesforceHomePage by default


        ITinvolveAppPage itAppPage = sMainBarComp.goToITinvolveAppPage();
        itAppPage.goToITinvolveMainAppPage();// return ITinvolveHomePage by default

        ITinvolveMainBarPageComponent itMainBarComp = HandlerFactory.getInstance(ITinvolveMainBarPageComponent.class);
        ITinvolvePoliciesPage itChangesPage = itMainBarComp.goToBrowseAllPoliciesPage();
        itChangesPage.selectFirtsPolicies();
        DatepickerCmp dateP = itChangesPage.clickOnEffectiveFrom();
        dateP.selectMonthByLabel(Month.September);
        dateP.selectYearByLabel(2014);
//        dateP.clickOnNextMonthButton();
//        WebUtils.waitForNSeconds(5);
//        dateP.clickOnTodayLink();
        WebUtils.waitForNSeconds(5);
        dateP.clickOnDay(8);
        WebUtils.waitForNSeconds(5);
        itChangesPage.clickOnEffectiveFrom();

        System.out.println(dateP.getSelectedMonth());
        System.out.println(dateP.getSelectedYear());
        System.out.println(dateP.getSelectedDay());
        WebUtils.waitForNSeconds(5);
    }
}
