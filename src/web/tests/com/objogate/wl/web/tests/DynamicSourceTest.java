package com.objogate.wl.web.tests;

import java.io.File;
import java.io.IOException;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.junit.Test;
import org.junit.Before;
import com.objogate.wl.Prober;
import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.RefreshingProber;
import com.objogate.wl.web.AsyncWebDriver;

public class DynamicSourceTest {
    WebDriver webDriver = new FirefoxDriver();
    Prober prober = new RefreshingProber(webDriver, new UnsynchronizedProber());
    AsyncWebDriver driver = new AsyncWebDriver(prober, webDriver);

    @Before
    public void startCreatingTmp() throws IOException {
        File tmpFile = File.createTempFile(getClass().getSimpleName(), ".tmp");
        File committedFile = File.createTempFile(getClass().getSimpleName(), ".html");
    }
    
    @Test
    @Ignore("not finished yet")
    public void refreshesThePageUntilContentsMatches() {
        fail("not finished yet");
    }
}
