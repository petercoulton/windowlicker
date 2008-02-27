package com.objogate.wl.driver;

import javax.swing.JList;
import javax.swing.ListModel;
import java.awt.Component;
import java.awt.Rectangle;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import com.objogate.exception.Defect;
import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.ComponentQuery;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.Gestures;

public class JListDriver extends ComponentDriver<JList> {

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
        has("selected index", new ComponentQuery<JList, Integer>() {
            public Integer query(JList component) {
                return component.getSelectedIndex();
            }
        }, Matchers.equalTo(expectedIndex));
    }

    public void selectItem(Matcher<Component> matcher) {
        selectItem(firstItemMatching(matcher));
    }

    public void selectItem(Object o) {
        selectItem(indexOf(o));
    }

    public void selectItem(int index) {
        mouseOverCell(index);
        performGesture(Gestures.leftClickMouse());
    }

    private int firstItemMatching(final Matcher<Component> matcher) {
        RenderedElementFinderManipulation elementFinderManipulation = new RenderedElementFinderManipulation(matcher);
        perform("serch for element", elementFinderManipulation);
        return elementFinderManipulation.getIndex();
    }

    private int indexOf(final Object o) {
        ElementIndexManipulation elementIndexManipulation = new ElementIndexManipulation(o);
        perform("search for element", elementIndexManipulation);
        return elementIndexManipulation.getIndex();
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

    private static class ElementIndexManipulation implements ComponentManipulation<JList> {
        private int index;
        private final Object o;

        public ElementIndexManipulation(Object o) {
            this.o = o;
        }

        public void manipulate(JList component) {
            ListModel model = component.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                Object element = model.getElementAt(i);
                if (element == o || element.equals(o)) {
                    index = i;
                    return;
                }
            }

            throw new Defect("Cannot find element " + o);
        }

        public int getIndex() {
            return index;
        }
    }

    private class RenderedElementFinderManipulation implements ComponentManipulation<JList> {
        private int index;
        private final Matcher<Component> matcher;

        public RenderedElementFinderManipulation(Matcher<Component> matcher) {
            this.matcher = matcher;
        }

        public void manipulate(JList component) {
            ListModel model = component.getModel();
            for (int i = 0; i < model.getSize(); i++) {
                Component rendered = renderedCell(component, i, false, false);
                if(matcher.matches(rendered)) {
                    index = i;
                    return;
                }
            }

            throw new Defect("Cannot find component matching " + matcher);
        }

        public int getIndex() {
            return index;
        }
    }
}
