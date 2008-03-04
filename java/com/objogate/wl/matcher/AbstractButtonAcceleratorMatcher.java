package com.objogate.wl.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class AbstractButtonAcceleratorMatcher<T extends AbstractButton> extends TypeSafeMatcher<T> {
    private int keyCode;

    public AbstractButtonAcceleratorMatcher(int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public boolean matchesSafely(T buttonAlike) {
        return buttonAlike.getMnemonic() == keyCode;
    }

    public void describeTo(Description description) {
        description.appendText("accelerator equals '" );
        description.appendValue(KeyEvent.getKeyText(keyCode));
        description.appendText("'");
    }
}
