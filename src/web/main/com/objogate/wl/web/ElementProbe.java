package com.objogate.wl.web;

import org.hamcrest.Description;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import com.objogate.wl.Probe;

public abstract class ElementProbe implements Probe {
    private final AsyncElementDriver driver;

    private WebElement foundElement = null;

    public ElementProbe(AsyncElementDriver driver) {
        this.driver = driver;
    }

    public void probe() {
        foundElement = null;
        try {
            foundElement = driver.findElement();
            probe(foundElement);
        }
        catch (NoSuchElementException e) {
            // try next time
        }
        catch (StaleElementReferenceException e) {
            // try next time
        }
    }

    public boolean isSatisfied() {
        return foundElement != null;
    }

    public void describeTo(Description description) {
        description.appendDescriptionOf(driver);
    }

    public void describeFailureTo(Description description) {
        if (foundElement == null) {
            description.appendText("did not find matching element");
        }
    }

    protected abstract void probe(WebElement element);
}
