package com.objogate.wl.driver.tests;

import javax.swing.text.JTextComponent;
import java.awt.Font;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.driver.JTextComponentDriver;

public abstract class AbstractJTextComponentDriverTest<U extends JTextComponentDriver<? extends JTextComponent>> extends AbstractComponentDriverTest<U> {
    public void setText(final String text) {
        driver.perform("setting text", new ComponentManipulation<JTextComponent>() {
            public void manipulate(JTextComponent component) {
                component.setText(text);
            }
        });
    }

    public void setFont(final Font font) {
        driver.perform("setting text", new ComponentManipulation<JTextComponent>() {
            public void manipulate(JTextComponent component) {
                component.setFont(font);
            }
        });
    }

    protected JTextComponentDriver.TextOccurence occurence(int count) {
        return JTextComponentDriver.occurence(count);
    }
}
