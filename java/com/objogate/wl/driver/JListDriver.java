package com.objogate.wl.driver;

import javax.swing.JList;
import javax.swing.ListModel;
import java.awt.Component;
import java.awt.Rectangle;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.Query;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.Gestures;

public class JListDriver extends ComponentDriver<JList> implements ListDriver {

    public JListDriver(GesturePerformer gesturePerformer, JList component) {
        super(gesturePerformer, component);
    }

    public JListDriver(GesturePerformer gesturePerformer, JList component, Prober prober) {
        super(gesturePerformer, component, prober);
    }

    public JListDriver(GesturePerformer gesturePerformer, ComponentSelector<JList> componentSelector) {
        super(gesturePerformer, componentSelector);
    }

    public JListDriver(GesturePerformer gesturePerformer, ComponentSelector<JList> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JListDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JList> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JListDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JList> componentType, Matcher<? super JList>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void hasSelectedIndex(int expectedIndex) {
        has(new Query<JList, Integer>() {
            public Integer query(JList component) {
                return component.getSelectedIndex();
            }

            public void describeTo(Description description) {
                description.appendText("selected list index");
            }
        }, Matchers.equalTo(expectedIndex));
    }

    public void selectItem(Matcher<? extends Component> matcher) {
        selectItem(firstItemMatching(matcher));
    }

    public void selectItem(int index) {
        mouseOverCell(index);
        performGesture(Gestures.leftClickMouse());
    }

    private int firstItemMatching(final Matcher<? extends Component> matcher) {
        final int[] index = new int[1];
        is(new TypeSafeMatcher<JList>() {
            public boolean matchesSafely(JList component) {
                ListModel model = component.getModel();
                for (int i = 0; i < model.getSize(); i++) {
                    Component rendered = renderedCell(component, i, false, false);
                    if (matcher.matches(rendered)) {
                        index[0] = i;
                        return true;
                    }
                }
                return false;
            }

            public void describeTo(Description description) {
                description.appendDescriptionOf(matcher);
            }
        });

        return index[0];
    }

    private void mouseOverCell(final int index) {
        scrollCellToVisible(index);

        CellHeightManipulation cellHeightManipulation = new CellHeightManipulation(index);
        perform("locating cell", cellHeightManipulation);

        moveMouseToOffset(10, cellHeightManipulation.getY());
    }

    private Component renderedCell(JList component, int i, boolean selected, boolean cellHasFocus) {
        return component.getCellRenderer()
                .getListCellRendererComponent(
                        component,
                        component.getModel().getElementAt(i),
                        i,
                        selected,
                        cellHasFocus
                );
    }

    public void scrollCellToVisible(final int row) {
        perform("scrolling cell to visible", new ComponentManipulation<JList>() {
            public void manipulate(JList list) {
                Rectangle r = list.getCellBounds(row, row);
                list.scrollRectToVisible(r);
            }
        });
    }

    private class CellHeightManipulation implements ComponentManipulation<JList> {
        private int y;
        private final int index;

        public CellHeightManipulation(int index) {
            this.index = index;
        }

        public int getY() {
            return y;
        }

        public void manipulate(JList component) {
            for (int i = 0; i < index; i++) {
                Component rendered = renderedCell(component, i, false, false);

                y += rendered.getPreferredSize().height;
            }

            y += renderedCell(component, index, false, false).getPreferredSize().height / 2;
        }
    }
}
