package com.objogate.wl.web.tests;

import static org.hamcrest.Matchers.containsString;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;

public class DynamicHtmlTest {
    AsyncWebDriver browser = new AsyncWebDriver(new UnsynchronizedProber(), new FirefoxDriver());
        
    @Test 
    public void asynchronousText() {
        openResource("async-text.html");
        
        browser.element(By.id("x")).assertText(containsString("Changed"));
    }

    @Test 
    public void asynchronousTitle() {
        openResource("async-title.html");
        
        browser.assertTitle(containsString("Changed"));
    }

    @Test 
    public void dynamicElement() {
        openResource("async-element.html");
        
        browser.element(By.id("x")).assertText(containsString("Created"));
    }
    
    @Test
    public void dynamicInputValue() {
        openResource("async-input-value.html");
        
        browser.element(By.id("x")).assertValue(containsString("Changed"));
    }

    private void openResource(String htmlResource) {
        browser.navigate().to(getClass().getResource(htmlResource).toExternalForm());
    }
    
    @After
    public void quitBrowser() {
        browser.quit();
    }
}
