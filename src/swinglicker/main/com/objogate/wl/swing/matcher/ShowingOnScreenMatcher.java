package com.objogate.wl.swing.matcher;

import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ShowingOnScreenMatcher extends TypeSafeMatcher<Component> {
    @Override
    public boolean matchesSafely(Component component) {
        return component.isShowing();
    }

    public void describeTo(Description description) {
        description.appendText("showing on screen");
    }
}
