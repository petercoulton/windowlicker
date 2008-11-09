package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.Automaton;

public class MouseReleaseGesture implements Gesture {
    private int buttons;

    public MouseReleaseGesture(int button) {
        this.buttons = button;
    }

    public void performGesture(Automaton automaton) {
        automaton.mouseRelease(buttons);
    }

    public void describeTo(Description description) {
        description.appendText("release button ");
        description.appendValueList("[", " ,", "]", Gestures.buttonListFor(buttons));
    }
}
