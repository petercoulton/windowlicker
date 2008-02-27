package com.objogate.wl.matcher;

import javax.swing.JButton;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ButtonTextMatcher extends TypeSafeMatcher<JButton> {
    private final String expectedText;

    public ButtonTextMatcher(String expectedText) {
        this.expectedText = expectedText;
    }

    /* Note that getText can seem to return null */
    public boolean matchesSafely(JButton jButton) {
        return expectedText.equals(jButton.getText());
    }

    public void describeTo(Description description) {
        description.appendText("JButton with text '").appendText(expectedText).appendText("'");
    }
}
