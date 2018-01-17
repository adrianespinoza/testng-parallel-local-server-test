package com.itinvolve.itsm.change.testcases;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import com.itinvolve.itsm.change.handlers.ITinvolveChangesElementPage;
import com.itinvolve.itsm.change.handlers.ITinvolveChangesPage;
import com.itinvolve.itsm.common.handlers.ITinvolveAppPage;
import com.itinvolve.itsm.common.handlers.ITinvolveComputerWindowPage;
import com.itinvolve.itsm.common.handlers.ITinvolveCreateObjectsIFrame;
import com.itinvolve.itsm.common.handlers.ITinvolveMainBarPageComponent;
import com.itinvolve.itsm.common.handlers.SalesforceMainBarPageComponent;
import com.itinvolve.itsm.framework.sfusers.Profile;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class WorkingWithInternalFrame {
    @Test(testName="ITI-20473", groups = {"Win"}, description = "put test description here")
    public void goToIFrameTest() {
        SalesforceMainBarPageComponent sMainBarComp = HandlerFactory.getInstance(SalesforceMainBarPageComponent.class);

        sMainBarComp.switchUser(Profile.SYSADMIN);// return SalesforceHomePage by default

        ITinvolveAppPage itAppPage = sMainBarComp.goToITinvolveAppPage();
        itAppPage.goToITinvolveMainAppPage();// return ITinvolveHomePage by default

        ITinvolveMainBarPageComponent itMainBarComp = HandlerFactory.getInstance(ITinvolveMainBarPageComponent.class);
        ITinvolveCreateObjectsIFrame itCreateObjIFrame = itMainBarComp.goToCreateObjectsIFrame();//into iframe

//        List<String> res = itCreateObjIFrame.getObjectTypes();
//        System.out.println("----> Size:::: " + res.size());
        itCreateObjIFrame.setSelect();

        String oldWindow = itCreateObjIFrame.templateInstanceOpenLookupWindow();//go to ITinvolveComputerWindowPage

        ITinvolveComputerWindowPage itComputerWin = (ITinvolveComputerWindowPage) HandlerFactory.getInstance(ITinvolveComputerWindowPage.class);
        itComputerWin.cancelSelectComputer();
        WebUtils.switchToWindow(oldWindow);

        itCreateObjIFrame = itMainBarComp.swithToCreateObjectsIframe();
        itCreateObjIFrame.cancelCreateObjects();

        ITinvolveChangesPage itChangesPage = itMainBarComp.goToBrowseAllChangesPage();
        ITinvolveChangesElementPage itChangeElementPage = itChangesPage.selectFirstChangeElement();
    }

    @AfterClass
    public void tearDown2() {
        System.out.println("after class custom");
    }
}
