package com.objogate.wl.gesture.tests;

import static com.objogate.wl.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.equalTo;

import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.driver.JTextFieldDriver;
import com.objogate.wl.driver.tests.AbstractJTextComponentDriverTest;

public class TypeTextGestureTest extends AbstractJTextComponentDriverTest<JTextFieldDriver> {
    @Before
    public void setUp() throws Exception {
        JTextField textField = new JTextField();
        textField.setColumns(30);
        
        view(textField);
        
        driver = new JTextFieldDriver(gesturePerformer, selectorFor(textField), prober);
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
        driver.focusWithMouse();
        driver.typeText(text);
        driver.text(equalTo(text));
    }
}
