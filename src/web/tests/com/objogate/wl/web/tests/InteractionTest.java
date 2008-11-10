package com.objogate.wl.web.tests;

import static org.hamcrest.Matchers.containsString;
import static org.openqa.selenium.By.id;

import org.junit.Test;

public class InteractionTest extends AbstractWebTest {
    @Test
    public void initiatingDynamicBehaviourWithUserAction() {
        openResource("button-click.html");
        
        browser.element(id("button")).click();
        browser.element(id("display")).assertText(containsString("Changed"));
    }
}
