package com.objogate.wl.web.tests;

import static org.hamcrest.Matchers.containsString;
import org.junit.Test;

public class NavigationTest extends AbstractWebTest {
    @Test
    public void canNavigateBackAndForth() {
        openResource("text-entry.html");
        browser.assertTitle(containsString("Text Entry"));

        openResource("async-title.html");
        browser.assertTitle(containsString("Changed"));

        browser.navigate().back();
        browser.assertTitle(containsString("Text Entry"));

        browser.navigate().forward();
        browser.assertTitle(containsString("Changed"));
    }
}
