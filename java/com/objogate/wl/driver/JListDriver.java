package com.objogate.wl.driver;

import java.awt.Component;
import java.awt.Rectangle;

import javax.swing.JList;
import javax.swing.ListModel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import com.objogate.wl.Prober;
import com.objogate.wl.Query;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.matcher.JLabelTextMatcher;
import com.objogate.wl.swing.ComponentManipulation;
import com.objogate.wl.swing.ComponentSelector;

public class JListDriver extends ComponentDriver<JList> implements ListDriver {
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
        selectItem(indexOfFirstItemMatching(matcher));
    }

    public void selectItem(int index) {
        mouseOverCell(index);
        performGesture(Gestures.leftClickMouse());
    }

    public void hasItem(Matcher<String> matcher) {
        hasRenderedItem(new JLabelTextMatcher(matcher));
    }
    
    public void hasRenderedItem(Matcher<? extends Component> matcher) {
        is(new RenderedItemMatcher(matcher));
    }

    private int indexOfFirstItemMatching(final Matcher<? extends Component> matcher) {
        RenderedItemMatcher criteria = new RenderedItemMatcher(matcher);
        is(criteria);
        return criteria.indexOfMatchedItem();
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

    private class RenderedItemMatcher extends TypeSafeMatcher<JList> {
        private final Matcher<? extends Component> matcher;
        private int index = -1;

        public RenderedItemMatcher(Matcher<? extends Component> matcher) {
            this.matcher = matcher;
        }

        public int indexOfMatchedItem() {
            return index;
        }

        @Override
        public boolean matchesSafely(JList component) {
            ListModel model = component.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                Component rendered = renderedCell(component, i, false, false);
                if (matcher.matches(rendered)) {
                    index = i;
                    return true;
                }
            }
            return false;
        }

        public void describeTo(Description description) {
            description.appendDescriptionOf(matcher);
        }
    }
}
