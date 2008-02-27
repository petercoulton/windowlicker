package com.objogate.wl.driver.tests;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import static javax.swing.SwingConstants.*;
import java.awt.Dimension;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JTabbedPaneDriver;

public class JTabbedPaneDriverTest extends AbstractComponentDriverTest<JTabbedPaneDriver> {
    JTabbedPane pane = new JTabbedPane();

    @Before
    public void testShowMessageDialog() throws Exception {
        view(pane);

        driver = new JTabbedPaneDriver(gesturePerformer, pane, prober);

        pane.addTab("aaaaa", component("the"));
        pane.addTab("bb", component("quick"));
        pane.addTab("cccc", component("brown"));
        pane.addTab("d", component("fox"));
    }

    @Test
    public void testCanNavigateHiddenTabs() throws Exception {
        pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        Dimension smallEnoughToShowTheSelectionControl = new Dimension(100, 100);
        pane.setPreferredSize(smallEnoughToShowTheSelectionControl);
        frame.pack();
        
        pause();
    }

    @Test
    public void testCanCountTabs() {
        driver.hasTabCount(4);

        pane.addTab("e", component("!"));

        driver.hasTabCount(5);
    }

    @Test
    public void testCanSelectLeftAlignedTabsByIndex() throws Exception {
        pane.setTabPlacement(LEFT);
        frame.pack();

        checkTabSelectionByIndex();
    }

    @Test
    public void testCanSelectRightAlignedTabsByIndex() throws Exception {
        pane.setTabPlacement(RIGHT);
        frame.pack();

        checkTabSelectionByIndex();
    }

    @Test
    public void testCanSelectTopAlignedTabsByIndex() throws Exception {
        pane.setTabPlacement(TOP);
        frame.pack();

        checkTabSelectionByIndex();
    }

    @Test
    public void testCanSelectBottomAlignedTabsByIndex() throws Exception {
        pane.setTabPlacement(BOTTOM);
        frame.pack();

        checkTabSelectionByIndex();
    }

    private void checkTabSelectionByIndex() {
        driver.selectTab(0);

        driver.hasSelectedTab(0);

        driver.selectTab(3);

        driver.hasSelectedTab(3);
    }

    private JLabel component(String s) {
        JLabel jLabel = new JLabel(s);
        jLabel.setFont(jLabel.getFont().deriveFont(20f));
        jLabel.setPreferredSize(new Dimension(300, 200));
        return jLabel;
    }
}
