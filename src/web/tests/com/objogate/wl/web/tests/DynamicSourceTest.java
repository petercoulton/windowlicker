package com.objogate.wl.web.tests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.objogate.exception.Defect;
import com.objogate.wl.Prober;
import com.objogate.wl.UnsynchronizedProber;
import com.objogate.wl.web.AsyncWebDriver;
import com.objogate.wl.web.RefreshingProber;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.hamcrest.Matchers.equalTo;

public class DynamicSourceTest {
    WebDriver webDriver = new FirefoxDriver();
    Prober prober = new UnsynchronizedProber();
    AsyncWebDriver driver = new AsyncWebDriver(new RefreshingProber(webDriver, prober), webDriver);

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Test
    public void refreshesThePageUntilContentsMatches() throws IOException {
        final File htmlFile = File.createTempFile(getClass().getSimpleName(), ".html");
        final String template = loadResource("dynamic-source.html");

        writeStringToFileAtomically(htmlFile, template.replace("REPLACE-ME", "Waiting"));

        driver.navigate().to(htmlFile.toURI().toURL());
        driver.element(By.id("dynamic")).assertText(equalTo("Waiting"));

        scheduler.schedule(new Runnable() {
            public void run() {
                writeStringToFileAtomically(htmlFile, template.replace("REPLACE-ME", "Done"));
            }
        }, 100, MILLISECONDS);

        driver.element(By.id("dynamic")).assertText(equalTo("Done"));
    }

    private void writeStringToFileAtomically(File htmlFile, String contents) {
        try {
            writeStringToFile(htmlFile, contents);
        } catch (IOException e) {
            throw new Defect("failed to write HTML file", e);
        }
    }

    private String loadResource(String name) throws IOException {
        InputStream in = getClass().getResourceAsStream(name);
        if (in == null) {
            throw new Defect("could not load resource " + name);
        }

        try {
            return IOUtils.toString(in);
        } finally {
            in.close();
        }
    }


    @After
    public void stopTimer() throws InterruptedException {
        scheduler.shutdown();
        scheduler.awaitTermination(1, TimeUnit.SECONDS);
    }

    @After
    public void closeBrowser() {
        driver.quit();
    }
}
