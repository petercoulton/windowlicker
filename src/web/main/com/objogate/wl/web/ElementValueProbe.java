package com.objogate.wl.web;

import org.hamcrest.Matcher;
import org.openqa.selenium.WebElement;

public final class ElementValueProbe extends ElementPropertyProbe {
    public ElementValueProbe(AsyncElementDriver elementDriver, Matcher<String> matcher) {
        super(elementDriver, "value", matcher);
    }
    
    @Override
    protected String propertyValue(WebElement e) {
        return e.getValue();
    }
}
