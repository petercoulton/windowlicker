package com.objogate.wl.driver.tests;

import static com.objogate.wl.probe.ComponentIdentity.selectorFor;
import static org.hamcrest.Matchers.equalTo;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import com.objogate.wl.driver.JTextFieldDriver;

public class JTextFieldDriverTest extends AbstractJTextComponentDriverTest<JTextFieldDriver> {
    private static final String INITIAL_TEXT = "initial text";

    @Before
    public void setUp() throws Exception {
        JTextField textField = new JTextField(INITIAL_TEXT);
        textField.setColumns(30);

        view(textField);

        driver = new JTextFieldDriver(gesturePerformer, selectorFor(textField), prober);
    }

    @Test
    //todo really bad error message when this does wrong
    public void assertCaretposition() {
        setText("this is a test");

        driver.moveCaretTo(0);
        driver.caretPositionIs(0);

        driver.moveCaretTo(6);
        driver.caretPositionIs(6);
    }

    @Test
    public void canTypeTextIntoTextField() {
        setText("");
        driver.replaceAllText("hello");
        driver.text(equalTo("hello"));
    }

    @Test
    public void canTypeSymbolsIntoTextField() {
        setText("");
        driver.replaceAllText("#@"); // these symbols are in different places on US, UK (and mac!) keyboards
        driver.text(equalTo("#@"));
    }

    @Test
    public void testDoubleClickingOfAWordToSelectIt() {
        setText("sheep my cheese");

        driver.doubleClickText(occurence(1).of("my"));

        driver.selectionStartsAt(6);
        driver.hasSelectedText("my");
    }

    @Test
    public void testDoubleClickingOnWordThatIsOffScreenDueToTheSizeOfTheComponent() throws Exception {
        setText("a really long sentence that cannot possibly fit into the text field, its so long i've got bored of typing so i'll stop now.");

        driver.doubleClickText(occurence(1).of("bored"));

        driver.hasSelectedText("bored");
        driver.selectionStartsAt(90);
    }

    @Test
    public void testCanSelectTextByDraggingTheMouse() throws Exception {
        setText("my monkey is more better than yours");

        driver.selectWithMouse(occurence(1).of("more better than"));

        driver.selectionStartsAt(13);
        driver.hasSelectedText("more better than");
    }

    @Test
    public void testCanSelectTextByDraggingTheMouseWhenItForcesTheTextFieldToScroll() {
        setText("a really long sentence that cannot possibly fit into the text field, its so long i've got bored of typing so i'll stop now.");
                       
        driver.selectWithMouse(occurence(1).of("its so long i've got bored of typing"));

        driver.hasSelectedText("its so long i've got bored of typing");
    }

    @Test
    public void testMouseSelectionWithArbitaryFont() {
        String font = setRandomFont();
        pack();
        setText("testing selection with font " + font + " and some stuff after");

        driver.selectWithMouse(occurence(1).of(font));

        driver.hasSelectedText(font);
    }

    private String setRandomFont() {
        Font font = randomFont();
        String text = font.toString();
        setFont(font);
        return text;
    }

    private Font randomFont() {
        Font[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        return fonts[0].deriveFont(20f);
    }
}
