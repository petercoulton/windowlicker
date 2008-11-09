package com.objogate.wl.web;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.objogate.wl.Probe;
import com.objogate.wl.Prober;

public class AsyncElementDriver implements SelfDescribing {
    private final Prober prober;
    private final WebDriver webDriver;
    private final By criteria;
    
    public AsyncElementDriver(Prober prober, WebDriver webDriver, By criteria) {
        this.prober = prober;
        this.webDriver = webDriver;
        this.criteria = criteria;
    }

    public void assertText(final Matcher<String> textMatcher) {
        prober.check(new Probe() {
            private WebElement element;
            private String actualText;
            
            public void probe() {
                element = null;
                actualText = null;
                
                try {
                    element = webDriver.findElement(criteria);
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
                    .appendDescriptionOf(AsyncElementDriver.this)
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
        });
    }
    
    public void describeTo(Description description) {
        description.appendText("an element ").appendText(criteria.toString());
    }
}
