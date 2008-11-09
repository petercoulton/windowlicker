package com.objogate.wl.swing.driver;

import java.awt.Adjustable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.InputEvent;

import javax.swing.BoundedRangeModel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import org.hamcrest.Matcher;

import com.objogate.wl.Prober;
import com.objogate.wl.gesture.MouseMoveAbsoluteGesture;
import com.objogate.wl.gesture.MousePressGesture;
import com.objogate.wl.gesture.MouseReleaseGesture;
import com.objogate.wl.swing.ComponentManipulation;
import com.objogate.wl.swing.ComponentSelector;
import com.objogate.wl.swing.gesture.GesturePerformer;
import com.objogate.wl.swing.matcher.ScrollBarMatcher;

public class JScrollbarDriver extends ComponentDriver<JScrollBar> {
    public JScrollbarDriver(ComponentDriver<? extends Container> containerDriver, Matcher<JScrollBar>... matchers) {
        super(containerDriver, JScrollBar.class, matchers);
    }

    @SuppressWarnings("unchecked")
    public JScrollbarDriver(ComponentDriver<? extends Container> containerDriver, JScrollPane pane, int orientation) {
        this(containerDriver, new ScrollBarMatcher(pane, orientation));
    }

    public JScrollbarDriver(GesturePerformer gesturePerformer, ComponentSelector<JScrollBar> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JScrollbarDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JScrollBar> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JScrollbarDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JScrollBar> componentType, Matcher<? super JScrollBar>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void scroll(final int movement) {
        ScrollbarLocationManipulation locationManipulation = new ScrollbarLocationManipulation();
        perform("locating scroll bar", locationManipulation);

        moveMouseToOffset(locationManipulation.getX(), locationManipulation.getY());

        performGesture(new MousePressGesture(InputEvent.BUTTON1_MASK));

        if (locationManipulation.getOrientation() == Adjustable.HORIZONTAL) {
            performGesture(new MouseMoveAbsoluteGesture(Adjustable.HORIZONTAL, movement));
        } else {
            performGesture(new MouseMoveAbsoluteGesture(Adjustable.HORIZONTAL, movement));
        }

        performGesture(new MouseReleaseGesture(InputEvent.BUTTON1_MASK));
    }

    private class ScrollbarLocationManipulation implements ComponentManipulation<JScrollBar> {
        private int x, y;
        private int orientation;

        public void manipulate(JScrollBar component) {
            Dimension minimumSize = component.getUI().getMinimumSize(component);//don't think this is right
            BoundedRangeModel model = component.getModel();
            double min = model.getMinimum();
            double val = model.getValue();
            double extent = model.getExtent();

            double ratio = extent / (model.getMaximum() - min);

            orientation = component.getOrientation();
            if (JScrollBar.HORIZONTAL == orientation) {
                double width = component.getBounds().getWidth();
                double gripperSize = Math.max(ratio * width, minimumSize.width);
                final double midPoint = (gripperSize / 2) + (val * ratio);
                y = component.getHeight() / 2;

                x = (int) midPoint;
            } else {
                double height = component.getBounds().getHeight();
                double gripperSize = Math.max(ratio * height, minimumSize.height);
                double midPoint = (gripperSize / 2) + (val * ratio);
                x = component.getWidth() / 2;

                y = (int) midPoint;
            }
        }

        public int getOrientation() {
            return orientation;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }
}
