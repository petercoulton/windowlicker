package com.objogate.wl.driver.tests;

import static com.objogate.wl.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.equalTo;

import javax.swing.JProgressBar;

import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.driver.JProgressBarDriver;

public class JProgressBarDriverTest extends AbstractComponentDriverTest<JProgressBarDriver>{

    private JProgressBar bar;

    @Test
    public void testCanReadTheCurrentValueOfTheComponent() {

        bar.setMinimum(0);
        bar.setMaximum(100);
        bar.setValue(50);

        driver.hasValue(equalTo(50));
    }

    @Before
    public void setUp() {

        bar = new JProgressBar();

        view(bar);

        driver = new JProgressBarDriver(gesturePerformer, selectorFor(bar), prober);
    }
}
