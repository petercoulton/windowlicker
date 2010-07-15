package com.objogate.wl.web.tests;

import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.Matchers.containsString;


public class DynamicHtmlTest extends AbstractWebTest {
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
    public void dynamicElementExists() {
        openResource("async-element.html");

        browser.element(By.id("x")).assertExists();
    }

    @Test
    public void dynamicElementIsDeleted() {
        openResource("async-delete-element.html");
        browser.element(By.id("deleteme")).assertExists();
        browser.element(By.id("deleteme")).assertDoesNotExist();
    }

    @Test
    public void dynamicInputValue() {
        openResource("async-input-value.html");
        
        browser.element(By.id("x")).assertValue(containsString("Changed"));
    }
}
