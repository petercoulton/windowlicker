package com.objogate.wl.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.awt.Component;

public class ShowingOnScreenMatcher extends TypeSafeMatcher<Component> {
    @Override public boolean matchesSafely(Component component) {
        return component.isShowing();
    }

    public void describeTo(Description description) {
        description.appendText("showing on screen");
    }
}
