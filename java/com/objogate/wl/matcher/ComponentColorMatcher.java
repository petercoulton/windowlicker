package com.objogate.wl.matcher;

import java.awt.Color;
import java.awt.Component;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public abstract class ComponentColorMatcher extends TypeSafeMatcher<Component> {
    protected Color color;

    public ComponentColorMatcher(Color color) {
        this.color = color;
    }

    public boolean matchesSafely(Component component) {
        return colorOf(component).equals(color);
    }

    protected abstract Color colorOf(Component component);

    public void describeTo(Description description) {
        description.appendText("with foreground ");
        description.appendValue(color);
    }
}
