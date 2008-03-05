package com.objogate.wl.probe.tests;

import static com.objogate.wl.matcher.ComponentMatchers.named;
import static com.objogate.wl.matcher.ComponentMatchers.showingOnScreen;
import static com.objogate.wl.matcher.ComponentMatchers.withLabelText;
import static com.objogate.wl.matcher.StringContainsInOrderMatcher.containsInOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

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
//    @Test 
    public void
    reportsWhenCannotFindTopLevelFrame() {
        prober.setTimeout(100);
        
        try {
            ComponentSelector<JFrame> selector = JFrameDriver.topLevelFrame(ComponentMatchers.named("--no-such-frame--"));
            prober.check("testing error messages", selector);
        }
        catch (AssertionError e) {
            String message = e.getMessage();
            assertThat(message, containsInOrder(
                "testing error messages",
                "Looked for",
                "exactly 1",
                "JFrame",
                "is named \"--no-such-frame--\"",
                "in all top level windows",
                "but found 0"));
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
            String message = e.getMessage();
            System.out.println(message);
            assertThat(message, containsInOrder(
                "TESTING"));
        }
    }
}
