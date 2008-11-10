package com.objogate.wl.web.tests;

import org.junit.After;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;

public abstract class AbstractWebTest {
    protected AsyncWebDriver browser = new AsyncWebDriver(new UnsynchronizedProber(), new FirefoxDriver());

    public AbstractWebTest() {
        super();
    }

    protected void openResource(String htmlResource) {
        browser.navigate().to(getClass().getResource(htmlResource).toExternalForm());
    }

    @After
    public void quitBrowser() {
        browser.quit();
    }

}