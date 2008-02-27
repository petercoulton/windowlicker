package com.objogate.wl.driver.tests;

import javax.swing.JButton;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.AbstractButtonDriver;

public class JButtonDriverTest extends AbstractButtonDriverTest<JButton, AbstractButtonDriver<JButton>> {

    @Before
    public void createButtonAndDriver() throws Exception {
        button = new JButton("button");

        view(button);

        driver = new AbstractButtonDriver<JButton>(gesturePerformer, button, prober);
    }

    @Test
    public void testNothing() {
        //only here because junit 4 won't run the tests in the superclass without it
    }
}
