package com.objogate.wl.matcher;

import java.awt.Component;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ComponentOpaqueMatcher extends TypeSafeMatcher<Component> {
    @Override
    public boolean matchesSafely(Component c) {
        return c.isOpaque();
    }

    public void describeTo(Description description) {
        description.appendText("opaque");

    }
}
