package com.objogate.wl.web.tests;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.openqa.selenium.By.id;

import org.junit.Test;

import com.objogate.wl.web.AsyncElementDriver;

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

    @Test
    public void clearingText() {
        openResource("text-entry.html");
        
        AsyncElementDriver input = browser.element(id("input"));
        AsyncElementDriver output = browser.element(id("reversed"));
        
        input.type("bananas\n");
        output.assertText(not(equalTo("")));
        
        input.clear();
        input.type("\n");
        output.assertText(equalTo(""));
    }
    
    @Test
    public void radioButtons() {
    	openResource("radio-buttons.html");
    	
    	AsyncElementDriver output = browser.element(id("output"));
        AsyncElementDriver bananas = browser.element(id("bananas"));
        AsyncElementDriver breadfruit = browser.element(id("breadfruit"));
        
        bananas.click();
        bananas.assertIsSelected();
        breadfruit.assertIsNotSelected();
        output.assertText(equalTo("Bananas"));
        
        breadfruit.click();
        breadfruit.assertIsSelected();
        bananas.assertIsNotSelected();
        output.assertText(equalTo("Bread Fruit"));

        bananas.click();
        bananas.assertIsSelected();
        breadfruit.assertIsNotSelected();
        output.assertText(equalTo("Bananas"));
    }
}
