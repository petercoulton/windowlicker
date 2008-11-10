package com.objogate.wl.web;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
        prober.check(new ElementTextProbe(this, textMatcher));
    }
    
    public void assertValue(final Matcher<String> valueMatcher) {
        prober.check(new ElementValueProbe(this, valueMatcher));
    }
    
    WebElement findElement() {
        return webDriver.findElement(criteria);
    }
    
    public void describeTo(Description description) {
        description.appendText("an element ").appendText(criteria.toString());
    }
    
    public void click() {
        prober.check(new ElementActionProbe(this) {
            @Override
            protected void action(WebElement element) {
                element.click();
            }
        });
    }
}
