package com.itinvolve.itsm.change.testcases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.ListUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.itinvolve.itsm.change.handlers.ITinvolveChangesElementPage;
import com.itinvolve.itsm.change.handlers.ITinvolveChangesPage;
import com.itinvolve.itsm.common.handlers.ITinvolveAppPage;
import com.itinvolve.itsm.common.handlers.ITinvolveMainBarPageComponent;
import com.itinvolve.itsm.common.handlers.SalesforceMainBarPageComponent;
import com.itinvolve.itsm.framework.annotations.AfterTestMethod;
import com.itinvolve.itsm.framework.annotations.BeforeTestMethod;
import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.sfusers.Profile;
import com.itinvolve.itsm.framework.utils.HandlerFactory;

public class VerifyBasicSectionExist {
    @BeforeTestMethod(methods = "testVerifyBasicSectionExist")
    public void testVerifyBasicSectionExistStart1() {
        System.out.println("This call was customized before test method testVerifyBasicSectionExist, AWESOME!");
    }

    @AfterTestMethod(methods = "testVerifyBasicSectionExist")
    public void testVerifyBasicSectionExistEnd() {
        System.out.println("This call was customized after test method testVerifyBasicSectionExist, AWESOME!");
    }

    @Test(testName="ITI-20473", groups = {"Changes"}, priority = 1, description = "put test description here")
    public void testVerifyBasicSectionExist() {
        SalesforceMainBarPageComponent sMainBarComp = (SalesforceMainBarPageComponent) HandlerFactory.getInstance(SalesforceMainBarPageComponent.class);

        sMainBarComp.switchUser(Profile.SYSADMIN);//its convenient return SalesforceHomePage

        ITinvolveAppPage itAppPage = sMainBarComp.goToITinvolveAppPage();
        itAppPage.goToITinvolveMainAppPage();//return ITinvolveHomePage

        ITinvolveMainBarPageComponent itMainBarComp = (ITinvolveMainBarPageComponent) HandlerFactory.getInstance(ITinvolveMainBarPageComponent.class);
        ITinvolveChangesPage itChangesPage = itMainBarComp.goToBrowseAllChangesPage();
        ITinvolveChangesElementPage itChangeElementPage = itChangesPage.selectFirstChangeElement();

        List<String> listExp_sections = new ArrayList<String>();
        listExp_sections.add("Details");
        listExp_sections.add("Description");
        listExp_sections.add("Additional Email Notification Recipients");
        listExp_sections.add("Plan Details");
        listExp_sections.add("Tags");
        listExp_sections.add("Attachments");

        List<String> labelTextSections = itChangeElementPage.getLabelTextSections();

        Collections.sort(labelTextSections);
        Collections.sort(listExp_sections);

        boolean containsAllSections = ListUtils.isEqualList(listExp_sections, labelTextSections);//compareListValues(listExp_sections, labelTextSections);//itChangeElementPage.reviewSections(listExp_sections);
        TestCaseLogger.log("Verify basic section exist as " + (containsAllSections ? "Success" : "Fail"));
        Assert.assertEquals(containsAllSections, false);
    }
    /*@BeforeTestMethod(method = "testVerifyNewStatus")
    public void test() {
        System.out.println("Fron Second test ");
    }

    @Test(groups = {"Changes"}, priority = 2)
    public void testVerifyNewStatus() throws Exception {
        String status = ITinvolveChangesElementPage.Instance().getChangeElementStatus();
        Assert.assertEquals(status, "Old");
    }*/
}
