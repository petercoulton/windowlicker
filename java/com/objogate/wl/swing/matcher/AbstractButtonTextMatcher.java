package com.objogate.wl.swing.matcher;

import javax.swing.AbstractButton;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class AbstractButtonTextMatcher<T extends AbstractButton> extends TypeSafeMatcher<T> {
    private String text;

    public AbstractButtonTextMatcher(String text) {
        this.text = text;
    }

    @Override
    public boolean matchesSafely(T buttonAlike) {
        return text.equals(buttonAlike.getText());
    }

    public void describeTo(Description description) {
        description.appendText("with text matching '");
        description.appendText(text);
        description.appendText("'");
    }
}
