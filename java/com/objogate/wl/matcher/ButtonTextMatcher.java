package com.objogate.wl.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import javax.swing.JButton;

public class ButtonTextMatcher extends TypeSafeMatcher<JButton> {
    private final String expectedText;

    public ButtonTextMatcher(String expectedText) {
        this.expectedText = expectedText;
    }

    public boolean matchesSafely(JButton jButton) {
        return jButton.getText().equals(expectedText);
    }

    public void describeTo(Description description) {
        description.appendText("JButton with text '").appendText(expectedText).appendText("'");
    }
}
