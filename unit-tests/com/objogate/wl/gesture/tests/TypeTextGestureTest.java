package com.objogate.wl.gesture.tests;

import javax.swing.JTextField;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JTextFieldDriver;
import com.objogate.wl.driver.tests.AbstractJTextComponentDriverTest;
import com.objogate.wl.driver.tests.Platform;
import com.objogate.wl.driver.tests.Problematic;

public class TypeTextGestureTest extends AbstractJTextComponentDriverTest<JTextFieldDriver> {
    @Before
    public void setUp() throws Exception {
        JTextField textField = new JTextField();
        textField.setColumns(30);
        
        view(textField);
        
        driver = new JTextFieldDriver(gesturePerformer, textField);
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
    @Problematic(platform = Platform.Mac, why = "see below")//easier to show in a comment to avoid escaping
    //text was       '!@$^&*()-_=+[{]};:'\",<.>/`\'
    //text should be '!\"$^&*()-_=+[{]};:'@#~,<.>/`\'
    public void canTypePunctuationAndSymbols() {
        assertCanType("!\"$^&*()-_=+[{]};:'@#~,<.>/`\\");
    }
    
    private void assertCanType(String text) {
        driver.focusWithMouse();
        driver.typeText(text);
        driver.text(equalTo(text));
    }
}
