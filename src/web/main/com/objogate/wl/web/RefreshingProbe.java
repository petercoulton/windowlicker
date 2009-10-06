package com.objogate.wl.web;

import org.hamcrest.Description;
import org.openqa.selenium.WebDriver;
import com.objogate.wl.Probe;

public class RefreshingProbe implements Probe {
    private final WebDriver webDriver;
    private final Probe probe;

    public RefreshingProbe(WebDriver webDriver, Probe probe) {
        this.webDriver = webDriver;
        this.probe = probe;
    }
    
    public void probe() {
        webDriver.navigate().refresh();
        probe.probe();
    }

    public boolean isSatisfied() {
        return probe.isSatisfied();
    }

    public void describeTo(Description description) {
        probe.describeTo(description);
    }

    public boolean describeFailureTo(Description description) {
        return probe.describeFailureTo(description);
    }
}

