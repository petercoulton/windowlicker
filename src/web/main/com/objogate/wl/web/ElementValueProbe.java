package com.objogate.wl.web;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.objogate.wl.Probe;

public final class ElementValueProbe implements Probe {
    private final AsyncElementDriver elementDriver;
    private final Matcher<String> matcher;
    
    private WebElement element;
    private String actualValue;
    
    public ElementValueProbe(AsyncElementDriver elementDriver, Matcher<String> matcher) {
        this.elementDriver = elementDriver;
        this.matcher = matcher;
    }
    
    public void probe() {
        element = null;
        actualValue = null;
        
        try {
            element = elementDriver.findElement();
            actualValue = element.getValue();
        }
        catch (NoSuchElementException e) {
            // try next time
        }
    }
    
    public boolean isSatisfied() {
        return element != null && matcher.matches(actualValue);
    }

    public void describeTo(Description description) {
        description
            .appendDescriptionOf(elementDriver)
            .appendText(" with value ")
            .appendDescriptionOf(matcher);
    }

    public boolean describeFailureTo(Description description) {
        if (element == null) {
            description.appendText("did not find matching element");
            return true;
        }
        else {
            description.appendText("value was ").appendValue(actualValue);
            return true;
        }
    }
}
