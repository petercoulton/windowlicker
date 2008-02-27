package com.objogate.wl.driver.tests;

import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.driver.AbstractButtonDriver;
import com.objogate.wl.probe.ActionListenerProbe;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import org.junit.Test;

import javax.swing.AbstractButton;

public abstract class AbstractButtonDriverTest<B extends AbstractButton, U extends AbstractButtonDriver<B>> extends AbstractComponentDriverTest<U> {
    protected B button;

    @Test
    public void assertsButtonText() {
        String text = button.getClass().getSimpleName();
        setButtonText(text);

        driver.text(equalTo(text));

        try {
            prober.setTimeout(100);
            driver.text(equalTo("bar"));
        }
        catch (AssertionError e) {
            return; // expected
        }

        fail("text assertion should have failed");
    }

    @Test
    @Problematic(
            why="Doesn't work when run as multiple tests, as layout manager changes button position after we start moving",
            platform = Platform.Linux)
    public void clicksButtonWithTheMouse() {
        ActionListenerProbe probe = new ActionListenerProbe();
        button.addActionListener(probe);

        driver.click();

        prober.check("should have been clicked", probe);
    }

    private void setButtonText(final String text) {
        driver.perform("setting text", new ComponentManipulation<AbstractButton>() {
            public void manipulate(AbstractButton b) {
                b.setText(text);
            }
        });
    }
}
