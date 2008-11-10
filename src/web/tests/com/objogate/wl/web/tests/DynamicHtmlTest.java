package com.objogate.wl.web.tests;

import static org.hamcrest.Matchers.containsString;

import org.junit.Test;
import org.openqa.selenium.By;


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
    public void dynamicInputValue() {
        openResource("async-input-value.html");
        
        browser.element(By.id("x")).assertValue(containsString("Changed"));
    }
}
