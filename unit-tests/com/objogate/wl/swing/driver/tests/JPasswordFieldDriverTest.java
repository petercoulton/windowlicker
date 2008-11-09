package com.objogate.wl.swing.driver.tests;

import static com.objogate.wl.swing.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.equalTo;

import javax.swing.JPasswordField;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.objogate.exception.Defect;
import com.objogate.wl.swing.driver.JPasswordFieldDriver;

public class JPasswordFieldDriverTest extends AbstractJTextComponentDriverTest<JPasswordFieldDriver> {

    @Before
    public void setUp() throws Exception {
        JPasswordField passwordField = new JPasswordField();
        passwordField.setColumns(30);

        view(passwordField);

        driver = new JPasswordFieldDriver(gesturePerformer, selectorFor(passwordField), prober);
    }

    @Test
    public void testCannotGetText() {
        setText("password");

        try {
            driver.hasText(equalTo("password"));
            Assert.fail("should not be able query the text of a password field");
        } catch (Defect expected) {
        }

        try {
            driver.hasText(equalTo("password"));

            Assert.fail("should not be able query the text of a password field");
        } catch (Defect expected) {
        }

        try {
            driver.hasText("password");

            Assert.fail("should not be able query the text of a password field");
        } catch (Defect expected) {
        }
    }
}
