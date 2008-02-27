package com.objogate.wl.driver.tests;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import org.junit.Test;
import com.objogate.wl.driver.JScrollbarDriver;

public class JScrollpaneDriverTest extends AbstractComponentDriverTest<JScrollbarDriver> {

    //not really a test - just noodling with scrollbars...
    @Test
    public void testNoodling() throws InterruptedException {
        JScrollPane pane = new JScrollPane(new ReallyBigTable(),
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        view(pane);

        driver = new JScrollbarDriver(frameDriver, pane, JScrollBar.HORIZONTAL);

        driver.scroll(200);

        Thread.sleep(5000);
    }
}