package com.objogate.wl.web;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.objogate.wl.Probe;

public final class ElementTextProbe implements Probe {
    private final AsyncElementDriver elementDriver;
    private final Matcher<String> textMatcher;
    
    private WebElement element;
    private String actualText;
    
    public ElementTextProbe(AsyncElementDriver elementDriver, Matcher<String> textMatcher) {
        this.elementDriver = elementDriver;
        this.textMatcher = textMatcher;
    }
    
    public void probe() {
        element = null;
        actualText = null;
        
        try {
            element = elementDriver.findElement();
            actualText = element.getText();
        }
        catch (NoSuchElementException e) {
            // try next time
        }
    }

    public boolean isSatisfied() {
        return element != null && textMatcher.matches(actualText);
    }

    public void describeTo(Description description) {
        description
            .appendDescriptionOf(elementDriver)
            .appendText(" with text ")
            .appendDescriptionOf(textMatcher);
    }

    public boolean describeFailureTo(Description description) {
        if (element == null) {
            description.appendText("did not find matching element");
            return true;
        }
        else {
            description.appendText("text was ").appendValue(actualText);
            return true;
        }
    }
}