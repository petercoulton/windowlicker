package com.objogate.wl.internal;

import org.hamcrest.Description;

import com.objogate.wl.Query;

public abstract class PropertyQuery<T,V> implements Query<T,V> {
    private final String propertyName;

    public PropertyQuery(String propertyName) {
        this.propertyName = propertyName;
    }

    public void describeTo(Description description) {
        description.appendText(propertyName);
    }
}
