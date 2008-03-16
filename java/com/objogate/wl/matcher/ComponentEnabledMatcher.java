package com.objogate.wl.matcher;

import java.awt.Component;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class ComponentEnabledMatcher extends TypeSafeMatcher<Component> {
    @Override
    public boolean matchesSafely(Component c) {
        return c.isEnabled();
    }

    public void describeTo(Description description) {
        description.appendText("enabled");
        
    }
}
