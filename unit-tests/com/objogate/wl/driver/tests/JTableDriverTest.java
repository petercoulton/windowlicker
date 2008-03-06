package com.objogate.wl.driver.tests;

import static com.objogate.wl.driver.JTableDriver.cell;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.driver.JTableDriver;
import com.objogate.wl.driver.JTableHeaderDriver;
import com.objogate.wl.driver.JTextFieldDriver;

public class JTableDriverTest extends AbstractComponentDriverTest<JTableDriver> {
    private ReallyBigTable table = new ReallyBigTable();

    @Before
    public void setUp() throws Exception {
        view(new JScrollPane(table));

        driver = new JTableDriver(gesturePerformer, table, prober);
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
    public void testCellSelection() throws Exception {
        driver.selectCell(200, 2);

        driver.hasSelectedCells(cell(200, 2));
    }

    @Test
    public void testSelectingMultipleCells() throws Exception {
        table.setCellSelectionEnabled(true);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        driver.selectCells(cell(1, 1), cell(4, 4), cell(8, 8));

        driver.hasSelectedCells(cell(1, 1), cell(4, 4), cell(8, 8));
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

        Component editorComponent = driver.editCell(0, 0);

        JTextFieldDriver textFieldDriver = new JTextFieldDriver(gesturePerformer, (JTextField) editorComponent, prober);
        textFieldDriver.replaceAllText("hello");

        driver.cellRenderedWithText(0, 0, Matchers.containsString("hello"));
    }

    private void moveColumn(String columnIdentifier, int movement) {
        new JTableHeaderDriver(gesturePerformer, table.getTableHeader(), prober).moveColumn(columnIdentifier, movement);
    }

    private Matcher<Color> matchingColor(final Color expected) {
        return equalTo(expected);
    }
}
