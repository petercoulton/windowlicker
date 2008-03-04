package com.objogate.wl.driver;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.Component;
import java.awt.Point;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.ComponentQuery;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.Gestures;
import static com.objogate.wl.gesture.Gestures.moveMouseTo;
import static com.objogate.wl.gesture.Gestures.whileHoldingMouseButton;
import com.objogate.wl.gesture.MouseMoveGesture;
import com.objogate.wl.gesture.Tracker;

public class JTableHeaderDriver extends ComponentDriver<JTableHeader> {


    public JTableHeaderDriver(GesturePerformer gesturePerformer, JTableHeader component) {
        super(gesturePerformer, component);
    }

    public JTableHeaderDriver(GesturePerformer gesturePerformer, JTableHeader component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public JTableHeaderDriver(GesturePerformer gesturePerformer, ComponentSelector<JTableHeader> componentSelector) {
        super(gesturePerformer, componentSelector);
    }

    public JTableHeaderDriver(GesturePerformer gesturePerformer, ComponentSelector<JTableHeader> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JTableHeaderDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JTableHeader> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JTableHeaderDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JTableHeader> componentType, Matcher<? super JTableHeader>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void moveColumn(final Object columnIdentifier, int movement) {
        moveColumn(getIndexOfColumnIdentifiedBy(columnIdentifier), movement);
    }

    public int getIndexOfColumnIdentifiedBy(Object columnIdentifier) {
        ColumnIndexManipulation manipulation = new ColumnIndexManipulation(columnIdentifier);
        perform("index of column", manipulation);
        return manipulation.getIndex();
    }

    public void moveColumn(final int index, int movement) {
        scrollCellToVisible(-1, index);

        final int midpointOfSourceColumn = midpointOfColumn(index);

        moveMouseToOffset(midpointOfSourceColumn, heightOfHeader() / 2);

        int targetColumn = index + movement;
        final int midpointOfTargetColumn = midpointOfColumn(targetColumn);

        final int translateX = midpointOfTargetColumn - midpointOfSourceColumn;

        performGesture(whileHoldingMouseButton(Gestures.BUTTON1,
                moveMouseTo(new Tracker() {

                    private Point destination;

                    public Point target(Point currentLocation) {
                        if (destination == null) {
                            destination = new Point(currentLocation);
                            destination.translate(translateX, 0);
                        }

                        return destination;
                    }
                })));
    }

    private int heightOfHeader() {
        HeaderHeightManipulation manipulation = new HeaderHeightManipulation();
        perform("header height", manipulation);
        return manipulation.getHeight();
    }

    private int midpointOfColumn(int index) {
        ColumnManipulation manipulation = new ColumnManipulation(index);
        perform("midpoint of cell", manipulation);
        return manipulation.getMidPoint();
    }

    public void scrollCellToVisible(final int row, final int col) {
        perform("scrolling cell", new ComponentManipulation<JTableHeader>() {
            public void manipulate(JTableHeader header) {
                JTable table = header.getTable();
                table.scrollRectToVisible(table.getCellRect(row, col, true));
            }
        });
    }

    public void resizeColumn(Object columnIdentifier, int delta) {
        ColumnIndexManipulation manipulation = new ColumnIndexManipulation(columnIdentifier);
        perform("index of column", manipulation);
        resizeColumn(manipulation.getIndex(), delta);
    }

    public void resizeColumn(int col, final int delta) {
        ColumnManipulation manipulation = new ColumnManipulation(col);
        perform("position of column", manipulation);

        int position = manipulation.getLeftSideOfColumn();
        moveMouseToOffset(position, heightOfHeader() / 2);
        performGesture(whileHoldingMouseButton(Gestures.BUTTON1, new MouseMoveGesture(new Tracker() {

            Point targetLocation = null;

            public Point target(Point currentLocation) {
                if (targetLocation == null) {
                    targetLocation = new Point(currentLocation);
                    targetLocation.translate(delta, 0);
                }
                return targetLocation;
            }
        })));
    }

    public void indexOfColumnIdentifiedBy(final Object columnIdentifier, final int expectedIndex) {
        final ColumnIndexManipulation manipulation = new ColumnIndexManipulation(columnIdentifier);

        has("index of column", new ComponentQuery<JTableHeader, Integer>() {
            public Integer query(JTableHeader component) {
                manipulation.manipulate(component);
                return manipulation.getIndex();
            }
        }, Matchers.equalTo(expectedIndex));
    }

    public void widthOfColumn(Object columnIdentifier, int expectedWidth) {
        int index = getIndexOfColumnIdentifiedBy(columnIdentifier);
        widthOfColumn(index, expectedWidth);
    }

    public void widthOfColumn(final int columnIndex, int expectedWidth) {
        has("column width", new ComponentQuery<JTableHeader, Integer>() {
            public Integer query(JTableHeader component) {
                TableColumn tableColumn = component.getColumnModel().getColumn(columnIndex);
                return tableColumn.getWidth();
            }
        }, Matchers.equalTo(expectedWidth));
    }

    public int getColumnWidth(Object columnIdentifier) {
        int index = getIndexOfColumnIdentifiedBy(columnIdentifier);
        return getColumnWidth(index);
    }

    public int getColumnWidth(int col) {
        ColumnManipulation manipulation = new ColumnManipulation(col);
        perform("index of column", manipulation);
        return manipulation.getWidth();
    }

    private static class ColumnIndexManipulation implements ComponentManipulation<JTableHeader> {
        private int index;
        private final Object columnIdentifier;

        public ColumnIndexManipulation(Object columnIdentifier) {
            this.columnIdentifier = columnIdentifier;
        }

        public void manipulate(JTableHeader component) {
            index = component.getColumnModel().getColumnIndex(columnIdentifier);
        }

        public int getIndex() {
            return index;
        }
    }

    public static class ColumnManipulation implements ComponentManipulation<JTableHeader> {
        private final int index;
        private int midpointOfColumn;
        private int positionOfColumn;
        private int width;
        private int leftSideOfColumn;

        public ColumnManipulation(int index) {
            this.index = index;
        }

        public void manipulate(JTableHeader component) {
            midpointOfColumn = midpointOfColumn(index, component.getColumnModel());
            positionOfColumn = positionOfColumn(index, component.getColumnModel());
            TableColumn tableColumn = component.getColumnModel().getColumn(index);
            width = tableColumn.getWidth();
            leftSideOfColumn = positionOfColumn + width;
        }

        public int getLeftSideOfColumn() {
            return leftSideOfColumn;
        }

        public int getWidth() {
            return width;
        }

        public int getMidPoint() {
            return midpointOfColumn;
        }

        public int getPositionOfColumn() {
            return positionOfColumn;
        }

        public static int midpointOfColumn(int index, TableColumnModel columnModel) {
            int x = columnModel.getColumn(index).getWidth() / 2;
            for (int i = 0; i < index; i++) {
                x += columnModel.getColumn(i).getWidth();
            }
            return x;
        }

        public static int positionOfColumn(int index, TableColumnModel columnModel) {
            int x = 0;
            for (int i = 0; i < index; i++) {
                x += columnModel.getColumn(i).getWidth();
            }
            return x;
        }
    }

    private static class HeaderHeightManipulation implements ComponentManipulation<JTableHeader> {
        private int height;

        public void manipulate(JTableHeader component) {
            height = component.getHeight();
        }

        public int getHeight() {
            return height;
        }
    }
}
