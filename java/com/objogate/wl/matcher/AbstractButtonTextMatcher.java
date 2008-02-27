package com.objogate.wl.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import javax.swing.*;

public class AbstractButtonTextMatcher<T extends AbstractButton> extends TypeSafeMatcher<T> {
    private String text;

    public AbstractButtonTextMatcher(String text) {
        this.text = text;
    }

    public boolean matchesSafely(T buttonAlike) {
        return text.equals(buttonAlike.getText());
    }

    public void describeTo(Description description) {
        description.appendText("with text matching '");
        description.appendText(text);
        description.appendText("'");
    }
}
