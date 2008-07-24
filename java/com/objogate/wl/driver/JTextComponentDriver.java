package com.objogate.wl.driver;

import static java.awt.event.KeyEvent.VK_DELETE;
import static com.objogate.wl.gesture.Gestures.repeat;
import static com.objogate.wl.gesture.Gestures.type;
import static com.objogate.wl.gesture.Gestures.typeKey;
import static com.objogate.wl.gesture.Gestures.whileHoldingKey;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

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
import com.objogate.wl.internal.query.TextQuery;

public class JTextComponentDriver<T extends JTextComponent> extends ComponentDriver<T> implements TextQuery {
    public JTextComponentDriver(GesturePerformer gesturePerformer, ComponentSelector<T> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JTextComponentDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<T> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JTextComponentDriver(ComponentDriver<? extends Component> parentOrOwner, Class<T> componentType, Matcher<? super T>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void hasText(String expectedText) {
        hasText(equalTo(expectedText));
    }

    public void hasText(Matcher<String> matcher) {
        has(new Query<T, String>() {
            public String query(T component) {
                return component.getText();
            }

            public void describeTo(Description description) {
                description.appendText("text");
            }
        }, matcher);
    }

    public void hasSelectedText(String expected) {
        hasSelectedText(equalTo(expected));
    }

    public void hasSelectedText(Matcher<String> matcher) {
        has(new Query<T, String>() {
            public String query(T component) {
                return component.getSelectedText();
            }

            public void describeTo(Description description) {
                description.appendText("selected text");
            }
        }, matcher);
    }

    public void replaceAllText(String text) {
        moveMouseToCenter();
        selectAll();
        typeText(text);
    }

    public void typeText(String text) {
        performGesture(type(text));
    }
    
    public void pressReturn() {
        typeText("\n");
    }

    public void deleteSelectedText() {
        typeKey(VK_DELETE);
    }

    public void replaceText(TextOccurence textOccurence, String replacement) {
        selectText(textOccurence);

        typeText(replacement);
    }

    public void selectText(TextOccurence textOccurence) {
        moveMouseToCenter();
        performGesture(Gestures.leftClickMouse());


        TextSearchMatcher search = new TextSearchMatcher(textOccurence);
        is(search);
        int start = search.getStart();

        moveCaretTo(start);

        int repetitions = search.getEnd() - start;
        performGesture(whileHoldingKey(KeyEvent.VK_SHIFT,
                repeat(repetitions, typeKey(KeyEvent.VK_RIGHT))));

    }

    public void moveCaretTo(int targetPosition) {

        int currentPosition = getCaretPosition();

        if (currentPosition < targetPosition) {
            performGesture(repeat(targetPosition - currentPosition, typeKey(KeyEvent.VK_RIGHT)));
        } else {
            performGesture(repeat(currentPosition - targetPosition, typeKey(KeyEvent.VK_LEFT)));
        }
    }

    public int getCaretPosition() {
        CarentPositionManipulation positionManipulation = new CarentPositionManipulation();
        perform("caret position", positionManipulation);
        return positionManipulation.getPosition();
    }

    public static TextOccurence occurence(int count) {
        return new TextOccurence(count);
    }

    public void isEmpty() {
        hasText(equalTo(""));
    }

    public void focusWithMouse() {
        leftClickOnComponent();
    }

    public void clearText() {
        selectAll();
        deleteSelectedText();
    }

    public static class TextOccurence {
        private final int count;
        private String text;

        public TextOccurence(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public String getText() {
            return text;
        }

        public TextOccurence of(String s) {
            this.text = s;
            return this;
        }
    }

    protected static class TextWidthManipulation implements ComponentManipulation<JTextComponent> {
        private int width;
        private final int length;
        private final int startIndex;

        public TextWidthManipulation(int startIndex, int length) {
            this.startIndex = startIndex;
            this.length = length;
        }

        public void manipulate(JTextComponent component) {
            try {
                String text = component.getText(startIndex, length);
                Font font = component.getFont();
                width = component.getFontMetrics(font).stringWidth(text);
            } catch (BadLocationException e) {
                throw new Defect("Cannot get text width", e);
            }
        }

        public int getWidth() {
            return width;
        }
    }

    private static class CarentPositionManipulation implements ComponentManipulation<JTextComponent> {
        private int position;

        public void manipulate(JTextComponent component) {
            position = component.getCaretPosition();
        }

        public int getPosition() {
            return position;
        }
    }

    public static class TextSearchMatcher extends TypeSafeMatcher<JTextComponent> {
        private final int occurence;
        private final String text;
        private int start;
        private int end;


        public TextSearchMatcher(JTextFieldDriver.TextOccurence textOccurence) {
            occurence = textOccurence.getCount();
            text = textOccurence.getText();
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        @Override
        public boolean matchesSafely(JTextComponent component) {
            String s = component.getText();
            int lastIndex = 0;
            for (int i = 0; i < occurence; i++) {
                int index = s.indexOf(text, lastIndex);

                if(index == -1)
                    return false;

                if (i == occurence - 1)
                    lastIndex = index;
                else
                    lastIndex = index + text.length();
            }

            this.start = lastIndex;
            this.end = start + text.length();

            return true;
        }

        public void describeTo(Description description) {
            description.appendText("occurence " + occurence + " of " + text);
        }
    }
}
