package com.objogate.wl.swing.driver.tests;

import static com.objogate.wl.swing.driver.tests.ReallyBigJList.applyFontRenderer;
import static com.objogate.wl.swing.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.containsString;

import javax.swing.JComboBox;

import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.swing.driver.JComboBoxDriver;
import com.objogate.wl.swing.matcher.JLabelTextMatcher;

public class JComboBoxDriverTest extends AbstractComponentDriverTest<JComboBoxDriver> {
    private final BigListOfRandomlySizedFonts data = new BigListOfRandomlySizedFonts();
    private int lastItemIndex = data.size() - 1;
    private final JComboBox comboBox = new JComboBox(data.toArray());

    @Before
    public void setUp() throws Exception {
        view(comboBox);

        driver = new JComboBoxDriver(gesturePerformer, selectorFor(comboBox), prober);
    }

    @Test
    public void testSelectingItemsByIndex() {
        driver.selectItem(0);
        driver.hasSelectedIndex(0);
        
        driver.selectItem(lastItemIndex);
        driver.hasSelectedIndex(lastItemIndex);
    }

    @Test
    public void testSelectingItemByMatchingRenderedValue() {
        comboBox.setRenderer(applyFontRenderer(comboBox.getRenderer()));

        String name = data.get(lastItemIndex).getName();

        driver.selectItem(labelWithTextMatching(name));

        driver.hasSelectedIndex(lastItemIndex);
    }

    @Test
    public void testEditing() throws InterruptedException {
        comboBox.setEditable(true);
        
        pause();

    }

    private JLabelTextMatcher labelWithTextMatching(String name) {
        return new JLabelTextMatcher(containsString(name));
    }
}
