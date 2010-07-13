package com.objogate.wl.web.tests;

import org.junit.Test;
import org.openqa.selenium.By;

import static org.hamcrest.Matchers.equalTo;

public class NestedElementsTest extends AbstractWebTest {
    @Test
    public void navigatingToNestedElements() throws Exception {
        openResource("nested-elements.html");

        browser.element(By.id("a")).element(By.className("button")).click();
        browser.element(By.id("display")).assertText(equalTo("Button A was pressed"));

        browser.element(By.id("b")).element(By.className("button")).click();
        browser.element(By.id("display")).assertText(equalTo("Button B was pressed"));
    }
}
