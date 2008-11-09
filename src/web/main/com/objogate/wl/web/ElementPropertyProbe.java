package com.objogate.wl.web;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.objogate.wl.Probe;

public abstract class ElementPropertyProbe implements Probe {
    private final AsyncElementDriver elementDriver;
    private final String propertyName;
    private final Matcher<String> valueMatcher;
    
    private WebElement element;
    private String actualValue;
    
    protected ElementPropertyProbe(AsyncElementDriver elementDriver, String propertyName, Matcher<String> textMatcher) {
        this.elementDriver = elementDriver;
        this.propertyName = propertyName;
        this.valueMatcher = textMatcher;
    }
    
    public void probe() {
        element = null;
        actualValue = null;
        
        try {
            element = elementDriver.findElement();
            actualValue = propertyValue(element);
        }
        catch (NoSuchElementException e) {
            // try next time
        }
    }
    
    protected abstract String propertyValue(WebElement e);
    
    public boolean isSatisfied() {
        return element != null && valueMatcher.matches(actualValue);
    }
    
    public void describeTo(Description description) {
        description
            .appendDescriptionOf(elementDriver)
            .appendText(" with ")
            .appendText(propertyName)
            .appendText(" ")
            .appendDescriptionOf(valueMatcher);
    }
    
    public boolean describeFailureTo(Description description) {
        if (element == null) {
            description.appendText("did not find matching element");
            return true;
        }
        else {
            description.appendText("text was ").appendValue(actualValue);
            return true;
        }
    }
}