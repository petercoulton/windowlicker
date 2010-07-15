package com.objogate.wl.web;

import org.hamcrest.Description;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.StaleElementReferenceException;
import com.objogate.wl.Probe;

public class MissingElementProbe implements Probe {
    private final AsyncElementDriver driver;
    private boolean isMissing = false;

    public MissingElementProbe(AsyncElementDriver driver) {
        this.driver = driver;
    }

    public void probe() {
        try {
            driver.findElement();
        }
        catch (NotFoundException e) {
            isMissing = true;
        }
        catch (StaleElementReferenceException e) {
            isMissing = true;
        }
    }

    public boolean isSatisfied() {
        return isMissing;
    }

    public void describeTo(Description description) {
        description.appendDescriptionOf(driver)
                   .appendText(" that did not exist");
    }

    public void describeFailureTo(Description description) {
        description.appendText("the element did exist");
    }
}