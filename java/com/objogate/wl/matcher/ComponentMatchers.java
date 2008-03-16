package com.objogate.wl.matcher;

import javax.swing.AbstractButton;
import javax.swing.JLabel;
import java.awt.Component;
import org.hamcrest.Matcher;

//TODO (nat): move these to the appropriate component driver classes
public class ComponentMatchers {
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
