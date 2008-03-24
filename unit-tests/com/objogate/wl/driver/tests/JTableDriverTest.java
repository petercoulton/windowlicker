package com.objogate.wl.driver.tests;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JTableDriver;
import static com.objogate.wl.driver.JTableDriver.cell;
import com.objogate.wl.driver.JTableHeaderDriver;
import com.objogate.wl.driver.JTextFieldDriver;
import com.objogate.wl.matcher.JLabelTextMatcher;

public class JTableDriverTest extends AbstractComponentDriverTest<JTableDriver> {
    private ReallyBigTable table = new ReallyBigTable();

    @Before
    public void setUp() throws Exception {
        table.pad();
        table.setRowHeight(table.getRowHeight() + 10);
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(800, 600));
        view(pane);

        driver = new JTableDriver(gesturePerformer, table, prober);
    }

    @Test
    public void testHasCellMatching() {
        driver.hasCell(new JLabelTextMatcher(Matchers.equalTo("1x1")));
    }

    @Test
    public void testCellColour() {
        NamedColor yellow = NamedColor.color("YELLOW");
        NamedColor white = NamedColor.color("WHITE");
        NamedColor black = NamedColor.color("BLACK");

        table.stripe(yellow, white, black);

        driver.cellHasColour(0, 1, matchingColor(black), matchingColor(yellow));
        driver.cellHasColour(0, "b", matchingColor(black), matchingColor(yellow));
        driver.cellHasBackgroundColor(0, 1, matchingColor(yellow));
        driver.cellHasBackgroundColor(0, "b", matchingColor(yellow));

        driver.cellHasColour(1, 1, matchingColor(black), matchingColor(white));
        driver.cellHasColour(1, "b", matchingColor(black), matchingColor(white));
        driver.cellHasForegroundColor(1, 1, matchingColor(black));
        driver.cellHasForegroundColor(1, "b", matchingColor(black));
    }

    @Test
    public void testSingleCellSelection() throws Exception {
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

    @Test
    public void testSelectingMultipleCells() throws Exception {
        table.setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        driver.selectCells(cell(1, 1), cell(3, 3), cell(6, 5));

        driver.hasSelectedCells(cell(1, 1), cell(3, 3), cell(6, 5));
    }

    @Test
    public void testSelectingMultipleRows() throws Exception {
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        driver.selectCells(cell(1, 1), cell(2, 1), cell(3, 1));

        driver.hasSelectedCells(
                cell(1, 0), cell(1, 4),
                cell(2, 0), cell(2, 4),
                cell(3, 0), cell(3, 4)
        );
    }

    @Test
    public void testSelectingMultipleColumns() throws Exception {
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(true);

        driver.selectCells(cell(1, 1), cell(2, 2), cell(3, 3));

        driver.hasSelectedCells(
                cell(1, 1), cell(1, 2), cell(1, 3),
                cell(9, 1), cell(9, 2), cell(9, 3)
        );
    }

    @Test
    public void testBlockSelectionWithKeyboard() throws Exception {
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

    @Test
    public void testBlockSelectionWithMouse() throws Exception {
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

    @Test
    public void testCellText() throws Exception {
        driver.cellRenderedWithText(0, 0, Matchers.containsString("0x0"));

        moveColumn("a", 1);

        driver.cellRenderedWithText(0, 1, Matchers.containsString("0x0"));
        driver.cellRenderedWithText(1, "a", Matchers.containsString("1x0"));
    }

    @Test
    @Problematic(why="doesnt seem to be able to select the right cell", platform = Platform.Linux )
    public void testEditingCells() throws Exception {
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
