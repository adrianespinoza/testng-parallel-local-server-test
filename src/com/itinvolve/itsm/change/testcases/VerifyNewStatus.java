package com.itinvolve.itsm.change.testcases;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.itinvolve.itsm.change.handlers.ITinvolveChangesElementPage;
import com.itinvolve.itsm.framework.annotations.BeforeTestMethod;
import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.utils.HandlerFactory;

public class VerifyNewStatus {
    @BeforeTestMethod(methods = "testVerifyNewStatus")
    public void test() {
        System.out.println("Fron Second test ");
    }

    @Test(testName="ITI-20473", groups = {"Changes"}, priority = 2, description = "put test description here")
    public void testVerifyNewStatus() throws Exception {
        ITinvolveChangesElementPage itcElementPage = (ITinvolveChangesElementPage) HandlerFactory.getInstance(ITinvolveChangesElementPage.class);
        String status = itcElementPage.getChangeElementStatus();
        TestCaseLogger.log("Verify change element status '" + status +"' as " + (("Old".equals(status)) ? "Success" : "Fail"));
        Assert.assertEquals(status, "Old");
    }
}
