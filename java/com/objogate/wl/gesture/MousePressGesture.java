package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.robot.Automaton;

public class MousePressGesture implements Gesture {
    private int buttons;

    /**
     * InputEvent.BUTTON1_MASK...
     */
    public MousePressGesture(int button) {
        this.buttons = button;
    }

    public void performGesture(Automaton automaton) {
        automaton.mousePress(buttons);
    }


    public void describeTo(Description description) {
        description.appendText("press button ");
        description.appendValueList("[", " ,", "]", Gestures.buttonListFor(buttons));
    }

}
