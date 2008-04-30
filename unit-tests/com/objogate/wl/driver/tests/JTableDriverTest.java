package com.objogate.wl.driver.tests;

import static com.objogate.wl.driver.JTableDriver.cell;
import static com.objogate.wl.driver.JTableDriver.matching;
import static com.objogate.wl.matcher.JLabelTextMatcher.withLabelText;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.Platform;
import com.objogate.wl.driver.JTableDriver;
import com.objogate.wl.driver.JTableHeaderDriver;
import com.objogate.wl.driver.JTextFieldDriver;

public class JTableDriverTest extends AbstractComponentDriverTest<JTableDriver> {
    private static final JTable SMALL_TABLE = new JTable(
            new Object[][] { new Object[] { "1x1", "1x2", "1x3" },
                             new Object[] { "2x1", "2x2", "2x3" } },
            new Object[] { "one", "two", "three" } );
    public static final NamedColor BLACK = NamedColor.color("BLACK");
    public static final NamedColor WHITE = NamedColor.color("WHITE");
    public static final NamedColor YELLOW = NamedColor.color("YELLOW");
    private ReallyBigTable table = new ReallyBigTable();

    @Before public void setUp() throws Exception {
      table.pad();
      table.setRowHeight(table.getRowHeight() + 10);
      driver = createDriverFor(table);
    }

    private JTableDriver createDriverFor(JTable drivenTable) {
      JScrollPane pane = new JScrollPane(drivenTable);
      pane.setPreferredSize(new Dimension(800, 600));
      view(pane);

      return new JTableDriver(gesturePerformer, drivenTable, prober);
    }

    @Test public void 
    detectsHasCellMatching() {
        driver.hasCell(withLabelText(equalTo("1x1")));
    }

    @SuppressWarnings("unchecked")
    @Test public void
    detectsHasRowMatching() {
      JTableDriver smallDriver = createDriverFor(SMALL_TABLE);
      smallDriver.hasRow(matching(withLabelText("2x1"), withLabelText("2x2"), withLabelText("2x3"))); 
    }

    
    @SuppressWarnings("unchecked")
    @Test public void
    reportsWhenDoesNotHaveRowMatching() {
      prober.setTimeout(300);
      JTableDriver smallDriver = createDriverFor(SMALL_TABLE);
      try {
        smallDriver.hasRow(matching(withLabelText("2x1"), withLabelText("1x2"), withLabelText("2x3")));
      } catch (AssertionError expected) {
        assertThat(expected.getMessage(), 
                   containsString("row with cells with text \"2x1\", with text \"1x2\", with text \"2x3\""));
        return;
      }
      fail("Should have failed");
    }

    @Test public void 
    matchesOnCellColour() {
        table.stripe(YELLOW, WHITE, BLACK);

        driver.cellHasColour(0, 1, matchingColor(BLACK), matchingColor(YELLOW));
        driver.cellHasColour(0, "b", matchingColor(BLACK), matchingColor(YELLOW));
        driver.cellHasBackgroundColor(0, 1, matchingColor(YELLOW));
        driver.cellHasBackgroundColor(0, "b", matchingColor(YELLOW));

        driver.cellHasColour(1, 1, matchingColor(BLACK), matchingColor(WHITE));
        driver.cellHasColour(1, "b", matchingColor(BLACK), matchingColor(WHITE));
        driver.cellHasForegroundColor(1, 1, matchingColor(BLACK));
        driver.cellHasForegroundColor(1, "b", matchingColor(BLACK));
    }

    @Test public void
    selectingSingleCell() throws Exception {
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        driver.selectCell(1, 1);
        driver.hasSelectedCells(cell(1, 1));

        driver.selectCell(2, 2);
        driver.hasSelectedCells(cell(2, 2));

        driver.selectCell(3, 3);
        driver.hasSelectedCells(cell(3, 3));

        driver.selectCell(table.getRowCount() - 1, 3);
        driver.hasSelectedCells(cell(table.getRowCount() - 1, 3));
    }

    @Test public void 
    selectingMultipleNonContiguousCells() throws Exception {
        table.setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        driver.selectCells(cell(1, 1), cell(3, 3), cell(6, 5));

        driver.hasSelectedCells(cell(1, 1), cell(3, 3), cell(6, 5));
    }

    @Test public void 
    selectingMultipleRows() throws Exception {
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        driver.selectCells(cell(1, 1), cell(2, 1), cell(3, 1));

        driver.hasSelectedCells(
                cell(1, 0), cell(1, 4),
                cell(2, 0), cell(2, 4),
                cell(3, 0), cell(3, 4)
        );
    }

    @Test public void 
    selectingMultipleColumns() throws Exception {
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(true);

        driver.selectCells(cell(1, 1), cell(2, 2), cell(3, 3));

        driver.hasSelectedCells(
                cell(1, 1), cell(1, 2), cell(1, 3),
                cell(9, 1), cell(9, 2), cell(9, 3)
        );
    }

    @Test public void 
    blockSelectionWithKeyboard() throws Exception {
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);

        driver.selectCells(cell(1, 1), cell(2, 2), cell(3, 3));

        driver.hasSelectedCells(
                cell(1, 1), cell(1, 2), cell(1, 3),
                cell(2, 1), cell(2, 2), cell(2, 3),
                cell(3, 1), cell(3, 2), cell(3, 3)
        );
    }

    @Test public void 
    blockSelectionWithMouse() throws Exception {
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);

        driver.dragMouseOver(cell(1, 1), cell(3, 3));
        driver.hasSelectedCells(
                cell(1, 1), cell(1, 2), cell(1, 3),
                cell(2, 1), cell(2, 2), cell(2, 3),
                cell(3, 1), cell(3, 2), cell(3, 3)
        );

        driver.dragMouseOver(cell(4, 3), cell(1, 1));
        driver.hasSelectedCells(
                cell(1, 1), cell(2, 2), cell(3, 3), cell(4, 3)
        );
    }

    @Test public void 
    cellRenderedWithText() throws Exception {
        driver.cellRenderedWithText(0, 0, Matchers.containsString("0x0"));

        moveColumn("a", 1);

        driver.cellRenderedWithText(0, 1, Matchers.containsString("0x0"));
        driver.cellRenderedWithText(1, "a", Matchers.containsString("1x0"));
    }

    @Test
    @Problematic(why="doesnt seem to be able to select the right cell", platform = Platform.Linux )
    public void 
    editingCells() throws Exception {
        table.addJTextFieldEditorToColumn(0);

        enterText(driver.editCell(0, 0), "hello");
        driver.cellRenderedWithText(0, 0, Matchers.containsString("hello"));

        enterText(driver.editCell(2, 2), "world!");
        driver.cellRenderedWithText(2, 2, Matchers.containsString("world!"));
    }

    private void enterText(Component editorComponent, String text) {
        JTextFieldDriver textFieldDriver = new JTextFieldDriver(gesturePerformer, (JTextField) editorComponent, prober);
        textFieldDriver.replaceAllText(text);
        textFieldDriver.typeText("\n");
    }

    private void moveColumn(String columnIdentifier, int movement) {
        new JTableHeaderDriver(gesturePerformer, table.getTableHeader(), prober).moveColumn(columnIdentifier, movement);
    }

    private Matcher<Color> matchingColor(final Color expected) {
        return equalTo(expected);
    }
}
