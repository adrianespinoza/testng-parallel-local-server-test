package com.itinvolve.itsm.common.handlers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.itinvolve.itsm.change.handlers.ITinvolveChangesPage;
import com.itinvolve.itsm.change.handlers.ITinvolveComputerPage;
import com.itinvolve.itsm.change.handlers.ITinvolvePoliciesPage;
import com.itinvolve.itsm.change.handlers.IssueWinAlertCreateNewIncidentPopup;
import com.itinvolve.itsm.change.handlers.ITinvolveKnowledgePage;
import com.itinvolve.itsm.framework.logs.TestCaseLogger;
import com.itinvolve.itsm.framework.utils.HandlerFactory;
import com.itinvolve.itsm.framework.utils.WebUtils;

public class ITinvolveMainBarPageComponent {
    //private By browseallLocator = By.xpath("//a[contains(text(),'Browse All')]");
    private By browseallLocator = By.cssSelector("div[id='menubarContainer']>div>div>ul>li:nth-child(3)");
    //private By changesLocator = By.xpath("//li[3]/div/table/tbody/tr[2]/td/ul/li[2]/a[contains(text(),'Changes')]");
    private By changesLocator = By.cssSelector("li:nth-child(3)>div>table>tbody>tr>td>ul>li>a[href*='apex/ListChange']");
    //private By changesLocator = By.cssSelector("div[id='menubarContainer']>div>div>ul>li:nth-child(3)>div>table>tbody>tr>td>ul>li>a[href*='apex/ListChange']");
    private By createObjectsLocator = By.cssSelector("a[title='Create Objects']");
    private By createObjectsIFrameLocator = By.cssSelector("iframe[id*='j_id0:SiteTemplate:j_id9:newObjectWizard:popup:popup_iframeContainer']");
    public By logoutLocator = By.cssSelector("a[href='https://cs10.salesforce.com/secur/logout.jsp']");//By.linkText("Logout");
    public By settingsLocator = By.cssSelector("a[href='https://cs10.salesforce.com/ui/setup/Setup']");

    private By knowledgeLocator = By.cssSelector("li:nth-child(3)>div>table>tbody>tr>td>ul>li>a[href*='apex/ListKnowledge']");
    private By policiesLocator = By.cssSelector("li:nth-child(3)>div>table>tbody>tr>td>ul>li>a[href*='apex/ListPolicy']");
    private By computerLocator = By.xpath(".//*[@id='SiteTemplate:j_id0:menunav']/ul/li[3]/div/table/tbody/tr[5]/td/ul/li[10]/a");

    public ITinvolveChangesPage goToBrowseAllChangesPage() {
        TestCaseLogger.log("Go to BrowseAll Changes Page.");
        goToBrowseAll();
        WebUtils.waitForNSeconds(1);
        WebUtils.waitAndClickOn(changesLocator);
        //WebUtils.waitForPageLoaded();
        return (ITinvolveChangesPage) HandlerFactory.getInstance(ITinvolveChangesPage.class);
    }

    public ITinvolveKnowledgePage goToBrowseAllKnowledgePage() {
        TestCaseLogger.log("Go to BrowseAll Knowledge Page.");
        goToBrowseAll();
        WebUtils.waitForNSeconds(1);
        WebUtils.waitAndClickOn(knowledgeLocator);
        //WebUtils.waitForPageLoaded();
        return HandlerFactory.getInstance(ITinvolveKnowledgePage.class);
    }

    public ITinvolveComputerPage goToBrowseAllComputerPage() {
        TestCaseLogger.log("Go to BrowseAll Computers Page.");
        goToBrowseAll();
        WebUtils.waitForNSeconds(1);
        WebUtils.waitAndClickOn(computerLocator);
        //WebUtils.waitForPageLoaded();
        return HandlerFactory.getInstance(ITinvolveComputerPage.class);
    }

    public ITinvolvePoliciesPage goToBrowseAllPoliciesPage() {
        TestCaseLogger.log("Go to BrowseAll Knowledge Page.");
        goToBrowseAll();
        WebUtils.waitForNSeconds(1);
        WebUtils.waitAndClickOn(policiesLocator);
        //WebUtils.waitForPageLoaded();
        return HandlerFactory.getInstance(ITinvolvePoliciesPage.class);
    }

    public SalesforceLoginPage logout() {
        WebUtils.waitAndClickOn(logoutLocator);
        return (SalesforceLoginPage) HandlerFactory.getInstance(SalesforceLoginPage.class);
    }

    public ITinvolveCreateObjectsIFrame goToCreateObjectsIFrame() {
        WebUtils.waitAndClickOn(createObjectsLocator);
        return swithToCreateObjectsIframe();
    }

    public ITinvolveMainBarPageComponent goToBrowseAll() {
        WebUtils.waitForNSeconds(1);
        WebUtils.waitAndClickOn(browseallLocator);
        return this;
    }

    public void goToSettingsPage() {
        WebUtils.waitAndClickOn(settingsLocator);
    }

    /**iframes*/
    public ITinvolveCreateObjectsIFrame swithToCreateObjectsIframe() {
        WebUtils.switchToIFrame(createObjectsIFrameLocator);
        return (ITinvolveCreateObjectsIFrame) HandlerFactory.getInstance(ITinvolveCreateObjectsIFrame.class);
    }

    public IssueWinAlertCreateNewIncidentPopup goToITprocessInciednts() {
        By locatorTIprocess = By.xpath(".//*[@id='SiteTemplate:j_id0:j_id290']/ul/li/a/div");
        By locatorIncident = By.xpath(".//*[@id='SiteTemplate:j_id0:j_id290']/ul/li/div/table/tbody/tr[2]/td/ul/li[2]/a");
        By iframeLocator = By.xpath(".//*[@id='j_id0:SiteTemplate:j_id11:newObjectWizard:popup:popup_iframeContainer']");
        WebUtils.waitAndClickOn(locatorTIprocess);
        WebUtils.waitAndClickOn(locatorIncident);

        WebUtils.switchToIFrame(iframeLocator);

        return HandlerFactory.getInstance(IssueWinAlertCreateNewIncidentPopup.class);
    }
}
