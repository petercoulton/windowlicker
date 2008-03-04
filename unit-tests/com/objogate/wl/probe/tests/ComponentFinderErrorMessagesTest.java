package com.objogate.wl.probe.tests;

import static org.junit.Assert.assertThat;

import java.awt.Component;

import javax.swing.JFrame;

import org.junit.Test;

import com.objogate.wl.ComponentSelector;
import com.objogate.wl.driver.ComponentDriver;
import com.objogate.wl.driver.JFrameDriver;
import com.objogate.wl.driver.tests.AbstractComponentDriverTest;
import com.objogate.wl.matcher.ComponentMatchers;
import com.objogate.wl.matcher.StringContainsInOrderMatcher;

public class ComponentFinderErrorMessagesTest extends AbstractComponentDriverTest<ComponentDriver<? extends Component>> {
    @SuppressWarnings("unchecked")
    @Test public void
    reportsWhenCannotFindTopLevelFrame() {
        prober.setTimeout(100);
        
        try {
            ComponentSelector<JFrame> selector = JFrameDriver.topLevelFrame(ComponentMatchers.named("--no-such-frame--"));
            prober.check("testing error messages", selector);
        }
        catch (AssertionError e) {
            String message = e.getMessage();
            assertThat(message, StringContainsInOrderMatcher.containsInOrder(
                "testing error messages",
                "Looked for",
                "exactly 1",
                "JFrame",
                "is named \"--no-such-frame--\"",
                "in all top level windows",
                "but found 0"
                ));
        }
    }
}
