package com.objogate.wl.swing.probe.tests;

import static com.objogate.wl.swing.matcher.StringContainsInOrderMatcher.containsInOrder;
import static org.junit.Assert.assertThat;

import java.awt.Component;

import javax.swing.JPanel;

import org.junit.Test;

import com.objogate.wl.swing.driver.ComponentDriver;
import com.objogate.wl.swing.driver.JComponentDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.driver.tests.AbstractComponentDriverTest;
import com.objogate.wl.swing.probe.NthComponentFinder;
import com.objogate.wl.swing.probe.RecursiveComponentFinder;

public class NthComponentFinderTest extends AbstractComponentDriverTest<ComponentDriver<? extends Component>> {
    @Test public void
    reportsWhenThereAreNoMatchingComponents() {
        JPanel panel = new JPanel();
        panel.setName("panel");
        view(panel);
        
        prober.setTimeout(100);
        try {
            NthComponentFinder<JPanel> finder = new NthComponentFinder<JPanel>(
                    new RecursiveComponentFinder<JPanel>(JPanel.class,
                            ComponentDriver.named("--no-such-panel--"), frameDriver.component()), 0);
            
            JComponentDriver<JPanel> panelDriver = new JComponentDriver<JPanel>(frameDriver, finder);
            JLabelDriver labelDriver = new JLabelDriver(panelDriver);
            
            labelDriver.is(ComponentDriver.showingOnScreen());
        }
        catch (AssertionError e) {
            assertThat(e.getMessage(), containsInOrder(
                "Tried to look for",
                "exactly 1", "JLabel",
                "in the <0> component from those matching JPanel", "with name \"--no-such-panel--\"",
                "in all top level windows",
                "and check that it is showing on screen",
                "but",
                "all top level windows",
                "contained 1 JFrame", "with name \"componentViewer\"",
                "contained 0 JPanel", "with name \"--no-such-panel--\""));
        }
    }
}
