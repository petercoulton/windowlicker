package com.objogate.wl.swing.driver.tests;

import static com.objogate.wl.swing.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.equalTo;

import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.swing.driver.JTextFieldDriver;

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
        
        String input = SYMBOLS_THAT_ARE_IN_DIFFERENT_PLACES_ON_US_UK_AND_MAC_KEYBOARDS;

        driver.replaceAllText(input);
        driver.hasText(equalTo(input));
    }
}
