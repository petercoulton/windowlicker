package com.objogate.wl.driver.tests;

import static com.objogate.wl.driver.tests.NamedColor.color;
import static com.objogate.wl.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.driver.JLabelDriver;

public class JLabelDriverTest extends AbstractComponentDriverTest<JLabelDriver> {
    JLabel label = new JLabel("label");

    @Before
    public void createButtonAndDriver() throws Exception {
        label.setPreferredSize(new Dimension(300, label.getPreferredSize().height));

        view(label);

        driver = new JLabelDriver(gesturePerformer, selectorFor(label), prober);
    }

    @Test
    public void assertsLabelText() {
        setLabelText("foo");

        driver.hasText(equalTo("foo"));

        try {
            prober.setTimeout(100);
            driver.hasText(equalTo("bar"));
        }
        catch (AssertionError e) {
            return; // expected
        }

        Assert.fail("text assertion should have failed");
    }

    @Test
    public void assertLabelColor() {
        setLabelText("green foreground, pink background");
        setOpaque(true);
        setColors(color("GREEN"), color("PINK"));

        driver.hasForegroundColor(color("GREEN"));
        driver.hasBackgroundColor(color("PINK"));
    }

    @Test
    public void assertThatHasBackgroundColorFailsIfLabelIsNotOpaque() {
        setLabelText("background should not be pink");
        setOpaque(false);
        setColors(color("GREEN"), color("PINK"));

        try {
            prober.setTimeout(100);
            driver.hasBackgroundColor(color("PINK"));
        } catch (AssertionError expected) {
        }
    }

    protected void setOpaque(final boolean flag) {
        driver.perform("setting colors", new ComponentManipulation<JComponent>() {
            public void manipulate(JComponent component) {
                component.setOpaque(flag);//just makes the background visible
            }
        });
    }

    private void setLabelText(final String text) {
        driver.perform("setting text", new ComponentManipulation<JLabel>() {
            public void manipulate(JLabel l) {
                l.setText(text);
            }
        });
    }
}
