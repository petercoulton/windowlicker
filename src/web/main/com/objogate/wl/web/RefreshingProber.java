package com.objogate.wl.web;

import org.openqa.selenium.WebDriver;
import com.objogate.wl.Prober;
import com.objogate.wl.Probe;

public class RefreshingProber implements Prober {
    private final WebDriver webDriver;
    private final Prober prober;

    public RefreshingProber(WebDriver webDriver, Prober prober) {
        this.webDriver = webDriver;
        this.prober = prober;
    }
    
    public void check(Probe probe) {
        prober.check(new RefreshingProbe(webDriver, probe));
    }
}
