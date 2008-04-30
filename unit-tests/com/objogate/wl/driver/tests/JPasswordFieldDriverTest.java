package com.objogate.wl.driver.tests;

import static com.objogate.wl.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.equalTo;

import javax.swing.JPasswordField;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.objogate.exception.Defect;
import com.objogate.wl.driver.JPasswordFieldDriver;

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
            driver.text(equalTo("password"));
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

    @Test
    public void testCannotSelectText() {
        setText("my passsword is secret");

        try {
            driver.selectText(occurence(1).of("secret"));

            Assert.fail("should not be able query the text of a password field");
        } catch (Defect expected) {

        }
    }

    @Test
    public void testReplaceTextUsingTextOccurence() {
        setText("my passsword is secret");

        try {
            driver.replaceText(occurence(1).of("secret"), "should not be allowed");

            Assert.fail("should not be able query the text of a password field");
        } catch (Defect expected) {

        }
    }
}
