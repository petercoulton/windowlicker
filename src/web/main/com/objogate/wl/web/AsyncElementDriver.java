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
        prober.check(new ElementPropertyProbe(this, "text", textMatcher) {
            @Override
            protected String propertyValue(WebElement e) {
                return e.getText();
            }
        });
    }

    public void assertValue(final Matcher<String> valueMatcher) {
        prober.check(new ElementPropertyProbe(this, "value", valueMatcher) {
            @Override
            protected String propertyValue(WebElement e) {
                return e.getValue();
            }
        });
    }

    public void assertIsEnabled() {
        assertEnabledFlagIs(true);
    }

    public void assertIsNotEnabled() {
        assertEnabledFlagIs(false);
    }

    private void assertEnabledFlagIs(final boolean expectedFlagValue) {
        prober.check(new ElementFlagProbe(this, "enabled", expectedFlagValue) {
            @Override
            protected boolean flagValue(WebElement e) {
                return e.isEnabled();
            }
        });
    }

    WebElement findElement() {
        return webDriver.findElement(criteria);
    }

    public void click() {
        prober.check(new ElementProbe(this) {
            @Override
            protected void probe(WebElement element) {
                element.click();
            }
        });
    }

    public void type(final String string) {
        prober.check(new ElementProbe(this) {
            @Override
            protected void probe(WebElement element) {
                element.sendKeys(string);
            }
        });
    }

    public void clear() {
        prober.check(new ElementProbe(this) {
            @Override
            protected void probe(WebElement element) {
                element.clear();
            }
        });
    }

    public void describeTo(Description description) {
        description.appendText("an element ").appendText(criteria.toString());
    }

}
