package com.objogate.wl.swing.matcher;

import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ComponentNameMatcher extends TypeSafeMatcher<Component> {
    private final String name;

    public ComponentNameMatcher(String name) {
        this.name = name;
    }

    @Override
    public boolean matchesSafely(Component other) {
        return name.equals(other.getName());
    }

    public void describeTo(Description description) {
        description.appendText("named ").appendValue(name);
    }
}
