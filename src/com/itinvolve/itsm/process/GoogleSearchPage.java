package com.itinvolve.itsm.process;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class GoogleSearchPage {
    // Here's the element by default is  "id" or "name" [q] without annotation
    // The element is now looked up using the name attribute
    @FindBy(how = How.NAME, using = "q")
    private WebElement q;

    public void searchFor(String text) {
        // And here we use it. Note that it looks like we've
        // not properly instantiated it yet....
        q.sendKeys(text);
        q.submit();
    }

}
