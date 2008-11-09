package com.objogate.wl.web;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;

public final class ElementTextProbe extends ElementPropertyProbe {
    public ElementTextProbe(AsyncElementDriver elementDriver, Matcher<String> textMatcher) {
        super(elementDriver, "text", textMatcher);
    }
    
    @Override
    protected String propertyValue(WebElement e) {
        return e.getText();
    }
}
