package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.Automaton;

public class MouseWheelGesture implements Gesture {
    private int amount;

    public MouseWheelGesture(int amount) {
        this.amount = amount;
    }

    public void performGesture(Automaton automaton) {
        automaton.mouseWheel(amount);
    }


    public void describeTo(Description description) {
        description.appendText("scroll mouse wheel by ");
        description.appendValue(amount);
    }
}
