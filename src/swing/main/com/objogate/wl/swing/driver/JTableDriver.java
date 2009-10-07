package com.objogate.wl.swing.driver;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.util.Iterator;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import com.objogate.exception.Defect;
import com.objogate.wl.Gesture;
import com.objogate.wl.Prober;
import com.objogate.wl.Query;
import com.objogate.wl.gesture.Gestures;
import static com.objogate.wl.gesture.Gestures.*;
import com.objogate.wl.gesture.Tracker;
import com.objogate.wl.swing.ComponentManipulation;
import com.objogate.wl.swing.ComponentSelector;
import com.objogate.wl.swing.driver.table.*;
import static com.objogate.wl.swing.driver.table.JTableCellManipulation.render;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class JTableDriver extends JComponentDriver<JTable> {
    public JTableDriver(ComponentDriver<? extends Container> containerDriver, Matcher<? super JTable>... matchers) {
        super(containerDriver, JTable.class, matchers);
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

    public void selectCells(Cell... cells) {
        scrollCellToVisible(cells[0]);
        
        Gesture[] gestures = new Gesture[cells.length];
        for (int i = 0; i < cells.length; i++) {
           gestures[i] = sequence(
                   moveMouseTo(pointIn(cells[i])),
                   leftClickMouse()
           );
        }

        performGesture(whileHoldingMultiSelect(sequence(gestures)));
    }

    public void selectCell(final int row, final int col) {
        selectCells(cell(row, col));
    }

    public void selectCell(final Matcher<? extends JComponent> matcher) {
        final Cell cell = hasCell(matcher);

        if (cell == null) {
            throw new Defect("Cannot find cell");
        }

        selectCells(cell);
    }

    public void dragMouseOver(Cell start, Cell end) {
        scrollCellToVisible(start);

        performGesture(
                moveMouseTo(pointIn(start)),
                whileHoldingMouseButton(BUTTON1,
                        moveMouseTo(pointIn(end)))
        );
    }

    private Tracker pointIn(Cell start) {
        return offset(relativeMidpointOfColumn(start.col), rowOffset(start.row));
    }

    public Cell hasCell(final Matcher<? extends JComponent> matcher) {
      RenderedCellMatcher cellMatcher = new RenderedCellMatcher(matcher);
        
      is(new CellInTableMatcher(cellMatcher));

      return cellMatcher.foundCell.cell;
    }
    
    public void hasRow(Matcher<Iterable<? extends Component>> rowMatcher) {
      is(new RowInTableMatcher(rowMatcher));
    }

    public Component editCell(int row, int col) {
        mouseOverCell(row, col);
        performGesture(Gestures.doubleClickMouse());

        JTableCellManipulation manipulation = new JTableCellManipulation(row, col);
        perform("finding cell editor", manipulation);

        return manipulation.getEditorComponent();
    }

    public void mouseOverCell(Cell cell) {
        mouseOverCell(cell.row, cell.col);
    }

    public void mouseOverCell(int row, int col) {
        scrollCellToVisible(row, col);

        int y = rowOffset(row);
        int x = relativeMidpointOfColumn(col);

        moveMouseToOffset(x, y);
    }

    private int rowOffset(int row) {
        int rowHeight = rowHeight();
        return (rowHeight * row) + (rowHeight / 2);
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
        is(new SelectedCellsMatcher(cells));
    }

    public void scrollCellToVisible(Cell cell) {
        scrollCellToVisible(cell.row, cell.col);
    }

    //todo (nick): this should be a gesture
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
      cellHasBackgroundColor(cell(row, columnIdentifier), backgroundColor);
    }

    public void cellHasBackgroundColor(final int row, final int col, Matcher<Color> backgroundColor) {
      cellHasBackgroundColor(cell(row, col), backgroundColor);
    }
    
    public void cellHasForegroundColor(final int row, final Object columnIdentifier, Matcher<Color> foregroundColor) {
      cellHasForegroundColor(cell(row, columnIdentifier), foregroundColor);
    }

    public void cellHasForegroundColor(final int row, final int col, Matcher<Color> foregroundColor) {
      cellHasForegroundColor(cell(row, col), foregroundColor);
    }
    
    public void cellRenderedWithText(final int row, final Object columnIdentifier, Matcher<String> expectedText) {
        cellRenderedWithText(cell(row, columnIdentifier), expectedText);
    }

    public void cellRenderedWithText(final int row, final int col, Matcher<String> expectedText) {
        cellRenderedWithText(cell(row, col), expectedText);
    }

    public void cellHasForegroundColor(final Location cell, Matcher<Color> foregroundColor) {
      has(cellWith(cell, foregroundColor()), foregroundColor);
    }
    
    public void cellHasBackgroundColor(final Location cell, Matcher<Color> backgroundColor) {
      has(cellWith(cell, backgroundColor()), backgroundColor);
    }

    public void cellRenderedWithText(final Location cell, Matcher<String> expectedText) {
      has(cellWith(cell, labelText()), expectedText);
    }

    public static <T> Query<JTable, T> cellWith(Location cell, Query<Component, T> detail) {
      return new RenderedCellQuery<T>(cell, detail);
    }
    
    public static Query<Component, Color> foregroundColor() {
      return new Query<Component, Color>() {
        public Color query(Component component) { return component.getForeground(); }
        public void describeTo(Description description) { description.appendText("foreground colour"); }
      };
    }

    public static Query<Component, Color> backgroundColor() {
      return new Query<Component, Color>() {
        public Color query(Component component) { return component.getBackground(); }
        public void describeTo(Description description) { description.appendText("background colour"); }
      };
    }

    public static IdentifierCell cell(final int row, final Object columnIdentifier) {
      return new IdentifierCell(row, columnIdentifier);
    }

    public static Cell cell(int row, int col) {
      return new Cell(row, col);
    }

    public static Query<Component, String> labelText() {
      return new Query<Component, String>() {
        public String query(Component cell) {
          return ((JLabel) cell).getText();
        }

        public void describeTo(Description description) {
            description.appendText("text");
        }
      };
    }

    private class SelectedCellsMatcher extends TypeSafeMatcher<JTable> {
        public Cell unselectedCell;
        private final Cell[] cells;

        public SelectedCellsMatcher(Cell... cells) {
            this.cells = cells;
        }

        @Override
        public boolean matchesSafely(JTable table) {
            for (Cell cell : cells) {
                if (!table.isCellSelected(cell.row, cell.col)) {
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
    
    private static final class RenderedCellMatcher extends TypeSafeMatcher<RenderedCell> {
      private final Matcher<? extends JComponent> matcher;
      RenderedCell foundCell;

      RenderedCellMatcher(Matcher<? extends JComponent> matcher) {
        this.matcher = matcher;
      }

      @Override public boolean matchesSafely(RenderedCell renderedCell) {
        if (matcher.matches(renderedCell.rendered)) {
          foundCell = renderedCell;
          return true;
        }
        return false;
      }

      public void describeTo(Description description) {
          description.appendDescriptionOf(matcher);
      }
    }

    private static final class CellInTableMatcher extends TypeSafeMatcher<JTable> {
      private final Matcher<RenderedCell> matcher;
      CellInTableMatcher(Matcher<RenderedCell> matcher) { this.matcher = matcher; }

      @Override public boolean matchesSafely(JTable table) {
          for (int row = 0; row < table.getRowCount(); row++) {
              for (int col = 0; col < table.getColumnCount(); col++) {
                  Cell cell = cell(row, col);
                  if (matcher.matches(new RenderedCell(cell, JTableCellManipulation.render(table, cell)))) {
                      return true;
                  }
              }
          }
          return false;
      }

      public void describeTo(Description description) {
          description.appendText("with cell ")
                     .appendDescriptionOf(matcher);
      }
    }

    private static final class RowInTableMatcher extends TypeSafeMatcher<JTable> {
      private final Matcher<Iterable<? extends Component>> matcher;
      RowInTableMatcher(Matcher<Iterable<? extends Component>> matcher) { this.matcher = matcher; }

      @Override public boolean matchesSafely(JTable table) {
          for (int row = 0; row < table.getRowCount(); row++) {
            if (matcher.matches(CellRowIterator.asIterable(table, row))) {
              return true;
            }
          }
          return false;
      }

      public void describeTo(Description description) {
          description.appendText("with row ")
                     .appendDescriptionOf(matcher);
      }
    }
    
    private static class CellRowIterator implements Iterator<Component> {
      private final JTable table;
      private final int row;
      private final int width;
      private int col = 0;

      public CellRowIterator(JTable table, int row) {
        this.table = table;
        this.row = row;
        this.width = table.getColumnCount();
      }

      public boolean hasNext() { return col < width; }
      public Component next() { return render(table, cell(row, col++)); }
      public void remove() { throw new Defect("Not implemented"); }
      
      public static Iterable<Component> asIterable(final JTable table, final int row) {
        return new Iterable<Component>() {
          public Iterator<Component> iterator() { return new CellRowIterator(table, row); }
        };
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

    private static class RenderedCellQuery<T> implements Query<JTable, T> {
      private final Location location;
      private final Query<Component, T> detail;

      public RenderedCellQuery(Location location, Query<Component, T> detail) {
          this.location = location;
          this.detail = detail;
      }

      public T query(JTable table) {
          return detail.query(JTableCellManipulation.render(table, location));
      }

      public void describeTo(Description description) {
          description.appendDescriptionOf(detail)
                     .appendText(" in cell at " + location);
      }
    }

}
