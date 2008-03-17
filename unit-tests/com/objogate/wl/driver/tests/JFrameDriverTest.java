package com.objogate.wl.driver.tests;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JFrameDriver;
import com.objogate.wl.driver.JMenuBarDriver;
import com.objogate.wl.matcher.JMenuTextMatcher;

public class JFrameDriverTest extends AbstractComponentDriverTest<JFrameDriver> {
    @Before
    public void setUp() throws Exception {
        view(new JLabel(""));
    }

    @Test
    public void assertsTitleText() throws Exception {
        frameDriver.hasTitle("JFrameDriverTest");
    }

    @Test
    public void assertsJMenu() throws Exception {
        JMenuBar bar = new JMenuBar();
        bar.add(new JMenu("hello"));
        frame.setJMenuBar(bar);

        JMenuBarDriver barDriver = frameDriver.menuBar();
        barDriver.has(new JMenuTextMatcher("hello"));
    }

}
