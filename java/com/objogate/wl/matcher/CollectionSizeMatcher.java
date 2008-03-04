package com.objogate.wl.matcher;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import static org.hamcrest.Matchers.equalTo;
import org.hamcrest.TypeSafeMatcher;

import java.util.Collection;

public class CollectionSizeMatcher<C extends Collection<?>> extends TypeSafeMatcher<C> {
    private final Matcher<Integer> sizeMatcher;

    public CollectionSizeMatcher(Matcher<Integer> sizeMatcher) {
        this.sizeMatcher = sizeMatcher;
    }

    @Override
    public boolean matchesSafely(C collection) {
        return sizeMatcher.matches(collection.size());
    }

    public void describeTo(Description description) {
        description.appendText("a collection with size ").appendDescriptionOf(sizeMatcher);
    }

    @Factory
    public static <C extends Collection<?>> Matcher<C> ofSize(Matcher<Integer> sizeMatcher) {
        return new CollectionSizeMatcher<C>(sizeMatcher);
    }
    
    @Factory
    public static <C extends Collection<?>> Matcher<C> ofSize(int expectedSize) {
        return ofSize(equalTo(expectedSize));
    }
}
