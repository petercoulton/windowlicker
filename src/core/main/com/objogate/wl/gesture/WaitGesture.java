package com.objogate.wl.gesture;

import org.hamcrest.Description;
import com.objogate.wl.Gesture;
import com.objogate.wl.Automaton;

public class WaitGesture implements Gesture {
    private final int waitMs;

    public WaitGesture(int waitMs) {
        this.waitMs = waitMs;
    }

    public void performGesture(Automaton automaton) {
        automaton.delay(waitMs);
    }
    
    public void describeTo(Description description) {
        description.appendText("wait ");
        description.appendValue(waitMs);
        description.appendText("ms");
    }
}
