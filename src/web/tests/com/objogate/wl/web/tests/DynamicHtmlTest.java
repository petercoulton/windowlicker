package com.objogate.wl.web.tests;

import static org.hamcrest.Matchers.containsString;

import java.net.URL;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;

public class DynamicHtmlTest {
    AsyncWebDriver browser = new AsyncWebDriver(new UnsynchronizedProber(), new FirefoxDriver());
        
    @Test 
    public void canAssertStateOfAsynchronouslyLoadedElements() {
        URL asyncPage = getClass().getResource("async.html");
        
        browser.navigate().to(asyncPage.toExternalForm());
        
        browser.element(By.id("x")).assertText(containsString("Changed"));
    }
    
    @After
    public void quitBrowser() {
        browser.quit();
    }
}
