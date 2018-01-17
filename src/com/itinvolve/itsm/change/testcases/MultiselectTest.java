package com.itinvolve.itsm.change.testcases;

import org.testng.annotations.Test;

import com.itinvolve.itsm.change.handlers.ITinvolveKnowledgePage;
import com.itinvolve.itsm.common.handlers.ITinvolveAppPage;
import com.itinvolve.itsm.common.handlers.ITinvolveMainBarPageComponent;
import com.itinvolve.itsm.common.handlers.SalesforceMainBarPageComponent;
import com.itinvolve.itsm.framework.sfcomp.MultiSelectPicklistCmp;
import com.itinvolve.itsm.framework.sfusers.Profile;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class MultiselectTest {
    @Test(testName="ITI-20473", groups = {"Win"}, description = "put test description here")
    public void goToIFrameTest() {
        SalesforceMainBarPageComponent sMainBarComp = HandlerFactory.getInstance(SalesforceMainBarPageComponent.class);
        sMainBarComp.switchUser(Profile.SYSADMIN);// return SalesforceHomePage by default


        ITinvolveAppPage itAppPage = sMainBarComp.goToITinvolveAppPage();
        itAppPage.goToITinvolveMainAppPage();// return ITinvolveHomePage by default

        ITinvolveMainBarPageComponent itMainBarComp = (ITinvolveMainBarPageComponent) HandlerFactory.getInstance(ITinvolveMainBarPageComponent.class);
        ITinvolveKnowledgePage itChangesPage = itMainBarComp.goToBrowseAllKnowledgePage();
        itChangesPage.selectFirtsKnowledge();
        MultiSelectPicklistCmp mspl = itChangesPage.clickOnCategory();
        mspl.selectLeftByLabel("Standard", "Best Practice");
//        mspl.selectLeftByLabel("Standard");
        mspl.clickArrowRightButton();
        System.out.println(">>> " + mspl.getTitle());
        mspl.selectLeftAll();
        WebUtils.waitForNSeconds(5);
        mspl.deselectLeftAll();
        WebUtils.waitForNSeconds(5);
        mspl.clickCancelButton(ITinvolveKnowledgePage.class);
        WebUtils.waitForNSeconds(5);
    }
}
