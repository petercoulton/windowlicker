package com.objogate.wl.web;

import org.openqa.selenium.WebDriver;

import com.objogate.wl.PollingProber;
import com.objogate.wl.Probe;

public class RefreshingProber extends PollingProber {
    private final WebDriver webDriver;

    public RefreshingProber(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
	protected void runProbe(Probe probe) {
    	webDriver.navigate().refresh();
    	probe.probe();
    }
}
