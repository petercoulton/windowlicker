package com.objogate.wl.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.swing.JLabel;
import java.awt.Component;

public class JLabelTextMatcher extends TypeSafeMatcher<Component> {
    private final Matcher<String> matcher;

    public JLabelTextMatcher(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matchesSafely(Component component) {
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            return matcher.matches(label.getText());
        }

        return false;
    }

    public void describeTo(Description description) {
        description.appendText("JLabel with ").appendText(matcher.toString());
    }
}
