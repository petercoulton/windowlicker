package com.objogate.wl.matcher;

import java.awt.Component;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class DisplayableComponentMatcher extends TypeSafeMatcher<Component> {
    public void describeTo(Description description) {
        description.appendText("is displayable");
    }

    public boolean matchesSafely(Component component) {
        return component.isDisplayable();
    }
}
