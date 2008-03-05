package com.objogate.wl.probe.tests;

import static com.objogate.wl.matcher.ComponentMatchers.named;
import static com.objogate.wl.matcher.ComponentMatchers.showingOnScreen;
import static com.objogate.wl.matcher.ComponentMatchers.withLabelText;
import static com.objogate.wl.matcher.StringContainsInOrderMatcher.containsInOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.junit.Test;

import com.objogate.wl.ComponentSelector;
import com.objogate.wl.driver.ComponentDriver;
import com.objogate.wl.driver.JFrameDriver;
import com.objogate.wl.driver.JLabelDriver;
import com.objogate.wl.driver.tests.AbstractComponentDriverTest;
import com.objogate.wl.matcher.ComponentMatchers;

public class ComponentFinderErrorMessagesTest extends AbstractComponentDriverTest<ComponentDriver<? extends Component>> {
    @SuppressWarnings("unchecked")
    @Test 
    public void
    reportsWhenCannotFindTopLevelFrame() {
        prober.setTimeout(100);
        
        try {
            ComponentSelector<JFrame> selector = JFrameDriver.topLevelFrame(ComponentMatchers.named("--no-such-frame--"));
            prober.check("testing error messages", selector);
        }
        catch (AssertionError e) {
            assertThat(e.getMessage(), containsInOrder(
                "testing error messages",
                "Tried to look for",
                "exactly 1", "JFrame", "named \"--no-such-frame--\"",
                "in all top level windows",
                "but",
                "all top level windows",
                "contained 0 JFrame", "named \"--no-such-frame--\""));
        }
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    reportsWhenCannotFindNestedComponent() {
        JPanel panel = new JPanel();
        panel.setName("panel");
        JLabel label = new JLabel("hello world");
        label.setName("label");
        panel.add(label);
        view(panel);
        
        prober.setTimeout(100);
        
        try {
            JLabelDriver labelDriver = new JLabelDriver(frameDriver, named("label"), withLabelText(equalTo("HELLO")));
            
            labelDriver.is(showingOnScreen());
        }
        catch (AssertionError e) {
            assertThat(e.getMessage(), containsInOrder(
                "Tried to look for",
                "exactly 1", "JLabel", "named \"label\"", "with text \"HELLO\"",
                "in exactly 1 JFrame", "named \"componentViewer\"",
                "in all top level windows",
                "and check that it is showing on screen",
                "but",
                "all top level windows",
                "contained 1 JFrame", "named \"componentViewer\"",
                "contained 0 JLabel", "named \"label\"", "with text \"HELLO\""));
        }
    }
    
        @SuppressWarnings("unchecked")
    @Test public void
    reportsWhenComponentAssertionFails() {
        JPanel panel = new JPanel();
        panel.setName("panel");
        JLabel label = new JLabel("hello world");
        label.setName("label");
        label.setBackground(Color.WHITE);
        panel.add(label);
        view(panel);
        
        prober.setTimeout(100);
        
        try {
            JLabelDriver labelDriver = new JLabelDriver(frameDriver, named("label"));
            
            labelDriver.hasBackgroundColor(Color.BLACK);
        }
        catch (AssertionError e) {
            assertThat(e.getMessage(), containsInOrder(
                "Tried to look for",
                "exactly 1", "JLabel", "named \"label\"",
                "in exactly 1 JFrame", "named \"componentViewer\"",
                "in all top level windows",
                "and check", "background color", "<java.awt.Color[r=0,g=0,b=0]>",
                "but",
                "all top level windows",
                "contained 1 JFrame", "named \"componentViewer\"",
                "contained 1 JLabel", "named \"label\"",
                "background color was", "<java.awt.Color[r=255,g=255,b=255]>"));
        }
    }

}
