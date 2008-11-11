package com.objogate.wl.web;

import org.hamcrest.Description;
import org.openqa.selenium.WebElement;

public abstract class ElementFlagProbe extends ElementProbe {
    private final String flagName;
    private final boolean expectedFlagValue;
    
    private boolean actualFlagValue;
    
    public ElementFlagProbe(AsyncElementDriver driver, String flagName, boolean expectedFlagValue) {
        super(driver);
        this.flagName = flagName;
        this.expectedFlagValue = expectedFlagValue;
        this.actualFlagValue = !expectedFlagValue;
    }
    
    @Override
    protected void probe(WebElement element) {
        actualFlagValue = flagValue(element);
    }
    
    @Override
    public boolean isSatisfied() {
        return super.isSatisfied()
            && actualFlagValue == expectedFlagValue;
    }
    
    @Override
    public void describeTo(Description description) {
        super.describeTo(description);
        description.appendText(" that ")
                   .appendText(expectedFlagValue ? "is" : "is not")
                   .appendText(" ")
                   .appendText(flagName);
    }
    
    @Override
    public boolean describeFailureTo(Description description) {
        if (super.describeFailureTo(description)) return true;
        
        description.appendText("element ")
                   .appendText(actualFlagValue ? "was" : "was not")
                   .appendText(" ")
                   .appendText(flagName);
        return true;
    }

    protected abstract boolean flagValue(WebElement element);
}
