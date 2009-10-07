package com.objogate.wl.swing.driver.tests;

import javax.swing.JTextField;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.swing.driver.JTextFieldDriver;
import static com.objogate.wl.swing.probe.ComponentIdentity.selectorFor;

public class JTextFieldDriverTest extends AbstractJTextComponentDriverTest<JTextFieldDriver> {
    private static final String INITIAL_TEXT = "initial text";
    private static final String SYMBOLS_THAT_ARE_IN_DIFFERENT_PLACES_ON_US_UK_AND_MAC_KEYBOARDS = "#@";

    @Before
    public void setUp() throws Exception {
        JTextField textField = new JTextField(INITIAL_TEXT);
        textField.setColumns(30);

        view(textField);

        driver = new JTextFieldDriver(gesturePerformer, selectorFor(textField), prober);
    }

    @Test
    public void canTypeTextIntoAnEmptyTextField() {
        setText("");

        driver.replaceAllText("hello");
        driver.hasText(equalTo("hello"));
    }

    @Test
    public void canReplaceTextInTheTextField() {
        setText("bananas");

        driver.replaceAllText("hello");
        driver.hasText(equalTo("hello"));
    }

    @Test
    public void canClearTheTextField() {
        setText("pineapples");

        driver.focusWithMouse();
        driver.clearText();
        driver.isEmpty();
    }

    @Test
    public void canTypeSymbolsIntoTextField() {
        setText("");

        assertCanType(SYMBOLS_THAT_ARE_IN_DIFFERENT_PLACES_ON_US_UK_AND_MAC_KEYBOARDS);
    }

    @Test
    public void canTypeLowerCase() {
        assertCanType("hello");
    }

    @Test
    public void canTypeUpperCase() {
        assertCanType("HELLO");
    }

    @Test
    public void canTypeSpaces() {
        assertCanType("hello world");
    }

    @Test
    public void canTypeNumbers() {
        assertCanType("0123456789");
    }

    @Test
    public void canTypePunctuationAndSymbols() {
        assertCanType("!\"$^&*()-_=+[{]};:'@#~,<.>/`\\");
    }

    private void assertCanType(String text) {
        setText("");
        driver.focusWithMouse();
        driver.typeText(text);
        driver.hasText(equalTo(text));
    }
}
