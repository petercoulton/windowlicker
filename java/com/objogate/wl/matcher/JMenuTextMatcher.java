package com.objogate.wl.matcher;

import javax.swing.JMenu;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class JMenuTextMatcher extends TypeSafeMatcher<JMenu> {
    private final String expectedText;

    public JMenuTextMatcher(String expectedText) {
        this.expectedText = expectedText;
    }

    @Override
    public boolean matchesSafely(JMenu jButton) {
        // Note that getText can seem to return null
        return expectedText.equals(jButton.getText());
    }

    public void describeTo(Description description) {
        description.appendText("JMenu with text '").appendText(expectedText).appendText("'");
    }
}
