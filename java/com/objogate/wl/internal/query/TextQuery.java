package com.objogate.wl.internal.query;

import org.hamcrest.Matcher;

public interface TextQuery {
    /**
     * Check if a component has matching text.
     *
     * @param matcher What to match for
     */
    void hasText(Matcher<String> matcher);
}
