package com.objogate.wl.driver.tests;

import javax.swing.JLabel;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JFrameDriver;

public class JFrameDriverTest extends AbstractComponentDriverTest<JFrameDriver> {
    @Before
    public void setUp() throws Exception {
        view(new JLabel(""));
    }

    @Test
    public void assertsIsDecorated() {
        frameDriver.hasDecoration();
    }

    @Test
    public void assertsTitleText() {
        frameDriver.hasTitle("JFrameDriverTest");
    }

}
