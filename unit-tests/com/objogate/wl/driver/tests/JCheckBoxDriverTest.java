package com.objogate.wl.driver.tests;

import static com.objogate.wl.probe.ComponentIdentity.selectorFor;

import javax.swing.JCheckBox;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.driver.JCheckBoxDriver;

public class JCheckBoxDriverTest extends AbstractButtonDriverTest<JCheckBox, JCheckBoxDriver> {

    @Before
    public void createButtonAndDriver() throws Exception {
        button = new JCheckBox("a check box");

        view(button);

        driver = new JCheckBoxDriver(gesturePerformer, selectorFor(button), prober);
    }

    @Test
    public void testChecking() {
        driver.click();

        driver.isChecked();

        driver.click();

        try {
            prober.setTimeout(100);
            driver.isChecked();
            Assert.fail("should not be checked");
        } catch (AssertionError expected) {
        }
    }

    @Test
    public void testUnchecking() {
        driver.click();
        driver.click();

        driver.isNotChecked();
    }
}
