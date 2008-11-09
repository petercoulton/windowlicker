package com.objogate.wl.web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;

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
}
