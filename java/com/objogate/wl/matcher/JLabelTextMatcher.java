package com.objogate.wl.matcher;

import static org.hamcrest.Matchers.equalTo;

import javax.swing.JLabel;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class JLabelTextMatcher extends TypeSafeMatcher<JLabel> {
    private final Matcher<String> matcher;

    public JLabelTextMatcher(Matcher<String> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matchesSafely(JLabel label) {
        return matcher.matches(label.getText());
    }

    public void describeTo(Description description) {
        description.appendText("with text ")
                .appendDescriptionOf(matcher);
    }

    public static JLabelTextMatcher withLabelText(String text) {
      return withLabelText(equalTo(text));
    }

    public static JLabelTextMatcher withLabelText(Matcher<String> text) {
        return new JLabelTextMatcher(text);
    }
}
