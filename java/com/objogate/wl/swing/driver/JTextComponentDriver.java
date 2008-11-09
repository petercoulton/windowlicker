package com.objogate.wl.swing.driver;

import static com.objogate.wl.gesture.Gestures.type;
import static com.objogate.wl.gesture.Gestures.typeKey;
import static java.awt.event.KeyEvent.VK_DELETE;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Component;
import java.awt.Font;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import com.objogate.exception.Defect;
import com.objogate.wl.Prober;
import com.objogate.wl.Query;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.swing.ComponentManipulation;
import com.objogate.wl.swing.ComponentSelector;
import com.objogate.wl.swing.internal.query.TextQuery;

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

}
