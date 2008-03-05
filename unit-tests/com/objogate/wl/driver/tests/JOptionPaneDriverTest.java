package com.objogate.wl.driver.tests;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.not;
import org.junit.Test;
import com.objogate.wl.driver.AbstractButtonDriver;
import com.objogate.wl.driver.JOptionPaneDriver;
import static com.objogate.wl.matcher.ComponentMatchers.showingOnScreen;

public class JOptionPaneDriverTest extends AbstractComponentDriverTest<JOptionPaneDriver> {
    private Object returnValue;

    @Test
    public void testShowMessageDialog() throws Exception {
        view(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "alert", "alert", JOptionPane.ERROR_MESSAGE);
            }
        });

        driver = new JOptionPaneDriver(frameDriver, JOptionPane.class);

        driver.clickOK();

        driver.is(not(showingOnScreen()));
    }

    @Test
    public void testShowConfirmDialog() throws Exception {
        view(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                returnValue = JOptionPane.showConfirmDialog(frame, "choose one", "choose one", JOptionPane.YES_NO_OPTION);
            }
        });

        driver = new JOptionPaneDriver(frameDriver, JOptionPane.class);

        driver.clickYes();

        driver.is(not(showingOnScreen()));

        assertEquals(JOptionPane.YES_OPTION, returnValue);
    }

    @Test
    public void testShowOptionDialog() throws Exception {
        view(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"OK", "CANCEL", "Boo!"};
                returnValue = JOptionPane.showOptionDialog(frame, "Click OK to continue", "Warning",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                        null, options, options[0]);
            }
        });

        driver = new JOptionPaneDriver(frameDriver, JOptionPane.class);

        driver.clickButtonWithText("CANCEL");

        driver.is(not(showingOnScreen()));

        assertEquals(1, returnValue);
    }

    @Test
    public void testShowInputDialog() throws Exception {
        view(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                returnValue = JOptionPane.showInputDialog(frame, "Please input a value");
            }
        });

        driver = new JOptionPaneDriver(frameDriver, JOptionPane.class);

        driver.typeText("hello");
        driver.clickOK();

        driver.is(not(showingOnScreen()));

        assertEquals("hello", returnValue);
    }

    @Test
    public void testShowInputDialogWithMultipleAllowedValues() throws Exception {
        final String wantedValue = "two";
        view(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                returnValue = JOptionPane.showInputDialog(frame, "Please input a value", "Title", JOptionPane.INFORMATION_MESSAGE, null,
                        new String[] { "one", wantedValue, "three"}, "one");
            }
        });

        driver = new JOptionPaneDriver(frameDriver, JOptionPane.class);

        driver.selectValue(wantedValue);

        driver.is(not(showingOnScreen()));

        assertEquals(wantedValue, returnValue);
    }

    @Test
    public void testShowInputDialogWithALargeNumberOfAllowedValues() throws Exception {

        final String[] values = new String[50];
        for ( int i = 0 ; i < values.length; i++ ) {
            values[i] = String.valueOf(i);
        }

        view(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                returnValue = JOptionPane.showInputDialog(frame, "Please input a value", "Title", JOptionPane.INFORMATION_MESSAGE, null,
                        values, values[0]);
            }
        });

        driver = new JOptionPaneDriver(frameDriver, JOptionPane.class);

        String wantedValue = "25";

        driver.selectValue(wantedValue);

        driver.is(not(showingOnScreen()));

        assertEquals(wantedValue, returnValue);
    }


    private void view(AbstractAction action) {
        JButton button = new JButton(action);
        button.setText("click me to run test");

        view(button);

        new AbstractButtonDriver<JButton>(gesturePerformer, button, prober).click();
    }

}
