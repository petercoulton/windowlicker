package com.objogate.wl.web;

import org.hamcrest.Description;
import org.openqa.selenium.WebDriver;
import com.objogate.wl.Probe;
import com.objogate.wl.Prober;

/**
 * A Prober that wraps another Prober to refresh WebDriver every time it performs a probe.
 *
 * This is useful for testing read-only web pages, the contents of which are updated asynchronously
 * w.r.t. the test.
 */
public class RefreshingProber implements Prober {
    private final WebDriver webDriver;
    private final Prober prober;

    public RefreshingProber(WebDriver webDriver, Prober prober) {
        this.webDriver = webDriver;
        this.prober = prober;
    }

    public void check(final Probe realProbe) {
        prober.check(new Probe() {
            public void probe() {
                webDriver.navigate().refresh();
                realProbe.probe();
            }

            public boolean isSatisfied() {
                return realProbe.isSatisfied();
            }

            public void describeTo(Description description) {
                realProbe.describeTo(description);
            }

            public void describeFailureTo(Description description) {
                realProbe.describeFailureTo(description);
            }
        });
    }
}
