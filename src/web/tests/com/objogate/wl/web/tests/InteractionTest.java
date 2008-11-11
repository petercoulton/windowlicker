package com.objogate.wl.web.tests;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.openqa.selenium.By.id;

import org.junit.Test;

public class InteractionTest extends AbstractWebTest {
    @Test
    public void initiatingDynamicBehaviourWithUserAction() {
        openResource("button-click.html");
        
        browser.element(id("button")).click();
        browser.element(id("display")).assertText(containsString("Changed"));
    }
    
    @Test
    public void interactingWithDynamicContent() {
        openResource("dynamic-button.html");
        
        browser.element(id("buttonA")).click();
        browser.element(id("buttonB")).click();
        browser.element(id("display")).assertText(containsString("B"));
    }
    
    @Test
    public void enablingElements() {
        openResource("enable-disable-button.html");
        
        browser.element(id("button")).assertIsNotEnabled();
        browser.element(id("enable")).click();
        browser.element(id("button")).assertIsEnabled();
        browser.element(id("button")).click();
        browser.element(id("display")).assertText(containsString("Changed"));
    }
    
    @Test
    public void typingText() {
        openResource("text-entry.html");
        
        browser.element(id("input")).type("hello world\n");
        browser.element(id("reversed")).assertText(equalTo("dlrow olleh"));
    }
}
