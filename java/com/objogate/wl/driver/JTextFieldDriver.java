package com.objogate.wl.driver;

import static com.objogate.wl.gesture.Gestures.BUTTON1;
import static com.objogate.wl.gesture.Gestures.clickMouseButton;
import static com.objogate.wl.gesture.Gestures.moveMouseTo;
import static com.objogate.wl.gesture.Gestures.translateMouseTo;
import static com.objogate.wl.gesture.Gestures.whileHoldingMouseButton;

import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;

import javax.swing.JTextField;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.matchers.TypeSafeMatcher;

import com.objogate.wl.ComponentManipulation;
import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.gesture.Gestures;
import com.objogate.wl.gesture.Tracker;

public class JTextFieldDriver extends JTextComponentDriver<JTextField> {
    public JTextFieldDriver(GesturePerformer gesturePerformer, ComponentSelector<JTextField> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JTextFieldDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JTextField> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JTextFieldDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JTextField> componentType, Matcher<? super JTextField>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void doubleClickText(TextOccurence textOccurence) {
        TextSearchMatcher search = new TextSearchMatcher(textOccurence);
        is(search);
        int end = search.getEnd();

        int scrollOffset = scrollPositionIntoView(end);

        final int x = getTextWidth(0, end - 1) + getInsets().left - scrollOffset - 1;
        final int y = ((int) screenBounds().getHeight() / 2);

        moveMouseToOffset(x, y);
        performGesture(Gestures.doubleClickMouse());
    }

    /**
     * not yet working if selecting involves scrolling the text field
     */
    public void selectWithMouse(TextOccurence textOccurence) {
        TextSearchMatcher search = new TextSearchMatcher(textOccurence);
        is(search);
        final int textStartIndex = search.getStart();
        final int textEndIndex = search.getEnd();

        int scrollOffset = scrollPositionIntoView(textStartIndex + 1);

        final int x = getTextWidth(0, textStartIndex) + getInsets().left - scrollOffset;
        final int y = ((int) screenBounds().getHeight() / 2);

        moveMouseToOffset(x, y);

        performGesture(whileHoldingMouseButton(Gestures.BUTTON1,
                translateMouseTo(new Tracker() {
                    public Point target(Point currentLocation) {
                        SelectionManipulation selectionManipulation = new SelectionManipulation();
                        perform("selection", selectionManipulation);

                        int direction = selectionManipulation.selectionEnd > textEndIndex ? -1 : 1;

                        if(!screenBounds().contains(currentLocation)) {///oops, we're off the edge... this should get handled below but for some reason it doesn't quite work
                            return translateHorizontally(currentLocation, direction, 10);
                        }

                        int numberOfCharactersToMove = selectionManipulation.selectionEnd - textEndIndex;

                        //if we're more than 2 characters away move quickly (5 at a time), else move slowly
                        int amount = Math.abs(numberOfCharactersToMove) > 2 ? 5 : 1;

                        if (numberOfCharactersToMove != 0) {
                            return translateHorizontally(currentLocation, direction, amount);
                        }
                        return currentLocation;
                    }

                    private Point translateHorizontally(Point currentLocation, int direction, int amount) {
                        Point desiredLocation = new Point(currentLocation);
                        desiredLocation.translate(direction * amount, 0);
                        return desiredLocation;
                    }

                })
        ));
    }


    public Insets getInsets() {
        ComponentInsetsManipulation componentInsetsManipulation = new ComponentInsetsManipulation();
        perform("component insets", componentInsetsManipulation);
        return componentInsetsManipulation.getInsets();
    }

    @Override
    public void moveCaretTo(final int position) {
        perform("moving caret", new ComponentManipulation<JTextField>() {
            public void manipulate(JTextField component) {
                component.setCaretPosition(position);
            }
        });
    }

    private int getTextWidth(int startIndex, int endIndex) {
        TextWidthManipulation widthManipulation = new TextWidthManipulation(startIndex, endIndex);
        perform("Text width", widthManipulation);
        return widthManipulation.getWidth();
    }

    /**
     * bodge to scroll the text field so the word is in view - would normally do this with the keyboard or mouse
     */
    private int scrollPositionIntoView(int start) {
        moveCaretTo(0);
        moveCaretTo(start);

        OffsetManipulation offsetManipulation = new OffsetManipulation();
        perform("get scroll offset", offsetManipulation);
        return offsetManipulation.getOffset();
    }

    public void focusWithMouse() {
        performGesture(moveMouseTo(centerOfComponent()), clickMouseButton(BUTTON1));
    }

    public void caretPositionIs(final int caretPosition) {
        is(new TypeSafeMatcher<JTextField>() {
            public boolean matchesSafely(JTextField item) {
                return item.getCaretPosition() == caretPosition;
            }

            public void describeTo(Description description) {
                description.appendText("caret position");
            }
        });
    }

    private static class ComponentInsetsManipulation implements ComponentManipulation<JTextField> {
        private Insets insets;

        public void manipulate(JTextField component) {
            this.insets = component.getInsets();
        }

        public Insets getInsets() {
            return insets;
        }
    }

    private static class OffsetManipulation implements ComponentManipulation<JTextField> {
        public int offset;

        public void manipulate(JTextField component) {
            offset = component.getScrollOffset();

        }

        public int getOffset() {
            return offset;
        }
    }

    private static class SelectionManipulation implements ComponentManipulation<JTextField> {
        int selectionStart;
        int selectionEnd;

        public void manipulate(JTextField component) {
            selectionStart = component.getSelectionStart();
            selectionEnd = component.getSelectionEnd();
        }
    }
}