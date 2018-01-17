package com.itinvolve.itsm.change.testcases;

import org.testng.annotations.Test;

import com.itinvolve.itsm.change.handlers.IssueWinAlertChooseTemplatePage;
import com.itinvolve.itsm.change.handlers.IssueWinAlertCreateNewIncidentPopup;
import com.itinvolve.itsm.common.handlers.ITinvolveAppPage;
import com.itinvolve.itsm.common.handlers.ITinvolveMainBarPageComponent;
import com.itinvolve.itsm.common.handlers.SalesforceMainBarPageComponent;
import com.itinvolve.itsm.framework.sfusers.Profile;
import com.itinvolve.itsm.framework.utils.HandlerFactory;

public class IssueWinAlert {
    @Test(testName="ITI-20473", groups = {"Alert"}, description = "put test description here")
    public void issueWinAlert() {
        SalesforceMainBarPageComponent sMainBarComp = HandlerFactory.getInstance(SalesforceMainBarPageComponent.class);
        sMainBarComp.switchUser(Profile.SYSADMIN);// return SalesforceHomePage by default

        ITinvolveAppPage itAppPage = sMainBarComp.goToITinvolveAppPage();
        itAppPage.goToITinvolveMainAppPage();// return ITinvolveHomePage by default

        ITinvolveMainBarPageComponent itMainBarComp = HandlerFactory.getInstance(ITinvolveMainBarPageComponent.class);
        IssueWinAlertCreateNewIncidentPopup createNewIncidentP = itMainBarComp.goToITprocessInciednts();
        IssueWinAlertChooseTemplatePage choosePage = createNewIncidentP.ApplyTemplate();
        choosePage.chooseTemplate();
    }
}
