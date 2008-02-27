package com.objogate.wl.matcher;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import java.awt.Component;
import org.hamcrest.Matcher;

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

    public static Matcher<Component> withFocus() {
        return new HasFocusMatcher();
    }

    public static Matcher<? super JLabel> withLabelText(Matcher<String> text) {
        return new JLabelTextMatcher(text);
    }
}
