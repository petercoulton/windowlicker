package com.objogate.wl.swing.driver;

import static com.objogate.wl.gesture.Gestures.leftClickMouse;
import static com.objogate.wl.gesture.Gestures.moveMouseTo;
import static com.objogate.wl.swing.gesture.ComponentEdgeTracker.Edge.Bottom;
import static com.objogate.wl.swing.gesture.ComponentEdgeTracker.Edge.Left;
import static com.objogate.wl.swing.gesture.ComponentEdgeTracker.Edge.Right;
import static com.objogate.wl.swing.gesture.ComponentEdgeTracker.Edge.Top;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import java.awt.Component;

import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.objogate.wl.Prober;
import com.objogate.wl.swing.ComponentManipulation;
import com.objogate.wl.swing.ComponentSelector;
import com.objogate.wl.swing.gesture.ComponentEdgeTracker;
import com.objogate.wl.swing.gesture.GesturePerformer;

public class JSliderDriver extends ComponentDriver<JSlider> {
    public JSliderDriver(GesturePerformer gesturePerformer, ComponentSelector<JSlider> jSliderComponentSelector, Prober prober) {
        super(gesturePerformer, jSliderComponentSelector, prober);
    }

    public JSliderDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JSlider> jSliderComponentSelector) {
        super(parentOrOwner, jSliderComponentSelector);
    }

    public JSliderDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JSlider> componentType, Matcher<? super JSlider>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void hasValue(final Matcher<Integer> matcher) {
        is(new TypeSafeMatcher<JSlider>() {
            @Override
            public boolean matchesSafely(JSlider jSlider) {
                return !jSlider.getValueIsAdjusting() && matcher.matches(jSlider.getValue());
            }

            public void describeTo(Description description) {
                description.appendText("slider is not adjusting & has value matching ");
                description.appendDescriptionOf(matcher);
            }
        });
    }

    public void hasMaximum(final Matcher<Integer> matcher) {
        is(new TypeSafeMatcher<JSlider>() {
            @Override
            public boolean matchesSafely(JSlider jSlider) {
                return matcher.matches(jSlider.getMaximum());
            }

            public void describeTo(Description description) {
                description.appendText("slider has maximum matching ");
                description.appendDescriptionOf(matcher);
            }
        });
    }

    public void hasMinimum(final Matcher<Integer> matcher) {
        is(new TypeSafeMatcher<JSlider>() {
            @Override
            public boolean matchesSafely(JSlider jSlider) {
                return matcher.matches(jSlider.getMinimum());
            }

            public void describeTo(Description description) {
                description.appendText("slider minimum matching ");
                description.appendDescriptionOf(matcher);
            }
        });
    }

    public void makeValue(int value) {
        hasMaximum(greaterThanOrEqualTo(value));
        hasMinimum(lessThanOrEqualTo(value));

        int currentValue = currentValue();
        int count = 0;
        while (count++ < 100 && currentValue != value) {
            if (currentValue < value) {
                increment();
            } else {
                decrement();
            }
            currentValue = currentValue();
        }
        hasValue(equalTo(value));
    }

    private int currentValue() {
        SliderValue sliderValue = new SliderValue();
        perform("get value", sliderValue);
        return sliderValue.getValue();
    }

    private int orientation() {
        SliderOrientation orientation = new SliderOrientation();
        perform("get orientation", orientation);
        return orientation.getOrientation();
    }

    private boolean inverted() {
        SliderInversion inversion = new SliderInversion();
        perform("get inversion", inversion);
        return inversion.getInversion();
    }

    public void increment() {
        if (inverted()) {
            clickBottomOrLeft();
        } else {
            clickTopOrRight();
        }
    }

    public void decrement() {
        if (inverted()) {
            clickTopOrRight();
        } else {
            clickBottomOrLeft();
        }
    }

    private void clickTopOrRight() {
        switch (orientation()) {
            case SwingConstants.HORIZONTAL:
                performGesture(moveMouseTo(new ComponentEdgeTracker(prober(), component(), Right, -1)));
                break;
            case SwingConstants.VERTICAL:
                performGesture(moveMouseTo(new ComponentEdgeTracker(prober(), component(), Top, 1)));
                break;
            default:
                throw new IllegalStateException("Only can handle Horizontal and Vertical Components!");
        }
        performGesture(leftClickMouse());
    }

    private void clickBottomOrLeft() {
        switch (orientation()) {
            case SwingConstants.HORIZONTAL:
                performGesture(moveMouseTo(new ComponentEdgeTracker(prober(), component(), Left, 1)));
                break;
            case SwingConstants.VERTICAL:
                performGesture(moveMouseTo(new ComponentEdgeTracker(prober(), component(), Bottom, -1)));
                break;
            default:
                throw new IllegalStateException("Only can handle Horizontal and Vertical Components!");
        }

        performGesture(leftClickMouse());
    }

    private static class SliderValue implements ComponentManipulation<JSlider> {
        private int value;

        public void manipulate(JSlider component) {
            this.value = component.getValue();
        }

        public int getValue() {
            return value;
        }
    }

    private static class SliderOrientation implements ComponentManipulation<JSlider> {
        private int orientation;

        public void manipulate(JSlider component) {
            this.orientation = component.getOrientation();
        }

        public int getOrientation() {
            return orientation;
        }
    }

    private static class SliderInversion implements ComponentManipulation<JSlider> {
        private boolean inversion;

        public void manipulate(JSlider component) {
            this.inversion = component.getInverted();
        }

        public boolean getInversion() {
            return inversion;
        }
    }


}
