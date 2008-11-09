package com.objogate.wl.swing.matcher;

import java.util.Collection;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class EmptyCollectionMatcher<C extends Collection<?>> extends TypeSafeMatcher<C> {
    @Override
    public boolean matchesSafely(C c) {
        return c.isEmpty();
    }

    public void describeTo(Description description) {
        description.appendText("an empty collection");
    }

    @Factory
    public static <C extends Collection<?>> Matcher<C> isEmpty() {
        return new EmptyCollectionMatcher<C>();
    }
}
