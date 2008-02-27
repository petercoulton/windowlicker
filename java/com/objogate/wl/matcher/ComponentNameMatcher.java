package com.objogate.wl.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.awt.*;

public class ComponentNameMatcher extends TypeSafeMatcher<Component> {
    private final String name;

    public ComponentNameMatcher(String name) {
        this.name = name;
    }

    public boolean matchesSafely(Component c) {
        String name = c.getName();
        return name != null && name.equals(this.name);
    }

    public void describeTo(Description description) {
        description.appendText("named ").appendValue(name);
    }
}
