package com.objogate.wl.matcher;

import java.awt.Color;
import java.awt.Component;

import org.hamcrest.Matcher;

public class ForegroundColorMatcher extends ComponentColorMatcher {
    public ForegroundColorMatcher(Color color) {
        super(color);
    }

    protected Color colorOf(Component component) {
        return component.getForeground();
    }

    public static Matcher<Component> hasForeground(Color color) {
        return new ForegroundColorMatcher(color);
    }
}
