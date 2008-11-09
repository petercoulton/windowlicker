package com.objogate.wl.swing.driver.tests;

import static com.objogate.wl.probe.ComponentIdentity.selectorFor;

import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.swing.driver.JRadioButtonDriver;

public class JRadioButtonDriverTest extends AbstractButtonDriverTest<JRadioButton, JRadioButtonDriver> {
    private JRadioButtonDriver otherDriver;

    @Before
    public void createButtonAndDriver() throws Exception {
        button = new JRadioButton("button 1");

        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton otherButton = new JRadioButton("button 2");
        buttonGroup.add(otherButton);
        buttonGroup.add(button);

        JPanel jPanel = new JPanel(new GridLayout(1, 2));
        jPanel.add(button);
        jPanel.add(otherButton);

        view(jPanel);

        driver = new JRadioButtonDriver(gesturePerformer, selectorFor(button), prober);
        otherDriver = new JRadioButtonDriver(gesturePerformer, selectorFor(otherButton), prober);
    }

    @Test
    public void testCheckingButtonInGroupUnchecksOthers() {
        otherDriver.click();
        driver.click();

        driver.isChecked();
        otherDriver.isNotChecked();
    }
}
