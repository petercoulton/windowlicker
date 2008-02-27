package com.objogate.wl.matcher;

import java.awt.Color;
import java.awt.Component;

import org.hamcrest.Matcher;

public class BackgroundColorMatcher extends ComponentColorMatcher {
    public BackgroundColorMatcher(Color color) {
        super(color);
    }

    protected Color colorOf(Component component) {
        return component.getBackground();
    }

    public static Matcher<Component> hasBackground(Color color) {
        return new BackgroundColorMatcher(color);
    }
}
