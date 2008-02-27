package com.objogate.wl.internal.query;

import org.hamcrest.Matcher;

public interface TextQuery {
    void text(Matcher<String> text);
}
