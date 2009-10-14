package com.objogate.wl.web;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;


public abstract class ElementPropertyProbe extends ElementProbe {
    private final String propertyName;
    private final Matcher<String> valueMatcher;

    private String actualValue;

    protected ElementPropertyProbe(AsyncElementDriver elementDriver, String propertyName, Matcher<String> textMatcher) {
        super(elementDriver);
        this.propertyName = propertyName;
        this.valueMatcher = textMatcher;
    }

    @Override
    protected void probe(WebElement element) {
        actualValue = propertyValue(element);
    }

    protected abstract String propertyValue(WebElement e);

    @Override
    public boolean isSatisfied() {
        return super.isSatisfied()
                && valueMatcher.matches(actualValue);
    }

    @Override
    public void describeTo(Description description) {
        super.describeTo(description);
        description.appendText(" with ")
                .appendText(propertyName)
                .appendText(" ")
                .appendDescriptionOf(valueMatcher);
    }

    @Override
    public void describeFailureTo(Description description) {
        super.describeFailureTo(description);
        if (super.isSatisfied()) {
            description.appendText(propertyName)
                    .appendText(" was ")
                    .appendValue(actualValue);
        }
    }
}