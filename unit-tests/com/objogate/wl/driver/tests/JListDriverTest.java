package com.objogate.wl.driver.tests;

import static com.objogate.wl.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.containsString;

import java.awt.Font;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.driver.JListDriver;
import com.objogate.wl.matcher.JLabelTextMatcher;

public class JListDriverTest extends AbstractComponentDriverTest<JListDriver> {
    ReallyBigJList list = new ReallyBigJList();

    @Before
    public void setUp() throws Exception {
        view(new JScrollPane(list));

        driver = new JListDriver(gesturePerformer, selectorFor((JList)list), prober);
    }

    @Test
    public void testSelectingItemsByIndex() {
        driver.selectItem(20);

        driver.hasSelectedIndex(20);

        driver.selectItem(3);

        driver.hasSelectedIndex(3);
    }

    @Test
    public void testHasItem() {
        final String name = ((Font) list.getModel().getElementAt(0)).getName();

        list.applyFontRenderer();

        driver.hasItem(Matchers.containsString(name));        
        driver.hasRenderedItem(labelWithTextMatching(name));
    }

    @Test
    public void testSelectingItemByRenderedValue() {
        int lastIndex = list.getModel().getSize() - 1;
        final String name = ((Font) list.getModel().getElementAt(lastIndex)).getName();

        list.applyFontRenderer();

        driver.selectItem(labelWithTextMatching(name));

        driver.hasSelectedIndex(lastIndex);
    }

    private JLabelTextMatcher labelWithTextMatching(String name) {
        return new JLabelTextMatcher(containsString(name));
    }

}
