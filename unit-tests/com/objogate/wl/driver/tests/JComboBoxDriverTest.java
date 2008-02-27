package com.objogate.wl.driver.tests;

import javax.swing.JComboBox;
import static org.hamcrest.Matchers.containsString;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JComboBoxDriver;
import static com.objogate.wl.driver.tests.ReallyBigJList.applyFontRenderer;
import com.objogate.wl.matcher.JLabelTextMatcher;

public class JComboBoxDriverTest extends AbstractComponentDriverTest<JComboBoxDriver> {
    private final BigListOfRandomlySizedFonts data = new BigListOfRandomlySizedFonts();
    private int lastItemIndex = data.size() - 1;
    private final JComboBox comboBox = new JComboBox(data.toArray());

    @Before
    public void setUp() throws Exception {
        view(comboBox);

        driver = new JComboBoxDriver(gesturePerformer, comboBox, prober);
    }

    @Test
    public void testSelectingItemsByIndex() throws InterruptedException {
        driver.selectItem(0);

        driver.hasSelectedIndex(0);

        driver.selectItem(lastItemIndex);

        driver.hasSelectedIndex(lastItemIndex);

    }

    @Test
    public void testSelectingItemsByObject() {
        driver.selectItem(data.get(0));

        driver.hasSelectedIndex(0);

        driver.selectItem(data.get(lastItemIndex));

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
