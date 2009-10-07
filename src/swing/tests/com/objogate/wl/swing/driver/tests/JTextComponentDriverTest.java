package com.objogate.wl.swing.driver.tests;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import static org.hamcrest.Matchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.swing.driver.JTextComponentDriver;
import static com.objogate.wl.swing.probe.ComponentIdentity.selectorFor;

public class JTextComponentDriverTest extends AbstractJTextComponentDriverTest<JTextComponentDriver<JTextComponent>> {
    JTextField textField = new JTextField("");

    @Before
    public void setUp() throws Exception {
        textField.setColumns(30);

        view(textField);

        driver = new JTextComponentDriver<JTextComponent>(gesturePerformer, selectorFor((JTextComponent)textField), prober);
    }
    
    @Test
    public void testReplaceAllText() throws Exception {
        setText("replace me");

        driver.replaceAllText("ok");
        
        driver.hasText(equalTo("ok"));
    }

    @Test
    public void testSelectAll() throws Exception {
        setText("select all");

        driver.leftClickOnComponent();
        driver.selectAll();

        driver.hasSelectedText(equalTo("select all"));
    }

    @Test
    public void testCutAndPaste() {
        setText("cut and paste");
        
        driver.focusWithMouse();
        driver.selectAll();
        driver.cut();

        driver.isEmpty();
        
        driver.paste();
        driver.hasText(equalTo("cut and paste"));
    }
    
    @Test
    public void testCopyAndPaste() {
        setText("original text");
        
        driver.focusWithMouse();
        driver.selectAll();
        
        driver.copy();
        driver.replaceAllText("this will be replaced");
        driver.hasText("this will be replaced");
        
        driver.selectAll();
        driver.paste();
        driver.hasText(equalTo("original text"));
    }
}
