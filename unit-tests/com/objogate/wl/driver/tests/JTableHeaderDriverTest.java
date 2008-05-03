package com.objogate.wl.driver.tests;

import static com.objogate.wl.matcher.IterableComponentsMatcher.matching;
import static com.objogate.wl.matcher.JLabelTextMatcher.withLabelText;
import static com.objogate.wl.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.driver.JTableHeaderDriver;

public class  JTableHeaderDriverTest extends AbstractComponentDriverTest<JTableHeaderDriver> {
    private ReallyBigTable table = new ReallyBigTable();

    @Before
    public void setUp() throws Exception {
        view(new JScrollPane(table));
        driver = createDriverFor(table);
    }
    
    private JTableHeaderDriver createDriverFor(JTable drivenTable) {
      JScrollPane pane = new JScrollPane(drivenTable);
      pane.setPreferredSize(new Dimension(800, 600));
      view(pane);

      return new JTableHeaderDriver(gesturePerformer, selectorFor(drivenTable.getTableHeader()), prober);
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    detectsHasHeadersMatching() {
      JTableHeaderDriver smallDriver = createDriverFor(JTableDriverTest.SMALL_TABLE);
      smallDriver.hasHeaders(matching(withLabelText("one"), withLabelText("two"), withLabelText("three"))); 
    }

    @SuppressWarnings("unchecked")
    @Test public void
    reportsTooManyHeaders() {
      reportsWrongHeaders(matching(withLabelText("one"), withLabelText("two")),
                          "headers with cells with text \"one\", with text \"two\"");
    }

    @SuppressWarnings("unchecked")
    @Test public void
    reportsMismatchedHeaders() {
      reportsWrongHeaders(matching(withLabelText("one"), withLabelText("three"), withLabelText("two")), 
                          "headers with cells with text \"one\", with text \"three\", with text \"two\"");
    }

    private void reportsWrongHeaders(Matcher<Iterable<? extends Component>> matchers, String expectedMessage) {
      prober.setTimeout(300);
      JTableHeaderDriver smallDriver = createDriverFor(JTableDriverTest.SMALL_TABLE);
      try {
        smallDriver.hasHeaders(matchers); 
      } catch (AssertionError expected) {
        assertThat(expected.getMessage(), 
                   containsString(expectedMessage));
        return;
      }
      fail("Should have failed");
    }

    @Test public void 
    canMoveColumnsToTheRight() throws Exception {
        driver.moveColumn(2, 3);

        driver.indexOfColumnIdentifiedBy("c", 5);

        driver.moveColumn("b", 1);

        driver.indexOfColumnIdentifiedBy("b", 2);
    }

    @Test public void 
    canMoveColumnsToTheLeft() throws Exception {
        driver.moveColumn(4, -1);

        driver.indexOfColumnIdentifiedBy("e", 3);

        driver.moveColumn("c", -2);

        driver.indexOfColumnIdentifiedBy("c", 0);
    }

    @Test public void 
    canMoveOffScreenColumns() throws Exception {
        driver.moveColumn(20, -5);

        driver.indexOfColumnIdentifiedBy("u", 15);
    }

    @Test public void 
    canResizeColumnByIdentifier() throws Exception {
        int widthBefore = driver.getColumnWidth("d");
        int delta = 25;

        driver.resizeColumn("d", delta);

        driver.widthOfColumn("d", widthBefore + delta);
    }

    @Test public void 
    canResizeColumnByIndex() throws Exception {
        int widthBefore = driver.getColumnWidth(2);
        int delta = -10;

        driver.resizeColumn(2, delta);

        driver.widthOfColumn(2, widthBefore + delta);
    }
}