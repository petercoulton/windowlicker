package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class KeyReleaseGesture implements Gesture {
    private final int keyCode;

    public KeyReleaseGesture(int keyCode) {
        this.keyCode = keyCode;
    }

    public void performGesture(Automaton automaton) {
        automaton.keyRelease(keyCode);
    }

    public void describeTo(Description description) {
        description.appendText("Release Key ");
        description.appendValue(keyCode);
    }
}
