package com.objogate.wl.matcher;

import org.hamcrest.Matcher;

import javax.swing.*;
import java.awt.*;

public class ComponentMatchers {
    public static Matcher<Component> named(String name) {
        return new ComponentNameMatcher(name);
    }

    public static Matcher<Component> showingOnScreen() {
        return new ShowingOnScreenMatcher();
    }

    public static <T extends AbstractButton> Matcher<T> withMnemonicKey(int keyCode) {
        return new AbstractButtonAcceleratorMatcher<T>(keyCode);
    }

    public static <T extends AbstractButton> Matcher<T> withButtonText(String text) {
        return new AbstractButtonTextMatcher<T>(text);
    }
}
