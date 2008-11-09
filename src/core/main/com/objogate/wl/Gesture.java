package com.objogate.wl;

import org.hamcrest.SelfDescribing;

public interface Gesture extends SelfDescribing {
    void performGesture(Automaton automaton);
}
