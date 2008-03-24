package com.objogate.wl.driver;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import com.objogate.exception.Defect;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.Query;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.Gestures;

public class JTableDriver extends ComponentDriver<JTable> {

    public JTableDriver(ComponentDriver<? extends Container> containerDriver, Matcher<? super JTable>... matchers) {
        super(containerDriver, JTable.class, matchers);
    }

    public JTableDriver(GesturePerformer gesturePerformer, JTable component) {
        super(gesturePerformer, component);
    }

    public JTableDriver(GesturePerformer gesturePerformer, JTable component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public JTableDriver(GesturePerformer gesturePerformer, ComponentSelector<JTable> componentSelector) {
        super(gesturePerformer, componentSelector);
    }

    public JTableDriver(GesturePerformer gesturePerformer, ComponentSelector<JTable> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JTableDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JTable> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JTableDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JTable> componentType, Matcher<? super JTable>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public static boolean arrayContains(int[] stuff, int item) {
        for (int selectedRow : stuff) {
            if (item == selectedRow)
                return true;
        }
        return false;
    }

    public void selectCell(Cell cell) {
        selectCell(cell.row, cell.col);
    }

    public void selectCells(Cell... cells) {
        int keyCode = multiSelectKey();
        performGesture(Gestures.pressKey(keyCode));
        for (Cell cell : cells) {
            selectCell(cell);
        }
        performGesture(Gestures.releaseKey(keyCode));
    }

    public void selectCell(final int row, final int col) {
        mouseOverCell(row, col);
        performGesture(Gestures.leftClickMouse());
    }

    // todo (nick): this is wrong but can't figure out another way - need to keep looking for a cell until its found
    public void selectCell(final Matcher<? extends JComponent> matcher) {
        final Cell[] cell = new Cell[1];
        perform("finding cell", new ComponentManipulation<JTable>() {
            public void manipulate(JTable component) {
                int rowCount = component.getRowCount();
                int columnCount = component.getColumnCount();

                for(int row = 0; row < rowCount; row++) {
                    for(int col = 0; col < columnCount; col++) {
                        Component renderedCell = JTableCellManipulation.render(component, row, col);
                        if(matcher.matches(renderedCell)) {
                            cell[0] = new Cell(row, col);
                        }
                    }
                }
            }
        });

        if(cell[0] == null)
            throw new Defect("Cannot find cell");

        selectCell(cell[0]);
    }

    public Component editCell(int row, int col) {
        mouseOverCell(row, col);
        performGesture(Gestures.doubleClickMouse());

        JTableCellManipulation manipulation = new JTableCellManipulation(row, col);
        perform("finding cell editor", manipulation);

        return manipulation.getEditorComponent();
    }

    public void mouseOverCell(int row, int col) {
        scrollCellToVisible(row, col);

        int rowHeight = rowHeight();
        int y = (rowHeight * row) + (rowHeight / 2);
        int x = relativeMidpointOfColumn(col);

        moveMouseToOffset(x, y);
    }

    private int relativeMidpointOfColumn(final int col) {
        ColumnManipulation manipulation = new ColumnManipulation(col);
        perform("column mid point", manipulation);
        return manipulation.getMidPoint();
    }

    private int rowHeight() {
        JTableRowHeightManipulation tableManipulation = new JTableRowHeightManipulation();
        perform("row height", tableManipulation);
        return tableManipulation.getRowHeight();
    }

    public void hasSelectedCells(final Cell... cells) {
        is(new SelectedTableCellsMatcher(cells));
    }

    public void scrollCellToVisible(final int row, final int col) {
        perform("table scrolling", new ComponentManipulation<JTable>() {
            public void manipulate(JTable table) {
                table.scrollRectToVisible(table.getCellRect(row, col, true));
            }
        });
    }

    public void cellHasColour(int row, Object columnIdentifier, Matcher<Color> foregroundColor, Matcher<Color> backgroundColor) {
        cellHasBackgroundColor(row, columnIdentifier, backgroundColor);
        cellHasForegroundColor(row, columnIdentifier, foregroundColor);
    }

    public void cellHasColour(int row, int col, Matcher<Color> foregroundColor, Matcher<Color> backgroundColor) {
        cellHasBackgroundColor(row, col, backgroundColor);
        cellHasForegroundColor(row, col, foregroundColor);
    }

    public void cellHasBackgroundColor(final int row, final Object columnIdentifier, Matcher<Color> backgroundColor) {
        has(new Query<JTable, Color>() {
            public Color query(JTable component) {
                return JTableCellManipulation.render(component, row, columnIdentifier).getBackground();
            }

            public void describeTo(Description description) {
                description.appendText("background color in cell at " + row + "x" + columnIdentifier);
            }
        }, backgroundColor);
    }

    public void cellHasBackgroundColor(final int row, final int col, Matcher<Color> backgroundColor) {
        has(new Query<JTable, Color>() {
            public Color query(JTable component) {
                return JTableCellManipulation.render(component, row, col).getBackground();
            }

            public void describeTo(Description description) {
                description.appendText("background colour in cell at " + row + "x" + col);
            }
        }, backgroundColor);
    }

    public void cellHasForegroundColor(final int row, final Object columnIdentifier, Matcher<Color> foregroundColor) {
        has(new Query<JTable, Color>() {
            public Color query(JTable component) {
                Component rendered = JTableCellManipulation.render(component, row, columnIdentifier);
                return rendered.getForeground();
            }

            public void describeTo(Description description) {
                description.appendText("foreground color in cell at " + row + "x" + columnIdentifier);
            }
        }, foregroundColor);
    }

    public void cellHasForegroundColor(final int row, final int col, Matcher<Color> foregroundColor) {
        has(new Query<JTable, Color>() {
            public Color query(JTable component) {
                return JTableCellManipulation.render(component, row, col).getForeground();
            }

            public void describeTo(Description description) {
                description.appendText("foreground colour in cell at " + row + "x" + col);
            }
        }, foregroundColor);
    }

    public static Cell cell(int row, int col) {
        return new Cell(row, col);
    }

    public void cellRenderedWithText(final int row, final Object columnIdentifier, Matcher<String> expectedText) {
        has(new CellTextQuery(row, columnIdentifier), expectedText);
    }

    public void cellRenderedWithText(final int row, final int col, Matcher<String> expectedText) {
        has(new CellTextQuery(row, col), expectedText);
    }

    public static class Cell {

        public final int row, col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public String toString() {
            return row + "x" + col;
        }
    }

    private class SelectedTableCellsMatcher extends TypeSafeMatcher<JTable> {
        public Cell unselectedCell;
        private final Cell[] cells;

        public SelectedTableCellsMatcher(Cell... cells) {
            this.cells = cells;
        }

        @Override
        public boolean matchesSafely(JTable component) {
            int[] selectedRows = component.getSelectedRows();
            int[] selectedColumns = component.getColumnModel().getSelectedColumns();
            for (Cell cell : cells) {
                if (!(arrayContains(selectedRows, cell.row) && arrayContains(selectedColumns, cell.col))) {
                    this.unselectedCell = cell;
                    return false;
                }
            }
            return true;
        }

        public void describeTo(Description description) {
            description.appendText("cell " + unselectedCell + " is not selected");
        }
    }

    private static class JTableRowHeightManipulation implements ComponentManipulation<JTable> {
        private int rowHeight;

        public void manipulate(JTable component) {
            rowHeight = component.getRowHeight();
        }

        public int getRowHeight() {
            return rowHeight;
        }
    }

    private static class ColumnManipulation implements ComponentManipulation<JTable> {
        private int midpoint;
        private final int col;

        public ColumnManipulation(int col) {
            this.col = col;
        }

        public void manipulate(JTable component) {
            midpoint = JTableHeaderDriver.ColumnManipulation.midpointOfColumn(col, component.getColumnModel());
        }

        public int getMidPoint() {
            return midpoint;
        }
    }

    private static class CellTextQuery implements Query<JTable, String> {
        private final int row;
        private Object columnIdentifier;
        private int col;

        public CellTextQuery(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public CellTextQuery(int row, Object columnIdentifier) {
            this.row = row;
            this.columnIdentifier = columnIdentifier;
        }

        public String query(JTable component) {
            Component cell = columnIdentifier == null ?
                    JTableCellManipulation.render(component, row, col) :
                    JTableCellManipulation.render(component, row, columnIdentifier);

            if (cell instanceof JLabel) {
                JLabel label = (JLabel) cell;
                return label.getText();
            } else {
                throw new Defect("Rendered component in cell " + row + "x" + col + " is not a JLabel but a " + component.getClass().getName());
            }
        }

        public void describeTo(Description description) {
            description.appendText("text in cell at " + row + " x " + col);
        }
    }
}
