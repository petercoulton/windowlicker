package com.objogate.wl.driver.tests;

import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import com.objogate.wl.driver.JTextComponentDriver;

public class JTextComponentDriverTest extends AbstractJTextComponentDriverTest<JTextComponentDriver<JTextComponent>> {
    JTextField textField = new JTextField("");

    @Before
    public void setUp() throws Exception {
        textField.setColumns(30);

        view(textField);

        driver = new JTextComponentDriver<JTextComponent>(gesturePerformer, textField, prober);
    }

    @Test
    public void testReplaceAllText() throws Exception {
        setText("replace me");

        driver.replaceAllText("ok");

        driver.hasText(Matchers.equalTo("ok"));
    }

    @Test
    public void testSelectAll() throws Exception {
        setText("select all");

        driver.leftClickOnComponent();
        driver.selectAll();

        driver.hasSelectedText(Matchers.equalTo("select all"));
    }

    @Test
    public void testCutAndPaste() {
        setText("cut and paste");

        driver.leftClickOnComponent();
        driver.selectAll();

        driver.cut();

        driver.hasSelectedText(Matchers.nullValue(String.class));

        driver.paste();

        driver.hasText(Matchers.equalTo("cut and paste"));
    }

    @Test
    public void testCopyAndPaste() {
        setText("copy and paste");

        driver.leftClickOnComponent();
        driver.selectAll();

        driver.copy();

        driver.replaceAllText("");

        driver.paste();

        driver.hasText(Matchers.equalTo("copy and paste"));
    }

    @Test
    public void testSelectingTextUsingTheKeyboard() {
        setText("the good the bad the ugly and the smelly");

        driver.selectText(occurence(3).of("the"));

        driver.selectionStartsAt(17);
    }

    @Test
    public void testReplacingTextUsingTheKeyboard() {
        setText("the quick brown fox jumps over the the dog");

        driver.replaceText(occurence(3).of("the"), "lazy");

        driver.hasText("the quick brown fox jumps over the lazy dog");
    }
}