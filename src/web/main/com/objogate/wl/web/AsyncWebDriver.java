package com.objogate.wl.web;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;

import com.objogate.wl.Probe;
import com.objogate.wl.Prober;

public class AsyncWebDriver {
    private final Prober prober;
    private final WebDriver webDriver;
    
    public AsyncWebDriver(Prober prober, WebDriver webDriver) {
        this.prober = prober;
        this.webDriver = webDriver;
    }
    
    public Navigation navigate() {
        return webDriver.navigate();
    }
    
    public AsyncElementDriver element(By criteria) {
        return new AsyncElementDriver(prober, webDriver, criteria);
    }
    
    public void quit() {
        webDriver.quit();
    }

    public void assertTitle(final Matcher<String> titleMatcher) {
        prober.check(new Probe() {
            private String actualTitle;
            
            public void probe() {
                actualTitle = webDriver.getTitle();
            }

            public boolean isSatisfied() {
                return titleMatcher.matches(actualTitle);
            }

            public void describeTo(Description description) {
                description.appendText("title is ").appendDescriptionOf(titleMatcher);
            }
            
            public boolean describeFailureTo(Description description) {
                description.appendText("title was ").appendValue(actualTitle);
                return true;
            }
        });
    }
}
