package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class KeyPressGesture implements Gesture {
    private final int keyCode;

    public KeyPressGesture(int keyCode) {
        this.keyCode = keyCode;
    }

    public void performGesture(Automaton automaton) {
        automaton.keyPress(keyCode);
    }


    public void describeTo(Description description) {
        description.appendText("Press Key ");
        description.appendValue(keyCode);
    }
}
