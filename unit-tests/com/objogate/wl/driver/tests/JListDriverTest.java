package com.objogate.wl.driver.tests;

import javax.swing.JScrollPane;
import java.awt.Font;
import static org.hamcrest.Matchers.containsString;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JListDriver;
import com.objogate.wl.matcher.JLabelTextMatcher;

public class JListDriverTest extends AbstractComponentDriverTest<JListDriver> {
    ReallyBigJList list = new ReallyBigJList();

    @Before
    public void setUp() throws Exception {
        view(new JScrollPane(list));

        driver = new JListDriver(gesturePerformer, list, prober);
    }

    @Test
    public void testSelectingItemsByIndex() {
        driver.selectItem(25);

        driver.hasSelectedIndex(25);

        driver.selectItem(3);

        driver.hasSelectedIndex(3);
    }

    @Test
    @Problematic(why="fails randomly on gnome", platform = Platform.Linux)
    public void testSelectingItemsByObject() {
        driver.selectItem(list.getModel().getElementAt(2));

        driver.hasSelectedIndex(2);

        driver.selectItem(list.getModel().getElementAt(20));

        driver.hasSelectedIndex(20);
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
