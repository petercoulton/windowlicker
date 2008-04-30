package com.objogate.wl.driver.tests;

import static com.objogate.wl.probe.ComponentIdentity.selectorFor;

import javax.swing.JScrollPane;

import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.driver.JTableHeaderDriver;

public class  JTableHeaderDriverTest extends AbstractComponentDriverTest<JTableHeaderDriver> {
    private ReallyBigTable table = new ReallyBigTable();

    @Before
    public void setUp() throws Exception {
        view(new JScrollPane(table));

        driver = new JTableHeaderDriver(gesturePerformer, selectorFor(table.getTableHeader()), prober);
    }

    @Test
    public void testMovingColumnsToTheRight() throws Exception {
        driver.moveColumn(2, 3);

        driver.indexOfColumnIdentifiedBy("c", 5);

        driver.moveColumn("b", 1);

        driver.indexOfColumnIdentifiedBy("b", 2);
    }

    @Test
    public void testMovingColumnsToTheLeft() throws Exception {
        driver.moveColumn(4, -1);

        driver.indexOfColumnIdentifiedBy("e", 3);

        driver.moveColumn("c", -2);

        driver.indexOfColumnIdentifiedBy("c", 0);
    }

    @Test
    public void testMovingOffScreenColumns() throws Exception {
        driver.moveColumn(20, -5);

        driver.indexOfColumnIdentifiedBy("u", 15);
    }

    @Test
    public void testResizingColumnByIdentifier() throws Exception {
        int widthBefore = driver.getColumnWidth("d");
        int delta = 25;

        driver.resizeColumn("d", delta);

        driver.widthOfColumn("d", widthBefore + delta);
    }

    @Test
    public void testResizingColumnByIndex() throws Exception {
        int widthBefore = driver.getColumnWidth(2);
        int delta = -10;

        driver.resizeColumn(2, delta);

        driver.widthOfColumn(2, widthBefore + delta);
    }
}