package com.objogate.wl.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.awt.Color;

public class ColorMatcher extends TypeSafeMatcher<Color> {
    private final Color expected;

    public ColorMatcher(Color expected) {
        this.expected = expected;
    }

    public boolean matchesSafely(Color item) {
        return expected.equals(item);
    }

    public void describeTo(Description description) {
        description.appendText("color matching " + expected);
    }
}
