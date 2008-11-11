package com.objogate.wl.web.tests;

import java.net.URL;

import org.junit.After;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;

public abstract class AbstractWebTest {
    protected AsyncWebDriver browser = new AsyncWebDriver(new UnsynchronizedProber(), new FirefoxDriver());

    public AbstractWebTest() {
        super();
    }

    protected void openResource(String htmlResourceName) {
        URL resourceURL = getClass().getResource(htmlResourceName);
        if (resourceURL == null) {
            throw new IllegalArgumentException("could not find resource named " + htmlResourceName);
        }
        
        browser.navigate().to(resourceURL.toExternalForm());
    }

    @After
    public void quitBrowser() {
        browser.quit();
    }

}