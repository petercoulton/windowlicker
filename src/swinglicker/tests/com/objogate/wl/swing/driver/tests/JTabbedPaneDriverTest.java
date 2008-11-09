package com.objogate.wl.swing.driver.tests;

import static com.objogate.wl.swing.probe.ComponentIdentity.selectorFor;
import static javax.swing.SwingConstants.BOTTOM;
import static javax.swing.SwingConstants.LEFT;
import static javax.swing.SwingConstants.RIGHT;
import static javax.swing.SwingConstants.TOP;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.swing.driver.JTabbedPaneDriver;

public class JTabbedPaneDriverTest extends AbstractComponentDriverTest<JTabbedPaneDriver> {
    JTabbedPane pane = new JTabbedPane();

    @Before
    public void testShowMessageDialog() throws Exception {
        view(pane);

        driver = new JTabbedPaneDriver(gesturePerformer, selectorFor(pane), prober);

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

        driver.selectTab(1);

        driver.hasSelectedTab(1);

        driver.selectTab(2);

        driver.hasSelectedTab(2);
    }

    private JLabel component(String s) {
        JLabel jLabel = new JLabel(s);
        jLabel.setFont(jLabel.getFont().deriveFont(20f));
        jLabel.setPreferredSize(new Dimension(300, 200));
        return jLabel;
    }
}
